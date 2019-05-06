package com.example.mathriddles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.mathriddles.MainActivity.APP_PREFERENCES;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    public SharedPreferences mLevels;
    private MediaPlayer mp;
    private MediaPlayer solve_sound;
    private MediaPlayer mistake_sound;

    private TextView riddle_text;
    private Toolbar toolbar;
    private List<Button> keybuttons = new ArrayList<>();
    private EditText input;
    private ImageButton enter;
    private ImageButton erase_from_input;
    private ImageButton helpHint;
    private TextView hint_text;

    private final int solvedRiddles = 11;
    private final int totalButtons = 9;

    private String[] riddles;
    private String[] answers;
    private String[] hints;
    private int countOfRiddles = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);

        mp = MediaPlayer.create(this, R.raw.button_click);
        solve_sound = MediaPlayer.create(this, R.raw.solve);
        mistake_sound = MediaPlayer.create(this,R.raw.mistake);

        getSolvedRiddles();
        findAllViews();
        setOnActions();
        getRiddlesFromArray();

        getLevel();
        setLevel();
    }

    private void getSolvedRiddles() {
        mLevels = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if(mLevels.contains("solved_riddles")){
            countOfRiddles = mLevels.getInt("solved_riddles",0);
        }

        if(countOfRiddles >= solvedRiddles) {
            countOfRiddles = 0;
        }
    }

    private void findAllViews() {

        toolbar = findViewById(R.id.toolbar);
        riddle_text = findViewById(R.id.riddle_text);
        enter = findViewById(R.id.enter);
        helpHint = findViewById(R.id.helpHint);
        erase_from_input = findViewById(R.id.erase_from_input);
        hint_text = findViewById(R.id.hint_text);

        input = findViewById(R.id.input);
        input.setFocusable(false);
        input.setClickable(false);
    }

    private void getLevel() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            countOfRiddles = Integer.parseInt(bundle.getString("level")) - 1;
        }
    }

    private void getRiddlesFromArray() {
        riddles = getResources().getStringArray(R.array.array_of_riddles);
        answers = getResources().getStringArray(R.array.answers_to_riddles);
        hints = getResources().getStringArray(R.array.hints_to_riddles);
    }

    private void setLevel() {
        getSupportActionBar().setTitle("Level " + (countOfRiddles + 1));
        riddle_text.setText(riddles[countOfRiddles]);
    }

    private void setOnActions() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Level " + (countOfRiddles + 1));

        for(int i = 0; i <= totalButtons; i++){
            String buttonID = "button_" + i;
            int resId = getResources().getIdentifier(buttonID,"id",getPackageName());
            Button button = findViewById(resId);
            button.setOnClickListener(this);
            keybuttons.add(button);
        }

        helpHint.setOnClickListener(this);
        enter.setOnClickListener(this);
        erase_from_input.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        mp.start();

        hint_text.setVisibility(View.INVISIBLE);

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
            showHint();
        }

        if(v == enter) {
            if(input.getText().toString().equals(answers[countOfRiddles])){
                solve_sound.start();
                getNextLevel();
            } else {
                mistake_sound.start();
            }
        }
    }

    private void getNextLevel() {
        input.setText("");

        countOfRiddles++;

        SharedPreferences.Editor editor = mLevels.edit();
        editor.putInt("solved_riddles", countOfRiddles);
        editor.apply();

        if(countOfRiddles >= riddles.length){
            launchActivity(WinScreen.class);
        } else {
            setLevel();
        }
    }


    private void showHint() {
        hint_text.setText(hints[countOfRiddles]);
        hint_text.setVisibility(View.VISIBLE);
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
    }
}
