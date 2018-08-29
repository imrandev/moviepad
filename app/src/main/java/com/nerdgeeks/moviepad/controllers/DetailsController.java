package com.nerdgeeks.moviepad.controllers;

import com.nerdgeeks.moviepad.data.domain.yts.Movie;

public interface DetailsController {
    void onResult(Movie movies);
    void onError(String message);
}
