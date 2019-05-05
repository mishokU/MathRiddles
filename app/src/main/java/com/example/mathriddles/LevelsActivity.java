package com.example.mathriddles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import static com.example.mathriddles.MainActivity.APP_PREFERENCES;

public class LevelsActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences mLevels;

    private int complete_buttons = 0;
    private final int countButtons = 11;
    private Toolbar toolbar;
    private List<Button> levels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels_activity);

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

            if(i < complete_buttons) {
                button.setBackgroundResource(R.drawable.complete_level);
                button.setOnClickListener(this);
                levels.add(button);
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
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View v) {
        for(Button level : levels){
            if(level == v) {
                launchActivity(((Button)v).getText().toString());
            }
        }

    }

    private void launchActivity(String level) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }
}
