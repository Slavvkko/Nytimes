package com.hordiienko.nytimes.ui.article_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hordiienko.nytimes.Constants;
import com.hordiienko.nytimes.R;
import com.hordiienko.nytimes.ui.adapter.ArticleAdapter;
import com.hordiienko.nytimes.model.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ArticleListFragment extends Fragment implements ArticleListContract.View, ArticleAdapter.ArticleListener {
    public static final String ARG_TYPE = "ARG_TYPE";

    private String apiType;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progressLoading)
    ProgressBar progressLoading;

    @BindView(R.id.recyclerArticles)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private boolean viewCreated;
    private ArticleAdapter articleAdapter;
    private ArticleListContract.Presenter articleListPresenter;

    public static ArticleListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);

        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (getArguments() != null) {
            apiType = getArguments().getString(ARG_TYPE);
        } else {
            apiType = Constants.ApiType.values()[0].getName();
        }

        articleAdapter = new ArticleAdapter(getContext(), this);

        articleListPresenter = new ArticleListPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        unbinder = ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(() -> articleListPresenter.onViewRefresh());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(articleAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            public void onScrollEnd() {
                articleListPresenter.onScrollEnd();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!viewCreated) {
            articleListPresenter.onViewReady();
            viewCreated = true;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        articleAdapter.updateContext(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }

        articleListPresenter.onViewDestroy();
    }

    @Override
    public void onClickFavorite(Article article, ImageView thumb) {
        articleListPresenter.onClickFavorite(article, thumb);
    }

    @Override
    public void updateItem(Article article) {
        articleAdapter.updateArticle(article);
    }

    @Override
    public String getApiType() {
        return apiType;
    }

    @Override
    public void setData(List<Article> articles) {
        swipeRefreshLayout.setRefreshing(false);
        articleAdapter.setData(articles);
    }

    @Override
    public void appendData(List<Article> articles) {
        articleAdapter.appendData(articles);
    }

    @Override
    public void showFailure(Throwable t) {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), getString(R.string.communication_error), Toast.LENGTH_LONG).show();
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
    public void showFooterProgress() {
        // you cannot call this method from RecyclerView.OnScrollListener.onScrolled
        recyclerView.post(() -> articleAdapter.addLoadingFooter());
    }

    @Override
    public void hideFooterProgress() {
        articleAdapter.removeLoadingFooter();
    }
}
