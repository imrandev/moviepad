package com.nerdgeeks.moviepad.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.app.Moviepad;
import com.nerdgeeks.moviepad.constant.Config;
import com.nerdgeeks.moviepad.controllers.DetailsController;
import com.nerdgeeks.moviepad.controllers.impl.DetailsControllerImpl;
import com.nerdgeeks.moviepad.data.domain.yts.Movie;
import com.nerdgeeks.moviepad.data.sqlite.DbHelper;
import com.nerdgeeks.moviepad.data.sqlite.data.DataManager;
import com.nerdgeeks.moviepad.data.sqlite.domain.Watch;
import com.nerdgeeks.moviepad.ui.adapter.CastAdapter;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class DetailsActivity extends AppCompatActivity implements DetailsController {

    private CoordinatorLayout parentView;

    private TextView movieTitle, movieYear, movieRatings, movieGenre,
            movieLang, movieMpa, movieTime, movieDesc;

    private ImageView movieCover;
    private RecyclerView castList;
    private DataManager dataManager;
    private Watch watch;
    private String videoId;
    private LinearLayout loadingBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_details);


        loadingBoard = findViewById(R.id.loading_board);

        watch = new Watch();
        dataManager = ((Moviepad) getApplicationContext()).getManager();

        String id = getIntent().getStringExtra("id");

        movieTitle = findViewById(R.id.movie_title);
        movieYear = findViewById(R.id.movie_year);
        movieRatings = findViewById(R.id.movie_ratings);
        movieGenre = findViewById(R.id.movie_genre);
        movieLang = findViewById(R.id.movie_lang);
        movieMpa = findViewById(R.id.movie_mpa);
        movieTime = findViewById(R.id.movie_time);
        movieDesc = findViewById(R.id.movie_desc);

        movieCover = findViewById(R.id.movie_cover);
        castList = findViewById(R.id.castList);

        //init ui
        parentView = findViewById(R.id.details_activity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        castList.setLayoutManager(layoutManager);
        castList.setNestedScrollingEnabled(false);

        // set listener for movie details
        DetailsControllerImpl.init().initMovies(this,
                Config.YTS_LIST_MOVIE_DETAILS, getMapOptions(id));
    }

    private void initView(Movie movies) {
        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(movies.getTitle());

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        videoId = movies.getYtTrailerCode();

        ImageView cover = findViewById(R.id.expandedImage);
        Picasso.with(this)
                .load(movies.getBackgroundImageOriginal())
                .into(cover);

        Picasso.with(this)
                .load(movies.getMediumCoverImage())
                .into(movieCover);

        movieTitle.setText(movies.getTitleEnglish());
        String ratings = "" + movies.getRating();
        movieRatings.setText(ratings);
        String year = "" + movies.getYear();
        movieYear.setText(year);

        int item = 0;
        StringBuilder genreBuilder = new StringBuilder();
        List<String> genreList = movies.getGenres();
        if (genreList != null){
            for (String genre : genreList){
                if (item < movies.getGenres().size()-1){
                    genreBuilder.append(genre + " • ");
                    item++;
                } else {
                    genreBuilder.append(genre);
                    item = 0;
                }
            }
            movieGenre.setText(genreBuilder);
        }

        movieLang.setText(movies.getLanguage());
        if (!movies.getMpaRating().isEmpty()){
            movieMpa.setText(movies.getMpaRating());
            movieMpa.setVisibility(View.VISIBLE);

        } else {
            movieMpa.setVisibility(View.GONE);
        }
        String time = "Runtime\n" + movies.getRuntime() + " min";
        movieTime.setText(time);

        movieDesc.setText(movies.getDescriptionFull());

        if (movies.getCast() != null){
            CastAdapter castAdapter = new CastAdapter(movies.getCast(), this);
            castList.setAdapter(castAdapter);
        }

        watch.setId(movies.getId());
        watch.setName(movies.getTitleEnglish());
        watch.setTime(time.replace("Runtime", ""));
        watch.setImgUrl(getEncodedImage(movieCover));
        watch.setYear(year);
        watch.setGenre(genreBuilder.toString());
        watch.setRating(ratings);
        watch.setWatched(false);

        loadingBoard.setVisibility(View.GONE);
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

    @Override
    public void onResult(Movie movies) {
        if (movies != null){
            initView(movies);
        }
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
    }

    private Map<String, String> getMapOptions(String id){
        Map<String, String> options = new HashMap<>();
        options.put("with_cast", "true");
        options.put("with_images", "true");
        options.put("movie_id", "" + id);
        return options;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_movie:
                if (!dataManager.isDataExists(watch.getId())){
                    dataManager.insertNewMovie(watch);
                    Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Already Added", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getEncodedImage(ImageView holder){
        BitmapDrawable drawable = (BitmapDrawable) holder.getDrawable();

        if (drawable != null){
            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 60, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            return Base64.encodeToString(b, Base64.DEFAULT);
        } else
            return "";
    }

    public void onYouTubeFragment(View view) {
        if (!videoId.isEmpty()){
            Intent intent = new Intent(this, YoutubePlayerFragment.class);
            intent.putExtra("VIDEO_ID", videoId);
            startActivity(intent);
        }
    }
}
