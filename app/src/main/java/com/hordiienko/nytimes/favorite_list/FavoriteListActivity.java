package com.hordiienko.nytimes.favorite_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hordiienko.nytimes.R;
import com.hordiienko.nytimes.adapter.ArticleAdapter;
import com.hordiienko.nytimes.model.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteListActivity extends AppCompatActivity implements FavoriteListContract.View, ArticleAdapter.ArticleListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressLoading)
    ProgressBar progressLoading;

    @BindView(R.id.recyclerArticles)
    RecyclerView recyclerView;

    private ArticleAdapter articleAdapter;
    private FavoriteListContract.Presenter favoriteListPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        articleAdapter = new ArticleAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(articleAdapter);

        favoriteListPresenter = new FavoriteListPresenter(this, this);

        favoriteListPresenter.onViewReady();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteListPresenter.onViewDestroy();
    }

    @Override
    public void showProgress() {
        progressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressLoading.setVisibility(View.GONE);
    }

    @Override
    public void setData(List<Article> articles) {
        articleAdapter.setData(articles);
    }

    @Override
    public void removeItem(Article article) {
        articleAdapter.removeArticle(article);
    }

    @Override
    public void showFailure(Throwable t) {
        Toast.makeText(this, getString(R.string.communication_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickFavorite(Article article, ImageView thumb) {
        favoriteListPresenter.onClickRemoveFavorite(article);
    }
}
