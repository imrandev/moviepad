package com.nerdgeeks.moviepad.data.sqlite.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nerdgeeks.moviepad.data.sqlite.DbHelper;
import com.nerdgeeks.moviepad.data.sqlite.domain.Watch;

public class DataManager extends DbHelper {

    public DataManager(Context context) {
        super(context);
    }

    public void createDb(SQLiteDatabase db) {
        this.onCreate(db);
    }

    public void upgradeDb(SQLiteDatabase db, int oldVersion, int newVersion){
        this.onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(Watch watch) {
        this.insertNewMovie(watch);
    }

    public void delete(Watch watch, String column) {
        this.onDeleteMovie(watch, column);
    }

    public void update(Watch watch) {
        this.onUpdateMovie(watch);
    }
}
