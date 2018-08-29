package com.nerdgeeks.moviepad.prefs.data;

import com.nerdgeeks.moviepad.prefs.SharedPrefManager;

import java.util.List;

public class PrefDataManger {
    private SharedPrefManager prefManager;

    public PrefDataManger(SharedPrefManager prefManager){
        this.prefManager = prefManager;
    }

    public void clear(){
        prefManager.clear();
    }

    public boolean isPrefAvailable(String item){
        return prefManager.isPrefAvailable(item);
    }
}
