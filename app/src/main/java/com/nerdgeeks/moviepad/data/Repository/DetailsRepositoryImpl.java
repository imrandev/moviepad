package com.nerdgeeks.moviepad.data.Repository;

import android.support.annotation.NonNull;

import com.nerdgeeks.moviepad.constant.Config;
import com.nerdgeeks.moviepad.controllers.DetailsController;
import com.nerdgeeks.moviepad.controllers.api.retrofit.RetrofitClient;
import com.nerdgeeks.moviepad.data.domain.yts.Browse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsRepositoryImpl implements DetailsRepository, Callback<Browse> {

    private DetailsController controller;
    @Override
    public void init(DetailsController controller, String url, Map<String, String> options) {
        this.controller = controller;
        RetrofitClient.getInstance(Config.YTS_LIST_MOVIE_DETAILS).getMovies(url, options).enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<Browse> call, @NonNull Response<Browse> response) {
        Browse browse = response.body();
        if (browse != null){
            controller.onResult(browse.getData().getMovie());
        }
    }

    @Override
    public void onFailure(@NonNull Call<Browse> call, @NonNull Throwable t) {
        controller.onError(t.getMessage());
    }
}
