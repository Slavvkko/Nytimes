package com.hordiienko.nytimes.network;

import com.hordiienko.nytimes.model.ArticleResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NytimesInterface {
    @GET("{apiType}/{section}/{period}.json?api-key=bfb23aba37184350acad640edd28839b")
    Single<ArticleResponse> listArticles(
            @Path("apiType") String apiType,
            @Path("section") String section,
            @Path("period") int period,
            @Query("offset") int offset);

//    @GET("{apiType}/{section}/{period}.json?api-key=bfb23aba37184350acad640edd28839b")
//    Call<ArticleResponse> listArticlesTest(
//            @Path("apiType") String apiType,
//            @Path("section") String section,
//            @Path("period") int period,
//            @Query("offset") int offset);
}
