package com.example.mathriddles;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Level extends Riddle {

    private Riddle riddle;
    private Thread scoreThread;
    private TextView active_view;
    private int score;
    private int level_speed;
    private String level_text;

    public Level(String level_text, TextView active_view,int level_speed, int score) {
        super(level_text);

        this.active_view = active_view;
        this.level_speed = level_speed;
        this.score = score;
        this.level_text = level_text;

        init();
    }

    public void stopThread(){
        scoreThread.interrupt();
    }

    public void startThread(){
        scoreThread.start();
    }

    private void init() {
        riddle = new Riddle(level_text);

        scoreThread = new Thread(){
            @Override
            public void run() {
                try {
                    while (score-- > 1 && !isInterrupted()) {
                        Thread.sleep(level_speed);
                        updateUI();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                active_view.setText("" + score);
            }
        });
    }

    private void runOnUiThread(Runnable r){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(r);
    }

    public int getScore(){
        return score;
    }
}
