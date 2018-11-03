package com.hordiienko.nytimes.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.Objects;

public class Article implements Parcelable {
    private boolean isFavorite;

    private String url;
    private String title;
    private String description;
    private String date;

    @Nullable
    private String image;

    public Article(String url, String title, String description, String date, @Nullable String image) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = image;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "date=" + date +
                ", title=" + title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(url, article.url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(isFavorite ? 1 : 0);
        out.writeString(url);
        out.writeString(title);
        out.writeString(description);
        out.writeString(date);
        out.writeString(image);
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    private Article(Parcel in) {
        isFavorite = in.readInt() == 1;
        url = in.readString();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        image = in.readString();
    }
}
