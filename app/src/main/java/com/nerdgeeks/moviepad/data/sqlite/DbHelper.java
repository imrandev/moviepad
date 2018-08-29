package com.nerdgeeks.moviepad.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nerdgeeks.moviepad.data.sqlite.domain.Watch;

import java.io.File;
import java.util.ArrayList;

import static com.nerdgeeks.moviepad.constant.Config.*;

/**
 * Created by IMRAN on 9/13/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewMovie(Watch watch){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_ID, watch.getId());
        values.put(DB_COLUMN_NAME, watch.getName());
        values.put(DB_COLUMN_GENRE, watch.getGenre());
        values.put(DB_COLUMN_IMG, watch.getImgUrl());
        values.put(DB_COLUMN_RATING, watch.getRating());
        values.put(DB_COLUMN_TIME, watch.getTime());
        values.put(DB_COLUMN_WATCH, watch.isWatched());
        values.put(DB_COLUMN_YEAR, watch.getYear());
        db.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    protected void onDeleteMovie(Watch watch, String column){
        if (watch == null){
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        switch (column){
            case DB_COLUMN_ID:
                if (!String.valueOf(watch.getId()).isEmpty()){
                    db.delete(DB_TABLE, DB_COLUMN_ID + " = ?", new String[]{String.valueOf(watch.getId())});
                }
                break;
            case DB_COLUMN_NAME:
                if (!watch.getName().isEmpty()){
                    db.delete(DB_TABLE, DB_COLUMN_NAME + " = ?", new String[]{watch.getName()});
                }
                break;
            case DB_COLUMN_GENRE:
                if (!watch.getGenre().isEmpty()){
                    db.delete(DB_TABLE, DB_COLUMN_GENRE + " = ?", new String[]{watch.getGenre()});
                }
                break;
            case DB_COLUMN_IMG:
                if (!watch.getImgUrl().isEmpty()){
                    db.delete(DB_TABLE, DB_COLUMN_IMG + " = ?", new String[]{watch.getImgUrl()});
                }
                break;
            case DB_COLUMN_RATING:
                if (!watch.getRating().isEmpty()){
                    db.delete(DB_TABLE, DB_COLUMN_RATING + " = ?", new String[]{watch.getRating()});
                }
                break;
            case DB_COLUMN_TIME:
                if (!watch.getTime().isEmpty()){
                    db.delete(DB_TABLE, DB_COLUMN_TIME + " = ?", new String[]{watch.getTime()});
                }
                break;
            case DB_COLUMN_YEAR:
                if (!watch.getYear().isEmpty()){
                    db.delete(DB_TABLE, DB_COLUMN_YEAR + " = ?", new String[]{watch.getYear()});
                }
                break;
            case DB_COLUMN_WATCH:
                if (String.valueOf(watch.isWatched()).equals("true")
                        || String.valueOf(watch.isWatched()).equals("false")){
                    String watched = String.valueOf(watch.isWatched());
                    db.delete(DB_TABLE, DB_COLUMN_WATCH + " = ?", new String[]{watched});
                }
                break;
        }
        db.close();
    }

    public void onDeleteMovie(Watch watch){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN_ID + " = ?",
                new String[]{String.valueOf(watch.getId())});
        db.close();
    }

    public ArrayList<Watch> onRetrieveMovieList(){
        ArrayList<Watch> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN_ID, DB_COLUMN_NAME, DB_COLUMN_GENRE,
                        DB_COLUMN_IMG, DB_COLUMN_RATING, DB_COLUMN_TIME, DB_COLUMN_WATCH, DB_COLUMN_YEAR}, null,
                null, null, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Watch watch = new Watch();
                    watch.setId(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_ID)));
                    watch.setName(cursor.getString(cursor.getColumnIndex(DB_COLUMN_NAME)));
                    watch.setGenre(cursor.getString(cursor.getColumnIndex(DB_COLUMN_GENRE)));
                    watch.setRating(cursor.getString(cursor.getColumnIndex(DB_COLUMN_RATING)));
                    watch.setImgUrl(cursor.getString(cursor.getColumnIndex(DB_COLUMN_IMG)));
                    watch.setTime(cursor.getString(cursor.getColumnIndex(DB_COLUMN_TIME)));
                    watch.setWatched(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DB_COLUMN_WATCH))));
                    watch.setYear(cursor.getString(cursor.getColumnIndex(DB_COLUMN_YEAR)));
                    movieList.add(watch);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return movieList;
        } else {
            return null;
        }
    }

    public int onUpdateMovie(Watch watch) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_ID, watch.getId());
        values.put(DB_COLUMN_NAME, watch.getName());
        values.put(DB_COLUMN_GENRE, watch.getGenre());
        values.put(DB_COLUMN_IMG, watch.getImgUrl());
        values.put(DB_COLUMN_RATING, watch.getRating());
        values.put(DB_COLUMN_TIME, watch.getTime());
        values.put(DB_COLUMN_YEAR, watch.getYear());
        values.put(DB_COLUMN_WATCH, watch.isWatched());

        // updating row
        return db.update(DB_TABLE, values, DB_COLUMN_ID + " = ?",
                new String[] { String.valueOf(watch.getId()) });
    }

    public void deleteByEncodedTitle(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN_NAME + " = ?", new String[]{title});
        db.close();
    }

    private void deleteByEncodedImage(String encode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN_IMG + " = ?", new String[]{encode});
        db.close();
    }

    private void deleteById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN_ID + " = ?", new String[]{Integer.toString(id)});
        db.close();
    }

    public void updateCheckedRow(String title, String bool){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_WATCH, bool);
        db.update(DB_TABLE, values, DB_COLUMN_NAME + " = ?", new String[]{title});
        db.close();
    }

    public ArrayList<String> getMovieList(){
        ArrayList<String> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN_NAME}, null,
                null, null, null, null);
        if (cursor != null){
            while (cursor.moveToNext()){
                int index = cursor.getColumnIndex(DB_COLUMN_NAME);
                movieList.add(cursor.getString(index));
            }
            cursor.close();
            db.close();
            return movieList;
        } else {
            return null;
        }
    }

    public String getEncodedImageById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN_IMG}, DB_COLUMN_ID + " =? ",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null){
                cursor.moveToFirst();
                String encode = cursor.getString(cursor.getColumnIndex(DB_COLUMN_IMG));
                cursor.close();
                return encode;
            } else
                return "";
        } catch (CursorIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
        return "";
    }

    public int getIdByTitle(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN_ID}, DB_COLUMN_NAME + " =? ",
                    new String[]{title}, null, null, null, null);
            if (cursor != null){
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(DB_COLUMN_ID));
                cursor.close();
                return id;
            } else
                return 0;
        } catch (CursorIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
        return 0;
    }

    public ArrayList<String> getCheckList(){
        ArrayList<String> checkList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN_WATCH}, null,
                null, null, null, null);
        if (cursor != null){
            while (cursor.moveToNext()){
                int index = cursor.getColumnIndex(DB_COLUMN_WATCH);
                checkList.add(cursor.getString(index));
            }
            cursor.close();
            db.close();
            return checkList;
        } else {
            return null;
        }
    }

    public ArrayList<String> getImageList(){
        ArrayList<String> imgList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN_IMG}, null,
                null, null, null, null);
        if (cursor != null){
            while (cursor.moveToNext()){
                int index = cursor.getColumnIndex(DB_COLUMN_IMG);
                imgList.add(cursor.getString(index));
            }
            cursor.close();
            db.close();
            return imgList;
        } else {
            return null;
        }
    }

    public boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();

        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master " +
                "where " + "tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public boolean existsColumnInTable(String inTable, String columnToCheck) {
        Cursor mCursor = null;
        SQLiteDatabase inDatabase = this.getReadableDatabase();
        try {
            // Query 1 row
            mCursor = inDatabase.rawQuery("SELECT * FROM " + inTable + " LIMIT 0", null);

            // getColumnIndex() gives us the index (0 to ...) of the column - otherwise we get a -1
            if (mCursor.getColumnIndex(columnToCheck) != -1)
                return true;
            else
                return false;

        } catch (Exception Exp) {
            return false;
        } finally {
            if (mCursor != null) mCursor.close();
        }
    }

    public boolean isDataExists(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DB_TABLE + " WHERE "
                + DB_COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        boolean exist = (cursor.getCount() > 0);
        cursor.close();
        return exist;
    }
}
