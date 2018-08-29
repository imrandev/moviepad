package com.nerdgeeks.moviepad.controllers.api.retrofit;


import com.nerdgeeks.moviepad.data.domain.yts.Browse;
import com.nerdgeeks.moviepad.data.domain.yts.Movie;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RetrofitAPI {
    // @GET("/api/v2/list_movies.json")
    @GET
    Call<Browse> getMovies(@Url String url, @QueryMap Map<String, String> options);

    // @GET("/api/v2/movie_details.json")
    // Call<Browse> getMovieDetails(@QueryMap Map<String, String> options);

    @GET
    Call<Movie> getDetails(@Url String url, @QueryMap Map<String, String> options);
}
