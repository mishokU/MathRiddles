package com.example.mathriddles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.example.mathriddles.MainActivity.APP_PREFERENCES;

public class WinScreen extends AppCompatActivity implements View.OnClickListener {

    public SharedPreferences mLevels;

    private Button reset;
    private Button toMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_activity);

        findViews();
        setActions();
    }

    private void setActions() {
        reset.setOnClickListener(this);
        toMain.setOnClickListener(this);
    }

    private void findViews() {
        mLevels = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        reset = findViewById(R.id.reset);
        toMain = findViewById(R.id.return_to_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View v) {

        if(v == reset){
            resetLevels();
            onBackPressed();
        }

        if(v == toMain){
            onBackPressed();
        }
    }

    private void resetLevels() {
        SharedPreferences.Editor editor = mLevels.edit();
        editor.putInt("solved_riddles", 0);
        editor.apply();
    }
}
