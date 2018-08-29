package com.nerdgeeks.moviepad.data.Repository;

import com.nerdgeeks.moviepad.controllers.BrowseController;

import java.util.Map;

public interface BrowserRepository {
    void init(BrowseController controller, String url,  Map<String, String> options);
}
