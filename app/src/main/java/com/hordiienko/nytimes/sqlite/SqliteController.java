package com.hordiienko.nytimes.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.hordiienko.nytimes.model.Article;

import java.util.List;

import io.reactivex.Single;

public class SqliteController {
    private static SqliteController instance;

    private SqliteHelper sqliteHelper;

    public static void init(Context context) {
        instance = new SqliteController(context);
    }

    public static SqliteController getInstance() {
        return instance;
    }

    private SqliteController(Context context) {
        sqliteHelper = new SqliteHelper(context);
    }

    public void addArticleToFavorite(Article article) {
        ContentValues values = new ContentValues();

        values.put(SqliteTable.COL_URL, article.getUrl());
        values.put(SqliteTable.COL_TITLE, article.getTitle());
        values.put(SqliteTable.COL_DESCRIPTION, article.getDescription());
        values.put(SqliteTable.COL_DATE, article.getDate());
        values.put(SqliteTable.COL_IMAGE, article.getImage());

        sqliteHelper.insertData(SqliteTable.TABLE_NAME, values);
    }

    public void removeArticleFromFavorite(Article article) {
        sqliteHelper.removeArticle(article.getUrl());
    }

    public Single<List<Article>> getFavoriteArticles() {
        return Single.create(emitter -> emitter.onSuccess(sqliteHelper.getArticles()));
    }
}
