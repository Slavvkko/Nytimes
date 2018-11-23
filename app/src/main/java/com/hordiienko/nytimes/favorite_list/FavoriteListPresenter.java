package com.hordiienko.nytimes.favorite_list;

import com.hordiienko.nytimes.db.DataBase;
import com.hordiienko.nytimes.model.Article;
import com.hordiienko.nytimes.db.realm.RealmService;
import com.hordiienko.nytimes.utils.ImageSaveHelper;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class FavoriteListPresenter implements FavoriteListContract.Presenter {
    private FavoriteListContract.View favoriteListView;

    private Disposable disposable;
    private DataBase db;

    public FavoriteListPresenter(FavoriteListContract.View view) {
        favoriteListView = view;
        db = RealmService.getInstance();
    }


    @Override
    public void onViewReady() {
        favoriteListView.showProgress();
        loadData();
    }

    @Override
    public void onClickRemoveFavorite(Article article) {
        if (article.getImage() != null) {
            ImageSaveHelper.deleteImage(article.getImage());
        }

        db.removeArticleFromFavorite(article);
        favoriteListView.removeItem(article);
    }

    @Override
    public void onViewDestroy() {
        favoriteListView = null;

        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void loadData() {
        Single<List<Article>> singleRealm = db.getFavoriteArticles();
        singleRealm.observeOn(AndroidSchedulers.mainThread()).subscribe(articleObserver);
    }

    private void endLoading() {
        favoriteListView.hideProgress();
    }

    private SingleObserver<List<Article>> articleObserver = new SingleObserver<List<Article>>() {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onSuccess(List<Article> articles) {
            endLoading();
            favoriteListView.setData(articles);
        }

        @Override
        public void onError(Throwable e) {
            endLoading();
            favoriteListView.showFailure(e);
        }
    };
}
