package com.hordiienko.nytimes.ui.article_list;

import android.widget.ImageView;

import com.hordiienko.nytimes.App;
import com.hordiienko.nytimes.db.DataBase;
import com.hordiienko.nytimes.model.Article;
import com.hordiienko.nytimes.network.NetworkHelper;
import com.hordiienko.nytimes.db.realm.RealmService;
import com.hordiienko.nytimes.utils.ImageSaveHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticleListPresenter implements ArticleListContract.Presenter {
    private static final int PAGE_SIZE = 20;

    private ArticleListContract.View articleListView;
    private Disposable disposable;
    private DataBase db;

    private int loadedCount;
    private boolean isProgressShow;
    private boolean isFooterProgressShow;
    private boolean isLoading;

    public ArticleListPresenter(ArticleListContract.View view) {
        articleListView = view;

        db = RealmService.getInstance();
    }

    @Override
    public void onViewReady() {
        isProgressShow = true;
        articleListView.showProgress();

        onViewRefresh();
    }

    @Override
    public void onViewRefresh() {
        if (!isLoading) {
            loadedCount = 0;
            loadData();
        }
    }

    @Override
    public void onScrollEnd() {
        if (!isLoading) {
            isFooterProgressShow = true;
            articleListView.showFooterProgress();
            loadData();
        }
    }

    @Override
    public void onClickFavorite(Article article, ImageView thumb) {
        if (!article.isFavorite()) {
            article.setFavorite(true);

            String imageFile = ImageSaveHelper.saveImage(App.getInstance(), thumb);
            article.setImage(imageFile);

            db.addArticleToFavorite(article);

        } else {
            article.setFavorite(false);

            if (article.getImage() != null) {
                ImageSaveHelper.deleteImage(article.getImage());
            }

            db.removeArticleFromFavorite(article);
        }

        articleListView.updateItem(article);
    }

    @Override
    public void onViewDestroy() {
        articleListView = null;

        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void loadData() {
        isLoading = true;

        Single<List<Article>> singleNetwork = NetworkHelper.getInstance().getArticles(articleListView.getApiType(), loadedCount / PAGE_SIZE * PAGE_SIZE);
        Single<List<Article>> singleRealm = db.getFavoriteArticles();

        Single<List<Article>> singleFinal = Single.timer(1, TimeUnit.SECONDS).flatMap(aLong -> Single.zip(singleNetwork, singleRealm, (articles, articles2) -> {
            for (Article article : articles) {
                for (Article article1 : articles2) {
                    if (article.equals(article1)) {
                        article.setFavorite(true);

                        // set image to file name for future delete
                        article.setImage(article1.getImage());
                    }
                }
            }

            return articles;
        }));

        singleFinal.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(articleObserver);
    }

    private void endLoading() {
        if (isFooterProgressShow) {
            isFooterProgressShow = false;
            articleListView.hideFooterProgress();
        } else if (isProgressShow) {
            isProgressShow = false;
            articleListView.hideProgress();
        }

        isLoading = false;
    }

    private SingleObserver<List<Article>> articleObserver = new SingleObserver<List<Article>>() {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onSuccess(List<Article> articles) {
            endLoading();

            int newArticlesCount = articles.size() - (loadedCount - (loadedCount / PAGE_SIZE * PAGE_SIZE));

            if (newArticlesCount > 0) {
                if (loadedCount > 0) {
                    articleListView.appendData(articles.subList(articles.size() - newArticlesCount, articles.size()));
                } else {
                    articleListView.setData(articles);
                }

                loadedCount += articles.size();
            }
        }

        @Override
        public void onError(Throwable e) {
            endLoading();
            articleListView.showFailure(e);
        }
    };
}
