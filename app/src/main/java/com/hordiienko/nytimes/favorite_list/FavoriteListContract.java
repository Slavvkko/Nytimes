package com.hordiienko.nytimes.favorite_list;

import com.hordiienko.nytimes.model.Article;

import java.util.List;

public interface FavoriteListContract {
    interface View {
        void showProgress();
        void hideProgress();
        void setData(List<Article> articles);
        void removeItem(Article article);
        void showFailure(Throwable t);
    }

    interface Presenter {
        void onViewReady();
        void onClickRemoveFavorite(Article article);
        void onViewDestroy();
    }
}
