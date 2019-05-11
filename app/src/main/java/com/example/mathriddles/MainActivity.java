package com.example.mathriddles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

import java.util.zip.Inflater;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @NotNull
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.play) public Button play;
    @BindView(R.id.levels) public Button levels;
    @BindView(R.id.restart) public Button restart;
    @BindView(R.id.exit) public Button exit;

    //Shared preferences
    public static final String APP_PREFERENCES = "levels_count";
    public SharedPreferences mLevels;

    //Sounds
    private Sounds sounds;

    //Firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Math Riddles");

        sounds = new Sounds(this);
        mLevels = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        setOnClickActions();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Get id current user
        FirebaseUser current_user = mAuth.getCurrentUser();
        //If there is no such user start sign in activity
        if(current_user == null){
            launchActivity(SignInActivity.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.main_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                launchActivity(ProfileActivity.class);
                return true;
            case R.id.leader_board:
                launchActivity(LeaderBoardActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setOnClickActions() {
        play.setOnClickListener(this);
        exit.setOnClickListener(this);
        levels.setOnClickListener(this);
        restart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        sounds.getButtonSound();

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

        if(activity == SignInActivity.class){
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        } else {
            startActivity(intent);
        }

        overridePendingTransition(0,0);
    }
}
