package com.nerdgeeks.moviepad.service;

import com.nerdgeeks.moviepad.controllers.DetailsController;
import com.nerdgeeks.moviepad.data.Repository.DetailsRepository;
import com.nerdgeeks.moviepad.data.Repository.DetailsRepositoryImpl;

import java.util.Map;

public class DetailsServiceImpl implements DetailsService {

    private DetailsRepository repository = new DetailsRepositoryImpl();

    @Override
    public void init(DetailsController controller, String url, Map<String, String> options) {
        repository.init(controller, url, options);
    }
}
