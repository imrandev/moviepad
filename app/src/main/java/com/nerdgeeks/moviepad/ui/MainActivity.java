package com.nerdgeeks.moviepad.ui;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nerdgeeks.moviepad.BuildConfig;
import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.app.Moviepad;
import com.nerdgeeks.moviepad.constant.Config;
import com.nerdgeeks.moviepad.data.sqlite.data.DataManager;
import com.nerdgeeks.moviepad.data.sqlite.domain.Watch;
import com.nerdgeeks.moviepad.ui.adapter.MovieAdapter;
import com.nerdgeeks.moviepad.data.sqlite.DbHelper;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        ListView.OnItemClickListener, AddMovieFragment.AddNewMovie, AbsListView.MultiChoiceModeListener {

    private DbHelper dbHelper;
    private ArrayAdapter<Watch> adapter;
    private ListView movieList;
    private boolean isChecked = false;
    private ArrayList<Watch> toDelete;
    private DataManager manager;
    private TextView taskSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = ((Moviepad) getApplicationContext()).getManager();
        // Handle Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        dbHelper = new DbHelper(this);
        movieList = findViewById(R.id.listMovie);
        taskSign = findViewById(R.id.task_sign);

        movieList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        movieList.setMultiChoiceModeListener(this);
        movieList.setOnItemClickListener(this);

        toDelete = new ArrayList<>();
        loadMovieList();
    }

    private void loadMovieList() {
        if (manager.isTableExists(Config.DB_TABLE)){
            ArrayList<Watch> watches = manager.onRetrieveMovieList();
            adapter = new MovieAdapter(this, watches);
            if (adapter.getCount() > 0){
                movieList.setAdapter(adapter);
                taskSign.setVisibility(View.GONE);
            } else {
                movieList.setAdapter(adapter);
                taskSign.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        } else {
            taskSign.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_movie:
                DialogFragment dialog = AddMovieFragment.newInstance();
                dialog.show(MainActivity.this.getFragmentManager(), "AddMovieFragment");
                return true;
            case R.id.action_browse_movie:
                startActivity(new Intent(this, BrowseActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
                return true;
            case R.id.action_rate:
                rateMyApp();
                return true;
            case R.id.action_about:
                aboutMyApp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setState(View view) {
        View rootView = (View) view.getParent();
        TextView item = (TextView) rootView.findViewById(R.id.movie_title);
        String title = item.getText().toString();
        if (!isChecked){
            dbHelper.updateCheckedRow(title, "false");
            isChecked = true;
            loadMovieList();
        }else {
            dbHelper.updateCheckedRow(title, "true");
            isChecked = false;
            loadMovieList();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String title = parent.getItemAtPosition(position).toString();
        if (!isChecked){
            dbHelper.updateCheckedRow(title, "false");
            isChecked = true;
            loadMovieList();
        }else {
            dbHelper.updateCheckedRow(title, "true");
            isChecked = false;
            loadMovieList();
        }
    }

    @Override
    public void addNewTask(Watch watch) {
        manager.insertNewMovie(watch);
        loadMovieList();
    }

    private void deleteMovieData(Watch watch){
        manager.onDeleteMovie(watch);
        loadMovieList();
    }

    private void deleteMovie(Watch watch, String column){
        manager.delete(watch, column);
    }

    @Override
    public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            toDelete.add(adapter.getItem(position));
        } else {
            toDelete.remove(adapter.getItem(position));
        }
    }

    @Override
    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.row_selection, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                for (Watch watch : toDelete) {
                    deleteMovieData(watch);
                }
                // Action picked, so close the CAB
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    private void aboutMyApp() {

        MaterialDialog.Builder bulder = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .customView(R.layout.about, true)
                .backgroundColor(getResources().getColor(R.color.colorPrimary))
                .titleColorRes(android.R.color.white)
                .positiveText("MORE APPS")
                .icon(getResources().getDrawable(R.mipmap.ic_launcher))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Uri uri = Uri.parse("market://search?q=pub:" + "NerdGeeks");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/search?q=pub:" + "NerdGeeks")));
                        }
                    }
                });

        MaterialDialog materialDialog = bulder.build();

        TextView versionCode = (TextView) materialDialog.findViewById(R.id.version_code);
        TextView versionName = (TextView) materialDialog.findViewById(R.id.version_name);
        versionCode.setText(String.valueOf("Version code : " + BuildConfig.VERSION_CODE));
        versionName.setText("Version name : " + BuildConfig.VERSION_NAME);

        materialDialog.show();
    }

    private void rateMyApp() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode mode) {
        toDelete.clear();
    }
}
