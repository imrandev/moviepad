package com.nerdgeeks.moviepad.constant;

import android.Manifest;

public class Config {

    // adMob string
    public static final String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713" ;
    public static final String ADMOB_INTERESTITIAL_ID = "" ;
    public static final String NATIVE_ADS_ID = "";
    public static final String BANNER_ADS_ID = "";
    public static final String REWARD_AD_ID = "ca-app-pub-3940256099942544/5224354917";

    // rest api
    public static final String YTS_API = "yts";
    public static final String OMDB_API = "omdb";

    // base url string
    public static final String OMDB_BASE_URL = "http://www.omdbapi.com";
    public static final String YTS_BASE_URL = "https://yts.am";

    // api url
    public static final String YTS_LIST_MOVIES = "/api/v2/list_movies.json";
    public static final String YTS_LIST_MOVIE_DETAILS = "/api/v2/movie_details.json";
    public static final String YTS_MOVIE_QUERY = "/api/v2/list_movies.json";

    // api key for authentication
    public static final String OMDM_API_KEY = "d1051d0";
    public static final String YOUTUBE_API_KEY = "AIzaSyAjM3HsCGtEMMEJDT9Rua9ytsilG8dRaGQ";

    // sqLite db strings
    public static final String DB_NAME = "moviepad_db";
    public static final String DC_DB_NAME = "moviepad_db_dc";
    public static final int DB_VERSION = 1;
    public static final String DB_TABLE = "Watch";
    public static final String DB_COLUMN_NAME = "name";
    public static final String DB_COLUMN_WATCH = "is_watch";
    public static final String DB_COLUMN_ID = "id";
    public static final String DB_COLUMN_IMG = "img_url";
    public static final String DB_COLUMN_TIME = "timestamp";
    public static final String DB_COLUMN_RATING = "rating";
    public static final String DB_COLUMN_GENRE = "genre";
    public static final String DB_COLUMN_YEAR = "year";

    public static final String DECISION_TABLE_NAME = "decision";
    public static final String DECISION_TABLE_COL_ACTION = "action";
    public static final String DECISION_TABLE_COL_ADVENTURE = "adventure";
    public static final String DECISION_TABLE_COL_ANIMATION = "animation";
    public static final String DECISION_TABLE_COL_BIO = "bio";
    public static final String DECISION_TABLE_COL_COMEDY = "comedy";
    public static final String DECISION_TABLE_COL_CRIME = "crime";
    public static final String DECISION_TABLE_COL_DOC = "doc";
    public static final String DECISION_TABLE_COL_DRAMA = "drama";
    public static final String DECISION_TABLE_COL_FAMILY = "family";
    public static final String DECISION_TABLE_COL_FANTASY = "fantasy";
    public static final String DECISION_TABLE_COL_HISTORY = "history";
    public static final String DECISION_TABLE_COL_HORROR = "horror";
    public static final String DECISION_TABLE_COL_MUSIC = "music";
    public static final String DECISION_TABLE_COL_MYSTERY = "mystery";
    public static final String DECISION_TABLE_COL_ROMANCE = "romance";
    public static final String DECISION_TABLE_COL_SCI = "sci";
    public static final String DECISION_TABLE_COL_SOCIAL = "social";
    public static final String DECISION_TABLE_COL_SPORT = "sport";
    public static final String DECISION_TABLE_COL_THRILLER = "thriller";
    public static final String DECISION_TABLE_COL_WAR = "war";


    // sqLite query
    public static final String createTableQuery =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, %s TEXT, %s TEXT, " +
                            "%s TEXT, %s TEXT, %s TEXT NOT NULL);"
            , DB_TABLE, DB_COLUMN_ID, DB_COLUMN_NAME, DB_COLUMN_WATCH, DB_COLUMN_IMG, DB_COLUMN_TIME
            , DB_COLUMN_RATING, DB_COLUMN_GENRE, DB_COLUMN_YEAR);
    public static final String MY_PREF = "moviepad_prefs";

    public static final String createDecisionTableQuery =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, " +
                            "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT," +
                            "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT," +
                            "%s TEXT,);"
                    , DECISION_TABLE_NAME, DB_COLUMN_ID, DECISION_TABLE_COL_ACTION, DECISION_TABLE_COL_ADVENTURE
                    ,DECISION_TABLE_COL_ANIMATION, DECISION_TABLE_COL_BIO, DECISION_TABLE_COL_COMEDY
                    ,DECISION_TABLE_COL_CRIME, DECISION_TABLE_COL_DOC, DECISION_TABLE_COL_DRAMA
                    ,DECISION_TABLE_COL_FAMILY, DECISION_TABLE_COL_FANTASY, DECISION_TABLE_COL_HISTORY,
                    DECISION_TABLE_COL_HORROR, DECISION_TABLE_COL_MUSIC, DECISION_TABLE_COL_MYSTERY,
                    DECISION_TABLE_COL_ROMANCE, DECISION_TABLE_COL_SCI, DECISION_TABLE_COL_SOCIAL,
                    DECISION_TABLE_COL_SPORT,DECISION_TABLE_COL_THRILLER,DECISION_TABLE_COL_WAR);

    // permission
    public static final int PERMISSIONS_REQUEST = 1;
    public static String[] PERMISSIONS_REQUIRED = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // list of genre
    public static String[] LIST_OF_GENRE = {
            "Action", "Adventure", "Animation", "Comedy", "Crime", "Drama", "Fantasy",
            "History", "Horror", "Magical", "Mystery","Philosophical",
            "Political", "Romance", "Sci-Fi", "Social", "Thriller"
    };
}
