package com.example.mathriddles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView appName;
    private Button play;
    private Button levels;
    private Button restart;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    private void launchActivity(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}
