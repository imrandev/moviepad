package com.nerdgeeks.moviepad.service;

import com.nerdgeeks.moviepad.controllers.BrowseController;

import java.util.Map;

public interface BrowserService {
    void init(BrowseController controller, String url, Map<String, String> options);
}
