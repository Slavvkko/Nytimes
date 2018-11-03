package com.hordiienko.nytimes;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hordiienko.nytimes.model.Article;
import com.hordiienko.nytimes.utils.GlideApp;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM = "EXTRA_ITEM";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.thumb)
    ImageView thumb;

//    @BindView(R.id.favoriteButton)
//    ImageView favoriteButton;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.description)
    TextView description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Article article = Objects.requireNonNull(getIntent().getExtras()).getParcelable(EXTRA_ITEM);
        String url = Objects.requireNonNull(article).getUrl();

        thumb.setTransitionName(url + "_thumb");
        title.setTransitionName(url + "_title");

//        if (article.isFavorite()) {
//            favoriteButton.setImageResource(R.drawable.ic_star_yellow_24dp);
//        } else {
//            favoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
//        }

        title.setText(article.getTitle());
        date.setText(article.getDate());
        description.setText(article.getDescription());

        String image = article.getImage();

        if (image != null && image.length() > 0) {
            // pause transition
            supportPostponeEnterTransition();

            if (image.charAt(0) == '/') {
                GlideApp.with(this)
                        .load(new File(image))
                        .placeholder(R.drawable.placeholder)
//                        .centerCrop()
//                        .dontAnimate()
                        .dontTransform()
                        .listener(glideListener)
                        .into(thumb);
            } else {
                GlideApp.with(this)
                        .load(image)
                        .placeholder(R.drawable.placeholder)
//                        .centerCrop()
//                        .dontAnimate()
                        .dontTransform()
                        .listener(glideListener)
                        .into(thumb);
            }
        } else {
            thumb.setImageResource(R.drawable.placeholder);
        }
    }

    private RequestListener<Drawable> glideListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            // resume transition
            supportStartPostponedEnterTransition();
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            // resume transition
            supportStartPostponedEnterTransition();
            return false;
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
