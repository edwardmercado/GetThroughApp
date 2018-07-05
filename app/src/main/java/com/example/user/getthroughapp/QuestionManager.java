package com.example.user.getthroughapp;

import android.content.Context;
import android.content.SharedPreferences;

public class QuestionManager {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Context context;


    public QuestionManager(Context context){
        this.context=context;
        sharedPref=context.getSharedPreferences("first",0);
        editor = sharedPref.edit();
    }

    public void setFirst(boolean isFirst){
        editor.putBoolean("checked", isFirst);
        editor.commit();
    }

    public boolean checked(){
        return sharedPref.getBoolean("checked", true);
    }

}
