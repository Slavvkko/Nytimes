package com.hordiienko.nytimes.db;

import com.hordiienko.nytimes.model.Article;

import java.util.List;

import io.reactivex.Single;

public interface DataBase {
    void addArticleToFavorite(Article article);
    void removeArticleFromFavorite(Article article);
    Single<List<Article>> getFavoriteArticles();
}
