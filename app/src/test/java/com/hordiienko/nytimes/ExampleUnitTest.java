package com.hordiienko.nytimes;

import com.hordiienko.nytimes.model.ArticleResponse;
import com.hordiienko.nytimes.network.NytimesInterface;
import com.hordiienko.nytimes.network.RetrofitClient;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws IOException {
        assertEquals(4, 2 + 2);

        Retrofit retrofit = RetrofitClient.getRetrofit();

        NytimesInterface nytimesInterface = retrofit.create(NytimesInterface.class);

        Call<ArticleResponse> call = nytimesInterface.listArticlesTest(
                Constants.ApiType.MOSTSHARED.getName(),
                Constants.SECTION,
                Constants.PERIOD,
                0);

        Response<ArticleResponse> response = call.execute();

        assertEquals(response.code(), 200);

        ArticleResponse article = response.body();

        assertEquals(article.getStatus(), "OK");
        assertNotEquals(article.getArticles().length, 0);
        assertNotNull(article.getArticles()[0].getImage());

        System.out.println(article.getArticles()[0]);
    }
}