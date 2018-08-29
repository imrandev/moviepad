package com.nerdgeeks.moviepad.service;

import com.nerdgeeks.moviepad.controllers.DetailsController;

import java.util.Map;

public interface DetailsService {
    void init(DetailsController controller, String url, Map<String, String> options);
}
