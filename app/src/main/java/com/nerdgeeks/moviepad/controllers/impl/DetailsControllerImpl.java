package com.nerdgeeks.moviepad.controllers.impl;

import com.nerdgeeks.moviepad.controllers.DetailsController;
import com.nerdgeeks.moviepad.service.DetailsService;
import com.nerdgeeks.moviepad.service.DetailsServiceImpl;

import java.util.Map;

public class DetailsControllerImpl {
    private DetailsService service = new DetailsServiceImpl();

    public static DetailsControllerImpl init(){
        return new DetailsControllerImpl();
    }

    public void initMovies(DetailsController controller, String url, Map<String, String> options){
        service.init(controller, url, options);
    }
}
