package com.example.mathriddles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.mathriddles.MainActivity.APP_PREFERENCES;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    public SharedPreferences mLevels;
    private MediaPlayer mp;

    private TextView riddle_text;
    private Toolbar toolbar;
    private List<Button> keybuttons = new ArrayList<>();
    private EditText input;
    private ImageButton enter;
    private ImageButton erase_from_input;
    private ImageButton helpHint;

    private final int solvedRiddles = 11;
    private String[] riddles;
    private String[] answers;
    private int countOfRiddles = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);

        mp = MediaPlayer.create(this, R.raw.button_click);

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
    }

    private void setLevel() {
        toolbar.setTitle("Level " + countOfRiddles);
        riddle_text.setText(riddles[countOfRiddles]);
        riddle_text.setVisibility(View.VISIBLE);
    }

    private void setOnActions() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        for(int i = 0; i <= 9; i++){
            String buttonID = "button_" + i;
            int resId = getResources().getIdentifier(buttonID,"id",getPackageName());
            Button button = findViewById(resId);
            button.setOnClickListener(this);
            keybuttons.add(button);
        }

        enter.setOnClickListener(this);
        erase_from_input.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mp.start();
        for(Button button : keybuttons){
            if(button == v) {
                CharSequence charSequence = input.getText() + ((Button)v).getText().toString();
                input.setText(charSequence);
            }
        }

        if(v == erase_from_input) {
            input.setText("");
        }

        if(v == enter) {
            if(input.getText().toString().equals(answers[countOfRiddles])){

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

            } else {
                Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_LONG).show();
            }
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
    }
}
