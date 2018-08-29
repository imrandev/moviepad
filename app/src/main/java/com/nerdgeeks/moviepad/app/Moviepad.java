package com.nerdgeeks.moviepad.app;

import android.app.Application;

import com.nerdgeeks.moviepad.data.sqlite.data.DataManager;

public class Moviepad extends Application {

    private DataManager manager;
    @Override
    public void onCreate() {
        super.onCreate();
        manager = new DataManager(this);
    }

    public DataManager getManager(){
        return manager;
    }
}
