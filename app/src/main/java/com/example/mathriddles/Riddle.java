package com.example.mathriddles;

public class Riddle {

    private String riddle ="";
    private String answer = "";
    private CharSequence hint = "";

    Riddle(String level){
        String[] level_text = level.split(":");
        this.riddle = level_text[0];
        this.answer = level_text[1];

        if(level_text.length >= 3){
            this.hint = level_text[2];
        }
    }

    public void setRiddle(String riddle){
        this.riddle = riddle;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public void setHint(String hint){
        this.hint = hint;
    }

    public String getRiddle(){
        return riddle;
    }

    public String getAnswer(){
        return answer;
    }

    public CharSequence getHint(){
        return hint;
    }
}
