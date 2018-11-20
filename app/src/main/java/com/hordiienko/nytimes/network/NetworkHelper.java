package com.hordiienko.nytimes.network;

import com.hordiienko.nytimes.Constants;
import com.hordiienko.nytimes.model.Article;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;

public class NetworkHelper {
    private static NetworkHelper instance;

    public static NetworkHelper getInstance() {
        if (instance == null) {
            instance = new NetworkHelper();
        }

        return instance;
    }

    private NytimesInterface nytimesInterface;

    private NetworkHelper() {
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
                });
    }
}
