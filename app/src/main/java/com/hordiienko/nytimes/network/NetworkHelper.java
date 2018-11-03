package com.hordiienko.nytimes.network;

import com.hordiienko.nytimes.Constants;
import com.hordiienko.nytimes.model.Article;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;

public class NetworkHelper {
    private NytimesInterface nytimesInterface;

    public NetworkHelper() {
        nytimesInterface = RetrofitClient.getRetrofit().create(NytimesInterface.class);
    }

    public Single<List<Article>> getArticles(String type, int offset) {
        return nytimesInterface.listArticles(type, Constants.SECTION, Constants.PERIOD, offset)
                .map(articleResponse -> {
                    if (!articleResponse.getStatus().equals("OK")) {
                        throw Exceptions.propagate(new Exception(articleResponse.getStatus()));
                    } else {
                        return Arrays.asList(articleResponse.getArticles());
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
