package com.example.mathriddles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.mathriddles.MainActivity.APP_PREFERENCES;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;

    //Key buttons elements
    private List<Button> keybuttons = new ArrayList<>();
    private EditText input;
    private ImageButton enter;
    private ImageButton erase_from_input;
    private ImageButton helpHint;

    private Button next_level_button;

    private int index = 0;
    private Level level;
    private String[] textLevels;

    private Toolbar toolbar;
    private TextView riddle_text;
    private TextView hint_text;
    private TextView score_text;
    private TextView rotation_view;
    private TextView current_level_score;

    private RelativeLayout riddle_place;
    private RelativeLayout next_level_place;
    private LinearLayout timer_place;

    private Animation timer_animation;

    private int score = 100;

    private Sounds sounds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);

        sounds = new Sounds(this);
        timer_animation = AnimationUtils.loadAnimation(this, R.anim.time_animation);


        findAllViews();

        setOnActions();

        setAllLevelsInformation();

        getCurrentLevelIndex();

        setLevel();
    }

    private void findAllViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Level ");

        riddle_text = findViewById(R.id.riddle_text);
        riddle_place = findViewById(R.id.riddle_place);
        enter = findViewById(R.id.enter);
        helpHint = findViewById(R.id.helpHint);
        erase_from_input = findViewById(R.id.erase_from_input);
        hint_text = findViewById(R.id.hint_text);
        rotation_view = findViewById(R.id.timer);
        score_text = findViewById(R.id.score_text);

        timer_place = findViewById(R.id.timer_score);
        next_level_button = findViewById(R.id.next_level_button);
        current_level_score = findViewById(R.id.this_level_score);
        next_level_place = findViewById(R.id.next_level_place);

        input = findViewById(R.id.input);
        input.setFocusable(false);
        input.setClickable(false);
    }

    private void setOnActions() {

        for(int i = 0; i <= 9; i++){
            String buttonID = "button_" + i;
            int resId = getResources().getIdentifier(buttonID,"id",getPackageName());
            Button button = findViewById(resId);
            button.setOnClickListener(this);
            keybuttons.add(button);
        }

        next_level_button.setOnClickListener(this);
        helpHint.setOnClickListener(this);
        enter.setOnClickListener(this);
        erase_from_input.setOnClickListener(this);
    }

    private void getCurrentLevelIndex() {
        SharedPreferences mLevels = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mLevels.contains("solved_riddles")){
            //Get number of solved riddles to get index in riddle list
            index = mLevels.getInt("solved_riddles",0);
        }
    }

    private void setLevel() {
        //Set up bar title
        getSupportActionBar().setTitle("Level " + (index + 1));
        //Reset hint and next level place
        hint_text.setVisibility(View.INVISIBLE);
        next_level_place.setVisibility(View.INVISIBLE);
        //Reset stripe of play place
        riddle_place.setBackgroundResource(R.drawable.riddle_place);
        //Set current level
        level = new Level(textLevels[index],score_text,150,100);
        //Set level riddle to view
        riddle_text.setText(level.getRiddle());
        //Start increasing score thread
        level.startThread();
        //Start timer animation
        rotation_view.startAnimation(timer_animation);
    }

    private void setAllLevelsInformation() {
        //Get information about all levels
        textLevels = getResources().getStringArray(R.array.riddles);
    }

    @Override
    public void onClick(View v) {

        riddle_place.setBackgroundResource(R.drawable.riddle_place);

        for(Button button : keybuttons){
            if(button == v) {
                CharSequence charSequence = input.getText() + ((Button)v).getText().toString();
                input.setText(charSequence);
            }
        }

        if(v == erase_from_input) {
            input.setText("");
        }

        if(v == helpHint){
            hint_text.setVisibility(View.VISIBLE);
            hint_text.setText(level.getHint());
        }

        if(v == enter) {
            checkCorrectAnswer();
        }

        if(v == next_level_button){
            setLevel();
        }
        sounds.getButtonSound();
    }

    private void checkCorrectAnswer() {
        if(input.getText().toString().equals(level.getAnswer())) {
            //If there is max level open the win activity
            index++;
            if (index >= textLevels.length) {
                launchActivity(WinScreen.class);
            } else {
                //Set empty input field
                input.setText("");
                riddle_place.setBackgroundResource(R.drawable.correct_answer);
                //Reset score and stop score thread and animation
                score = 100;
                level.stopThread();
                rotation_view.clearAnimation();
                //Show level score
                next_level_place.setVisibility(View.VISIBLE);
                current_level_score.setText("This level score is " + level.getScore());
            }
        } else {
            riddle_place.setBackgroundResource(R.drawable.wrong_answer);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        launchActivity(LevelsActivity.class);
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }
}
