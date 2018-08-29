package com.nerdgeeks.moviepad.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.constant.Config;
import com.nerdgeeks.moviepad.controllers.BrowseController;
import com.nerdgeeks.moviepad.controllers.impl.BrowseControllerImpl;
import com.nerdgeeks.moviepad.ui.adapter.BrowseAdapter;
import com.nerdgeeks.moviepad.data.domain.yts.Movies;
import com.nerdgeeks.moviepad.ui.adapter.GenreAdapter;
import com.nerdgeeks.moviepad.utils.CustomDividerItemDecoration;
import com.nerdgeeks.moviepad.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowseActivity extends AppCompatActivity implements BrowseController,
        SearchView.OnQueryTextListener{

    private RecyclerView browseListView, genreListView;
    private CoordinatorLayout parentView;
    private BrowseAdapter adapter;
    private List<Movies> allMovies = new ArrayList<>(2);
    private ProgressBar mLoader;
    private String genre;
    private SearchView searchView;
    private boolean isGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        // Handle Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Browse Movies");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        browseListView = findViewById(R.id.browseList);
        genreListView = findViewById(R.id.gList);
        parentView = findViewById(R.id.browseActivity);
        mLoader = findViewById(R.id.load_progress);

        LinearLayoutManager genreManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        genreListView.setLayoutManager(genreManager);
        genreListView.setNestedScrollingEnabled(true);

        GenreAdapter genreAdapter = new GenreAdapter(
                getResources().getStringArray(R.array.genre_array));
        genreAdapter.setGenreListener(onGenreListener);
        genreListView.setAdapter(genreAdapter);

        mLoader.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        browseListView.setLayoutManager(layoutManager);
        browseListView.setNestedScrollingEnabled(true);
        browseListView.setItemAnimator(new DefaultItemAnimator());
        browseListView.addItemDecoration(new CustomDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 0));

        BrowseControllerImpl.init().initMovies(this,
                Config.YTS_LIST_MOVIES, getMapOptions(1, 50, false));

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                mLoader.setVisibility(View.VISIBLE);
                int next_page = page + 1;
                BrowseControllerImpl.init().initMovies(BrowseActivity.this,
                        Config.YTS_LIST_MOVIES, getMapOptions(next_page, 20, false));

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeInserted(adapter.getItemCount(),allMovies.size()-1);
                    }
                });
            }
        };
        // Adds the scroll listener to RecyclerView
        browseListView.addOnScrollListener(scrollListener);
    }

    private GenreAdapter.GenreListener onGenreListener = new GenreAdapter.GenreListener() {
        @Override
        public void onClick(View v, String genre) {
            setListByGenre(genre);
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()){
            allMovies.clear();
            BrowseControllerImpl.init().initMovies(this,
                    Config.YTS_LIST_MOVIES, getMapOptions(1, 50, isGenre));
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setListByGenre(String genre) {
        this.genre = genre;
        allMovies.clear();
        mLoader.setVisibility(View.VISIBLE);
        BrowseControllerImpl.init().initMovies(this,
                Config.YTS_LIST_MOVIES, getMapOptions(1, 20, true));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (allMovies != null){
           if (allMovies.size() > 0){
               allMovies.clear();
           }
        }
        adapter.notifyDataSetChanged();
        BrowseControllerImpl.init().initMovies(BrowseActivity.this,
                Config.YTS_MOVIE_QUERY + "?query_term=" + query + "&limit=1", getMapOptions(1, 20, false));
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onResult(List<Movies> movies) {
        if (allMovies != null && allMovies.size() > 0){
            this.allMovies.addAll(movies);
            adapter.notifyDataSetChanged();
        } else {
            this.allMovies = movies;
            adapter = new BrowseAdapter(this, allMovies);
            browseListView.setAdapter(adapter);
        }
        mLoader.setVisibility(View.GONE);
    }

    @Override
    public void onError(String message) {
        int color = Color.RED;
        int TIME_OUT = Snackbar.LENGTH_INDEFINITE;

        Snackbar snackbar = Snackbar
                .make(parentView, message, TIME_OUT);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        snackbar.show();
        mLoader.setVisibility(View.INVISIBLE);
    }

    private Map<String, String> getMapOptions(int page, int limit, boolean isGenre){
        Map<String, String> options = new HashMap<>();
        if (isGenre){
            this.isGenre = true;
            options.put("genre", genre);
        } else {
            this.isGenre = false;
        }
        options.put("limit", "" + limit);
        options.put("sort_by", "year");
        options.put("page", "" + page);
        return options;
    }
}
