package com.nerdgeeks.moviepad.data.Repository;

import com.nerdgeeks.moviepad.controllers.DetailsController;

import java.util.Map;

public interface DetailsRepository {
    void init(DetailsController controller, String url, Map<String, String> options);
}
