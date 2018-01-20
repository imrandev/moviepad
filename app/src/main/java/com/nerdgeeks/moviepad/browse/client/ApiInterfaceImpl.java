package com.nerdgeeks.moviepad.browse.client;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nerdgeeks.moviepad.browse.config.Config;
import com.nerdgeeks.moviepad.browse.model.BrowseModel;
import com.nerdgeeks.moviepad.browse.model.Movie;
import com.nerdgeeks.moviepad.browse.model.Movies;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IMRAN on 9/14/2017.
 */

public class ApiInterfaceImpl {
    private Context context;
    private ApiInterfaceListener listener;
    private SingleMovieDetails details;

    public interface ApiInterfaceListener {
        void moviesReady(List<Movies> movies, int page);
        void onFailureSnackBar(String message);
    }

    public interface SingleMovieDetails {
        void getDetails(Movie movies);
        void onFailureSnackBar(String message);
    }

    public ApiInterfaceImpl(){

    }

    public ApiInterfaceImpl(Context context, ApiInterfaceListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setSingleMovieDetailsListener(Context context, SingleMovieDetails details){
        this.context = context;
        this.details = details;
    }

    public void getMovies(final int page){
        ApiClient
            .getClient()
            .getResults(Config.ADDITIONAL_URL + "" + page + "&limit=50")
            .enqueue(new Callback<BrowseModel>() {
                @Override
                public void onResponse(@NonNull Call<BrowseModel> call, @NonNull Response<BrowseModel> response) {
                    BrowseModel result = response.body();

                    if (result != null) {
                        assert listener != null;
                        listener.moviesReady(result.getData().getMovies(), page);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BrowseModel> call, @NonNull Throwable t) {
                    assert listener != null;
                    listener.onFailureSnackBar(t.getMessage());
                }
            });
    }

    public void getMovies(final int page, String genre){
        ApiClient
            .getClient()
            .getResults(Config.ADDITIONAL_URL + "" + page + "&genre=" + genre + "&limit=50")
            .enqueue(new Callback<BrowseModel>() {
                @Override
                public void onResponse(@NonNull Call<BrowseModel> call, @NonNull Response<BrowseModel> response) {
                    BrowseModel result = response.body();

                    if (result != null) {
                        assert listener != null;
                        listener.moviesReady(result.getData().getMovies(), page);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BrowseModel> call, @NonNull Throwable t) {
                    String exception = t.getMessage().replaceAll("\"yts.ag\"","");
                    assert listener != null;
                    listener.onFailureSnackBar(exception);
                }
            });
    }

    public void getMovieDetails(String id){
        ApiClient
                .getClient()
                .getResults(Config.MOVIE_DETAILS_URL + "" + id + "&with_images=true&with_cast=true")
                .enqueue(new Callback<BrowseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<BrowseModel> call, @NonNull Response<BrowseModel> response) {
                        BrowseModel result = response.body();

                        if (result != null) {
                            assert listener != null;
                            details.getDetails(result.getData().getMovie());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BrowseModel> call, @NonNull Throwable t) {
                        String exception = t.getMessage().replaceAll("\"yts.ag\"","");
                        assert listener != null;
                        details.onFailureSnackBar(exception);
                    }
                });
    }
}
