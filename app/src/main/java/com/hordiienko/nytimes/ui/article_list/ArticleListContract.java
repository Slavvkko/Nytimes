package com.hordiienko.nytimes.ui.article_list;

import android.widget.ImageView;

import com.hordiienko.nytimes.model.Article;

import java.util.List;

public interface ArticleListContract {
    interface View {
        void showProgress();
        void hideProgress();
        void showFooterProgress();
        void hideFooterProgress();
        String getApiType();
        void setData(List<Article> articles);
        void appendData(List<Article> articles);
        void showFailure(Throwable t);
        void updateItem(Article article);
    }

    interface Presenter {
        void onViewReady();
        void onViewRefresh();
        void onScrollEnd();
        void onClickFavorite(Article article, ImageView thumb);
        void onViewDestroy();
    }
}
