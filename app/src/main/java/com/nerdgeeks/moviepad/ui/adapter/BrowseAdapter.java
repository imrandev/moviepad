package com.nerdgeeks.moviepad.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.app.Moviepad;
import com.nerdgeeks.moviepad.data.domain.yts.Movies;
import com.nerdgeeks.moviepad.data.sqlite.data.DataManager;
import com.nerdgeeks.moviepad.data.sqlite.domain.Watch;
import com.nerdgeeks.moviepad.ui.DetailsActivity;
import com.nerdgeeks.moviepad.ui.YoutubePlayerFragment;
import com.nerdgeeks.moviepad.data.sqlite.DbHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by IMRAN on 9/14/2017.
 */

public class BrowseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<Movies> models;
    private OnItemClickListener onItemClickListener;

    public BrowseAdapter(Activity activity, List<Movies> models) {
        this.activity = activity;
        this.models = models;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_row,parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        ViewHolder myHolder = (ViewHolder) holder;

        if (models != null){
            Picasso.with(activity)
                    .load(models.get(position).getMediumCoverImage())
                    .into(myHolder.thumbnail);

            int item = 0;
            StringBuilder movieGenre = new StringBuilder();
            List<String> genreList = models.get(position).getGenres();
            if (genreList != null){
                for (String genre : genreList){
                    if (item < models.get(position).getGenres().size()-1){
                        movieGenre.append(genre + " • ");
                        item++;
                    } else {
                        movieGenre.append(genre);
                        item = 0;
                    }
                }
                myHolder.genre.setText(movieGenre);
            }

            String title = models.get(position).getTitle();
            String ratings = String.valueOf(models.get(position).getRating());
            String year = String.valueOf(models.get(position).getYear());
            String time = models.get(position).getRuntime().toString() + " min";

            myHolder.rating.setText(ratings);
            myHolder.name.setText(title);
            myHolder.year.setText(year);
            myHolder.time.setText(time);

            final Watch watch = new Watch();
            watch.setId(models.get(position).getId());
            watch.setName(title);
            watch.setWatched(false);
            watch.setRating(ratings);
            watch.setGenre(movieGenre.toString());
            watch.setImgUrl(getEncodedImage(holder));
            watch.setYear(year);
            watch.setTime(time);

            myHolder.addTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataManager manager = ((Moviepad) activity.getApplicationContext()).getManager();
                    if (!manager.isDataExists(watch.getId())){
                        manager.insertNewMovie(watch);
                        Toast.makeText(activity, "Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Already Added", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            myHolder.playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videoId = models.get(holder.getAdapterPosition()).getYtTrailerCode();
                    if (!videoId.isEmpty()){
                        Intent intent = new Intent(activity, YoutubePlayerFragment.class);
                        intent.putExtra("VIDEO_ID", videoId);
                        activity.startActivity(intent);
                    }
                }
            });

            myHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, DetailsActivity.class);
                    intent.putExtra("id", String.valueOf(watch.getId()));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            });
        }
    }


    private String getEncodedImage(RecyclerView.ViewHolder holder){
        ViewHolder myHolder = (ViewHolder) holder;
        BitmapDrawable drawable = (BitmapDrawable) myHolder.thumbnail.getDrawable();

        if (drawable != null){
            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 60, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            return Base64.encodeToString(b, Base64.DEFAULT);
        } else
            return "";
    }

    @Override
    public int getItemCount() {
        if (models != null){
            return models.size();
        } else
            return 0;
    }

    // Add a new item to the RecyclerView on a predefined position
    public void addNewItems(int position, Movies data) {
        models.add(position, data);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail, playBtn, downloadBtn;
        private TextView name, genre, rating, year, time;
        private LinearLayout cardView;
        private Button addTask;
        ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            addTask = itemView.findViewById(R.id.add_task);
            playBtn = itemView.findViewById(R.id.play_tube);
            name = itemView.findViewById(R.id.movie_name);
            genre = itemView.findViewById(R.id.genre);
            rating = itemView.findViewById(R.id.rating);
            time = itemView.findViewById(R.id.time);
            year = itemView.findViewById(R.id.year);

            cardView = itemView.findViewById(R.id.card_row);
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position, String url, String name, String year);
    }
}
