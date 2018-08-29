package com.nerdgeeks.moviepad.ui;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.data.sqlite.domain.Watch;
import com.nerdgeeks.moviepad.utils.IdGenerator;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMovieFragment extends DialogFragment {

    private EditText movieName;
    private Spinner movieGenre;
    private EditText movieYear;

    public AddMovieFragment() {
        // Required empty public constructor
    }

    public static AddMovieFragment newInstance(){
        AddMovieFragment fragment = new AddMovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_movie, null);
        movieName = rootView.findViewById(R.id.edit_text_list_name);
        movieGenre = rootView.findViewById(R.id.mSpinner);
        movieYear = rootView.findViewById(R.id.edit_text_list_year);

        movieName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction()
                        == KeyEvent.ACTION_DOWN){

                }
                return true;
            }
        });

        builder.setView(rootView)
                .setPositiveButton(R.string.positive_button_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (movieGenre.getSelectedItem() != null
                                && !movieName.getText().toString().isEmpty()
                                && !movieYear.getText().toString().isEmpty()){
                            Watch watch = new Watch();
                            watch.setName(movieName.getText().toString());
                            watch.setId(IdGenerator.generateUniqueId());
                            watch.setYear(movieYear.getText().toString());
                            watch.setGenre(String.valueOf(movieGenre.getSelectedItem()));
                            watch.setWatched(false);
                            watch.setImgUrl(getDefaultImage());
                            AddNewMovie activity = (AddNewMovie) getActivity();
                            activity.addNewTask(watch);
                        } else {
                            if (movieName.getText().toString().isEmpty()){
                                movieName.setError("Empty! Please type name of the movie");
                            } else if (movieYear.getText().toString().isEmpty()){
                                movieYear.setError("Empty! Please type year of the movie");
                            } else {
                                Toast.makeText(getActivity(), "Please select movie genre", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        return builder.create();
    }

    public interface AddNewMovie{
        void addNewTask(Watch watch);
    }

    private String getDefaultImage(){
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.ic_movie_dark);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 60, stream);
        byte[] bitMapData = stream.toByteArray();

        return Base64.encodeToString(bitMapData, Base64.DEFAULT);
    }
}
