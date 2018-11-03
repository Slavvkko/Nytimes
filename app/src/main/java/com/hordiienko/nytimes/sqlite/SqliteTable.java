package com.hordiienko.nytimes.sqlite;

import android.provider.BaseColumns;

public class SqliteTable implements BaseColumns {
    public static final String TABLE_NAME = "articles";
    public static final String COL_URL = "url";
    public static final String COL_TITLE = "title";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_DATE = "date";
    public static final String COL_IMAGE = "image";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_URL + " TEXT," +
                    COL_TITLE + " TEXT," +
                    COL_DESCRIPTION + " TEXT," +
                    COL_DATE + " TEXT," +
                    COL_IMAGE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
