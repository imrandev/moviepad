package com.nerdgeeks.moviepad.service;

import com.nerdgeeks.moviepad.controllers.BrowseController;
import com.nerdgeeks.moviepad.data.Repository.BrowserRepository;
import com.nerdgeeks.moviepad.data.Repository.BrowserRepositoryImpl;

import java.util.Map;

public class BrowserServiceImpl implements BrowserService {

    private BrowserRepository repository = new BrowserRepositoryImpl();

    @Override
    public void init(BrowseController controller, String url, Map<String, String> options) {
        repository.init(controller, url, options);
    }
}
