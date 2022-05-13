package com.example.e_lapor;

import android.content.Context;
import android.content.SharedPreferences;

public class tampil {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "intro_slider-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public tampil(Context context){
        this.context=context;
        pref=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor=pref.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.commit();

    }
    public boolean isFirstTimeLuanch(){
       return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}

