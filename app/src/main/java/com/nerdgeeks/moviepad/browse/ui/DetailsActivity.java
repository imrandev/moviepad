package com.nerdgeeks.moviepad.browse.ui;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.browse.client.ApiInterfaceImpl;
import com.nerdgeeks.moviepad.browse.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class DetailsActivity extends AppCompatActivity implements ApiInterfaceImpl.SingleMovieDetails {

    private CoordinatorLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_details);

        String id = getIntent().getStringExtra("id");

        //init ui
        parentView = (CoordinatorLayout) findViewById(R.id.details_activity);

        // set listener for movie details
        ApiInterfaceImpl apiInterface = new ApiInterfaceImpl();
        apiInterface.setSingleMovieDetailsListener(this, this);
        apiInterface.getMovieDetails(id);
    }

    @Override
    public void getDetails(Movie movies) {
        initView(movies);
    }

    private void initView(Movie movies) {
        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(movies.getTitle());

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView cover = (ImageView) findViewById(R.id.expandedImage);
        Picasso.with(this)
                .load(movies.getBackgroundImageOriginal())
                .into(cover);
    }

    @Override
    public void onFailureSnackBar(String message) {
        int color = Color.RED;
        int TIME_OUT = Snackbar.LENGTH_INDEFINITE;

        Snackbar snackbar = Snackbar
                .make(parentView, message, TIME_OUT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackbar.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }
}
