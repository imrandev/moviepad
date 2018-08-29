package com.nerdgeeks.moviepad.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import com.nerdgeeks.moviepad.constant.Config;

public class SharedPrefManager {

    private SharedPreferences sharedPreferences;

    public SharedPrefManager(Context context){
        sharedPreferences = context.getSharedPreferences(Config.MY_PREF, Context.MODE_PRIVATE);
    }

    public void clear(){
        sharedPreferences.edit().clear().apply();
    }

    public boolean isPrefAvailable(String item){
        return sharedPreferences.contains(item);
    }
}
