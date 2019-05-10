package com.example.mathriddles;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

public class Sounds {

    private MediaPlayer button_sound;
    private MediaPlayer solve_sound;
    private MediaPlayer mistake_sound;
    private Context context;

    public Sounds(Context context){
        this.context = context;

        getSounds();
    }

    private void getSounds(){
        button_sound = MediaPlayer.create(context, R.raw.button_click);
        solve_sound = MediaPlayer.create(context, R.raw.solve);
        mistake_sound = MediaPlayer.create(context,R.raw.mistake);
    }

    public void getButtonSound(){
        button_sound.start();
    }

    public void getSolveSound(){
        solve_sound.start();
    }

    public void getMistakeSound(){
        mistake_sound.start();
    }
}
