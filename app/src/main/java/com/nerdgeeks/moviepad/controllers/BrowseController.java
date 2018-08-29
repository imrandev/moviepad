package com.nerdgeeks.moviepad.controllers;

import com.nerdgeeks.moviepad.data.domain.yts.Movies;

import java.util.List;

public interface BrowseController {
    void onResult(List<Movies> movies);
    void onError(String message);
}
