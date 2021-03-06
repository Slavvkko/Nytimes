package com.hordiienko.nytimes.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hordiienko.nytimes.ArticleActivity;
import com.hordiienko.nytimes.R;
import com.hordiienko.nytimes.model.Article;
import com.hordiienko.nytimes.utils.GlideApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_PROGRESS = 0;

    private Context context;
    private List<Article> articleList;
    private ArticleListener articleListener;

    public interface ArticleListener {
        void onClickFavorite(Article article, ImageView thumb);
    }

    public ArticleAdapter(Context context, ArticleListener articleListener) {
        this.context = context;
        this.articleListener = articleListener;

        articleList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return new ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.view_article, viewGroup, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(context).inflate(R.layout.view_progress, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ArticleViewHolder) {
            Article article = articleList.get(position);
            ArticleViewHolder articleHolder = (ArticleViewHolder) viewHolder;

            articleHolder.title.setText(article.getTitle());
//            articleHolder.date.setText(article.getDate());
            articleHolder.description.setText(article.getDescription());

            if (article.isFavorite()) {
                articleHolder.favoriteButton.setImageResource(R.drawable.ic_star_yellow_24dp);
            } else {
                articleHolder.favoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
            }

            String image = article.getImage();

            if (image != null && image.length() > 0) {
                if (image.charAt(0) == '/') {
                    GlideApp.with(context)
                            .load(new File(image))
                            .placeholder(R.drawable.placeholder)
//                            .centerCrop()
//                            .dontAnimate()
//                            .dontTransform()
                            .into(articleHolder.thumb);
                } else {
                    GlideApp.with(context)
                            .load(image)
                            .placeholder(R.drawable.placeholder)
//                            .centerCrop()
//                            .dontAnimate()
//                            .dontTransform()
                            .into(articleHolder.thumb);
                }
            } else {
                articleHolder.thumb.setImageResource(R.drawable.placeholder);
            }

            articleHolder.thumb.setTransitionName(article.getUrl() + "_thumb");
            articleHolder.title.setTransitionName(article.getUrl() + "_title");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return articleList.get(position) == null ? VIEW_TYPE_PROGRESS : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void setData(List<Article> articles) {
        articleList.clear();
        appendData(articles);
    }

    public void appendData(List<Article> articles) {
        articleList.addAll(articles);
        notifyDataSetChanged();
    }

    public void updateArticle(Article article) {
        notifyItemChanged(articleList.indexOf(article));
    }

    public void removeArticle(Article article) {
        int position = articleList.indexOf(article);

        articleList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateContext(Context context) {
        this.context = context;
    }

    public void addLoadingFooter() {
        articleList.add(null);
        notifyItemInserted(articleList.size() - 1);
    }

    public void removeLoadingFooter() {
        int position = articleList.size() - 1;

        articleList.remove(position);
        notifyItemRemoved(position);
    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.thumb)
        ImageView thumb;

        @BindView(R.id.favoriteButton)
        ImageView favoriteButton;

        @BindView(R.id.title)
        TextView title;

//        @BindView(R.id.date)
//        TextView date;

        @BindView(R.id.description)
        TextView description;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @OnClick(R.id.favoriteButton)
        void favoriteOnClick() {
            articleListener.onClickFavorite(articleList.get(getAdapterPosition()), thumb);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra(ArticleActivity.EXTRA_ITEM, articleList.get(getAdapterPosition()));

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    (Activity) context,
                    new Pair<>(thumb, thumb.getTransitionName()),
                    new Pair<>(title, title.getTransitionName())
            );

            context.startActivity(intent, options.toBundle());
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
