package com.example.mathriddles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import static com.example.mathriddles.MainActivity.APP_PREFERENCES;

public class LevelsActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences mLevels;
    private MediaPlayer button_sound;

    private int complete_buttons = 0;
    private final int countButtons = 15;
    private Toolbar toolbar;
    private List<Button> levels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels_activity);

        button_sound = MediaPlayer.create(this, R.raw.button_click);

        mLevels = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if(mLevels.contains("solved_riddles")){
            complete_buttons = mLevels.getInt("solved_riddles", 0);
        }

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SELECT LEVEL");

        setButtons();
    }

    private void setButtons() {
        for(int i = 0; i <= countButtons; i++){
            String buttonID = "button_" + i;
            int resId = getResources().getIdentifier(buttonID,"id",getPackageName());
            Button button = findViewById(resId);
             button.setBackgroundResource(R.drawable.complete_level);
             button.setOnClickListener(this);
             levels.add(button);
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
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View v) {

        button_sound.start();

        for(Button level : levels){
            if(level == v) {
                launchActivity(((Button)v).getText().toString());
            }
        }

    }

    private void launchActivity(String level) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("level", Integer.parseInt(level)-1);
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }
}
