package com.nerdgeeks.moviepad.data.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nerdgeeks.moviepad.constant.Config.DB_VERSION;
import static com.nerdgeeks.moviepad.constant.Config.DC_DB_NAME;
import static com.nerdgeeks.moviepad.constant.Config.DECISION_TABLE_NAME;
import static com.nerdgeeks.moviepad.constant.Config.createDecisionTableQuery;

public class DecisionHelper extends SQLiteOpenHelper {


    public DecisionHelper(Context context) {
        super(context, DC_DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDecisionTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", DECISION_TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }

    public int getCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) AS TOTAL FROM " + DECISION_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getInt(cursor.getColumnIndex("TOTAL"));
        cursor.close();
        return count;
    }

    public void getTopColumn(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DECISION_TABLE_NAME;

        String q = "select count(*) as Result from blog_posts\n" +
                "group by blog_id\n" +
                "order by Result desc\n" +
                "limit 1";
    }
}
