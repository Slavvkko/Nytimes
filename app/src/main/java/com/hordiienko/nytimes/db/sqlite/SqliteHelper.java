package com.hordiienko.nytimes.db.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hordiienko.nytimes.model.Article;

import java.util.LinkedList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "articles.db";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqliteTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqliteTable.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean insertData(String table, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(table, null, values) != -1;
    }

    public boolean removeArticle(String url) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(SqliteTable.TABLE_NAME,
                SqliteTable.COL_URL + " =? ",
                new String[]{url}) > 0;
    }

    public List<Article> getArticles() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                SqliteTable.COL_URL,
                SqliteTable.COL_TITLE,
                SqliteTable.COL_DESCRIPTION,
                SqliteTable.COL_DATE,
                SqliteTable.COL_IMAGE
        };

        Cursor cursor = db.query(
                SqliteTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        List<Article> articles = new LinkedList<>();

        while (cursor.moveToNext()) {
            Article article = new Article(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));

            article.setFavorite(true);

            articles.add(article);
        }

        cursor.close();
        return articles;
    }
}
