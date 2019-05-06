package com.example.mathriddles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Shared preferences
    public static final String APP_PREFERENCES = "levels_count";
    public SharedPreferences mLevels;
    private MediaPlayer button_sound;

    private TextView appName;
    private Button play;
    private Button levels;
    private Button restart;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_sound = MediaPlayer.create(this,R.raw.button_click);
        mLevels = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        findAllViews();
        setOnActions();
    }

    private void setOnActions() {
        play.setOnClickListener(this);
        exit.setOnClickListener(this);
        levels.setOnClickListener(this);
        restart.setOnClickListener(this);
    }

    private void findAllViews() {
        appName = findViewById(R.id.app_name);
        play = findViewById(R.id.play);
        levels = findViewById(R.id.levels);
        restart = findViewById(R.id.restart);
        exit = findViewById(R.id.exit);
    }

    @Override
    public void onClick(View v) {

        button_sound.start();

        if(v == play){
            launchActivity(PlayActivity.class);
        }

        if(v == exit){
            finish();
        }

        if(v == restart) {
            restartLevels();
        }

        if(v == levels){
            launchActivity(LevelsActivity.class);
        }
    }

    private void restartLevels() {
        SharedPreferences.Editor editor = mLevels.edit();
        editor.putInt("solved_riddles",0);
        editor.apply();
    }

    private void launchActivity(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

}
