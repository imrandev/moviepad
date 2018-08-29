package com.nerdgeeks.moviepad.data.Repository;

import android.support.annotation.NonNull;

import com.nerdgeeks.moviepad.constant.Config;
import com.nerdgeeks.moviepad.controllers.BrowseController;
import com.nerdgeeks.moviepad.controllers.api.retrofit.RetrofitClient;
import com.nerdgeeks.moviepad.data.domain.yts.Browse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowserRepositoryImpl implements BrowserRepository, Callback<Browse> {

    private BrowseController controller;
    @Override
    public void onResponse(@NonNull Call<Browse> call, @NonNull Response<Browse> response) {
        Browse browse = response.body();

        if (browse != null){
            controller.onResult(browse.getData().getMovies());
        }
    }

    @Override
    public void onFailure(@NonNull Call<Browse> call, @NonNull Throwable t) {
        controller.onError(t.getMessage());
    }

    @Override
    public void init(BrowseController controller, String url, Map<String, String> options) {
        this.controller = controller;
        RetrofitClient.getInstance(Config.YTS_BASE_URL).getMovies(url, options).enqueue(this);
    }
}
