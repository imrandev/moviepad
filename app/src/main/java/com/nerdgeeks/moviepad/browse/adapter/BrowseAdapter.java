package com.nerdgeeks.moviepad.browse.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.browse.model.Movies;
import com.nerdgeeks.moviepad.browse.ui.DetailsActivity;
import com.nerdgeeks.moviepad.browse.ui.YoutubePlayerFragment;
import com.nerdgeeks.moviepad.todolist.database.DbHelper;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_row,parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ViewHolder myHolder = (ViewHolder) holder;

        Picasso.with(activity)
                .load(models.get(position).getMediumCoverImage())
                .into(myHolder.thumbnail);



        final int id = models.get(position).getId();

        final String movieName = models.get(position).getTitle();
        myHolder.name.setText(movieName);

        final String year = String.valueOf(models.get(position).getYear());
        myHolder.year.setText(year);

        int item = 0;
        StringBuilder movieGenre = new StringBuilder();
        for (String genre : models.get(position).getGenres()){
            if (item < models.get(position).getGenres().size()-1){
                movieGenre.append(genre + ", ");
                item++;
            } else {
                movieGenre.append(genre);
                item = 0;
            }
        }
        myHolder.genre.setText(movieGenre);

        myHolder.rating.setText(String.valueOf(models.get(position).getRating()));

        myHolder.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper dbHelper = new DbHelper(activity);
                if (!dbHelper.isDataExists(id)){
                    dbHelper.insertNewMovie(id, movieName, "false", getEncodedImage(holder));
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

        myHolder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = models.get(holder.getAdapterPosition()).getTorrents().get(0).getUrl();
                onItemClickListener.onClick(v, holder.getAdapterPosition(), url, movieName, year);
            }
        });

        myHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("id", String.valueOf(id));
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
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

        private ImageView thumbnail, addTask, playBtn, downloadBtn;
        private TextView name, genre, rating, year;
        private CardView cardView;
        ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            addTask = (ImageView) itemView.findViewById(R.id.add_task);
            playBtn = (ImageView) itemView.findViewById(R.id.play_tube);
            downloadBtn = (ImageView) itemView.findViewById(R.id.download);
            name = (TextView) itemView.findViewById(R.id.movie_name);
            genre = (TextView) itemView.findViewById(R.id.genre);
            rating = (TextView) itemView.findViewById(R.id.rating);
            year = (TextView) itemView.findViewById(R.id.year);

            cardView = (CardView) itemView.findViewById(R.id.card_row);
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position, String url, String name, String year);
    }
}
