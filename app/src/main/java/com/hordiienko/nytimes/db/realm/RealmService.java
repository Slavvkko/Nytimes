package com.hordiienko.nytimes.db.realm;

import com.hordiienko.nytimes.db.DataBase;
import com.hordiienko.nytimes.model.Article;

import java.util.List;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmService implements DataBase {
    private static RealmService instance;

    public static RealmService getInstance() {
        if (instance == null) {
            instance = new RealmService();
        }

        return instance;
    }

    private RealmService() {
    }

    @Override
    public void addArticleToFavorite(Article article) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(r -> r.copyToRealm(article));
        }
    }

    @Override
    public void removeArticleFromFavorite(Article article) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(r -> {
                RealmResults<Article> result = r.where(Article.class).equalTo("url", article.getUrl()).findAll();
                result.deleteAllFromRealm();
            });
        }
    }

    @Override
    public Single<List<Article>> getFavoriteArticles() {
        return Single.create(emitter -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                RealmResults<Article> results = realm.where(Article.class).findAll();
                List<Article> list = realm.copyFromRealm(results);
                emitter.onSuccess(list);
            }
        });
    }
}
