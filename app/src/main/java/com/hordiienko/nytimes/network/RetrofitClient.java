package com.hordiienko.nytimes.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hordiienko.nytimes.Constants;
import com.hordiienko.nytimes.model.Article;
import com.hordiienko.nytimes.model.ArticleDeserializer;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = buildRetrofit();
        }

        return retrofit;
    }

    private static Retrofit buildRetrofit() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Article.class, new ArticleDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
