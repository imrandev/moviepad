package com.nerdgeeks.moviepad.browse.ui;

import android.Manifest;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.browse.adapter.BrowseAdapter;
import com.nerdgeeks.moviepad.browse.client.ApiInterfaceImpl;
import com.nerdgeeks.moviepad.browse.client.SearchApiInterface;
import com.nerdgeeks.moviepad.browse.model.Movies;
import com.nerdgeeks.moviepad.browse.utils.EndlessRecyclerViewScrollListener;
import com.nerdgeeks.moviepad.todolist.ui.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BrowseActivity extends AppCompatActivity implements
        ApiInterfaceImpl.ApiInterfaceListener, SearchView.OnQueryTextListener, SearchApiInterface.SearchResultListener {

    private static final int REQUEST_PERMISSION_SETTING = 100;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private RecyclerView browseListView;
    private CoordinatorLayout parentView;
    private BrowseAdapter adapter;
    private ApiInterfaceImpl apiInterface;
    private List<Movies> allMovies;
    private ProgressBar mLoader;
    private int FLAG = 0;
    private String genre;
    private SearchView searchView;

    private ProgressBar pb;
    private int downloadedSize = 0;
    private int totalSize = 0;
    private TextView cur_val;
    private float per;
    private String filename;
    private String URL, NAME, YEAR;

    private static final int REQUEST_WRITE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Browse Movies");
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        browseListView = (RecyclerView) findViewById(R.id.browseList);
        parentView = (CoordinatorLayout) findViewById(R.id.browseActivity);
        mLoader = (ProgressBar) findViewById(R.id.load_progress);

        mLoader.setVisibility(View.VISIBLE);

        apiInterface = new ApiInterfaceImpl(this, this);
        apiInterface.getMovies(1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        browseListView.setLayoutManager(layoutManager);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                mLoader.setVisibility(View.VISIBLE);
                int next_page = page + 1;
                if (FLAG==0){
                    apiInterface.getMovies(next_page);
                } else {
                    apiInterface.getMovies(next_page, genre);
                }

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

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
        return true;
    }

    @Override
    public void getResult(List<Movies> movies) {
        if (allMovies != null){
            allMovies.clear();
            this.allMovies = movies;
        }
        adapter = new BrowseAdapter(this, allMovies);
        adapter.setOnItemClickListener(recyclerRowClickListener);
        browseListView.setAdapter(adapter);
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
        mLoader.setVisibility(View.INVISIBLE);
    }

    @Override
    public void moviesReady(List<Movies> movies, int page) {
        if (page>1){
            this.allMovies.addAll(movies);
            adapter.notifyDataSetChanged();
        } else {
            if (allMovies != null){
                allMovies.clear();
            }
            this.allMovies = movies;
            adapter = new BrowseAdapter(this, allMovies);
            adapter.setOnItemClickListener(recyclerRowClickListener);
            browseListView.setAdapter(adapter);
        }
        mLoader.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()){
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
        switch (item.getItemId()){
            case R.id.action:
                FLAG =1;
                genre = "action";
                setListByGenre(genre);
                return true;

            case R.id.adventure:
                FLAG =1;
                genre = "adventure";
                setListByGenre(genre);
                return true;

            case R.id.animation:
                FLAG =1;
                genre = "animation";
                setListByGenre(genre);
                return true;

            case R.id.comedy:
                FLAG =1;
                genre = "comedy";
                setListByGenre(genre);
                return true;

            case R.id.drama:
                FLAG =1;
                genre = "drama";
                setListByGenre(genre);
                return true;

            case R.id.doc:
                FLAG =1;
                genre = "horror";
                setListByGenre(genre);
                return true;

            case R.id.mystery:
                FLAG =1;
                genre = "mystery";
                setListByGenre(genre);
                return true;

            case R.id.romance:
                FLAG =1;
                genre = "romance";
                setListByGenre(genre);
                return true;

            case R.id.crime:
                FLAG =1;
                genre = "sci-fi";
                setListByGenre(genre);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListByGenre(String genre) {
        mLoader.setVisibility(View.VISIBLE);
        apiInterface.getMovies(1,genre);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        SearchApiInterface searchApiInterface = new SearchApiInterface(this, this);
        searchApiInterface.getSearchResult(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private BrowseAdapter.OnItemClickListener recyclerRowClickListener = new BrowseAdapter.OnItemClickListener() {
        @Override
        public void onClick(View view, int position, final String url, final String name, final String year) {
            URL = url;
            NAME = name;
            YEAR = year;

            if (isStoragePermissionGranted()){
                showProgress(url, name);
                new Thread(new Runnable() {
                    public void run() {
                        downloadFile(url, name, year);
                    }
                }).start();
            } else {
                if (ContextCompat.checkSelfPermission(BrowseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //This is called if user has denied the permission before
                    //In this case I am just asking the permission again
                    ActivityCompat.requestPermissions(BrowseActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission
                    showProgress(URL, NAME);
                    downloadFile(URL, NAME, YEAR);

                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage.\nHence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void downloadFile(String path, String movieName, String year){
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            //connect
            urlConnection.connect();
            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            filename = movieName+"(" + year + ")[YTS.AG]" + ".torrent";
            File file = new File(SDCardRoot, filename);

            FileOutputStream fileOutput = new FileOutputStream(file);
            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();
            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        per = ((float)downloadedSize/totalSize) * 100;
                        cur_val.setText("Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + per + "%)" );
                    }
                });
            }

            //close the output stream when complete //
            fileOutput.close();
            per = 0;
            downloadedSize = 0;
            totalSize = 0;

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    private void showError(final String s) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(BrowseActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showProgress(String url, String movieName) {
        MaterialDialog.Builder builder  =new MaterialDialog.Builder(this)
                .title(movieName)
                .customView(R.layout.progress_dialog, true)
                .backgroundColor(getResources().getColor(R.color.colorPrimary))
                .titleColorRes(android.R.color.white)
                .positiveText("OPEN")
                .icon(getResources().getDrawable(R.mipmap.ic_launcher))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        openDownloadedFileFromStorage();
                    }
                });

        MaterialDialog dialog = builder.build();
        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file from ... " + url);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();

        pb = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(getResources().getDrawable(R.drawable.white_progress));
    }

    private void openDownloadedFileFromStorage() {
        if (filename!=null){
            File file = new File(Environment.getExternalStorageDirectory(),filename);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(this, "com.nerdgeeks.cinetodo", file);
                intent.setDataAndType(contentUri, "application/x-bittorrent");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/x-bittorrent");
            }

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e){
                Toast.makeText(this,"Sorry! please download a torrent client app", Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(this,"Sorry! Torrent file not downloaded", Toast.LENGTH_SHORT).show();
    }

    private  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
}
