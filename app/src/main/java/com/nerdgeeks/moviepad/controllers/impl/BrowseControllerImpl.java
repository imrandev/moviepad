package com.nerdgeeks.moviepad.controllers.impl;

import com.nerdgeeks.moviepad.controllers.BrowseController;
import com.nerdgeeks.moviepad.service.BrowserService;
import com.nerdgeeks.moviepad.service.BrowserServiceImpl;

import java.util.Map;

public class BrowseControllerImpl {

    private BrowserService service = new BrowserServiceImpl();

    public static BrowseControllerImpl init(){
        return new BrowseControllerImpl();
    }

    public void initMovies(BrowseController controller, String url, Map<String, String> options){
        service.init(controller, url, options);
    }
}
