package com.hordiienko.nytimes.model;

import com.google.gson.annotations.SerializedName;

public class ArticleResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("results")
    private Article[] articles;

    public String getStatus() {
        return status;
    }

    public Article[] getArticles() {
        return articles;
    }
}
