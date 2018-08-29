package com.nerdgeeks.moviepad.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.app.Moviepad;
import com.nerdgeeks.moviepad.data.sqlite.DbHelper;
import com.nerdgeeks.moviepad.data.sqlite.data.DataManager;
import com.nerdgeeks.moviepad.data.sqlite.domain.Watch;

import java.util.ArrayList;

/**
 * Created by IMRAN on 9/14/2017.
 */

public class MovieAdapter extends ArrayAdapter<Watch> {

    private final Activity activity;
    private final ArrayList<Watch> values;

    private static class ViewHolder {
        TextView item_title;
        TextView item_genre;
        TextView item_time;
        TextView item_year;
        ImageView item_checked;
    }

    public MovieAdapter(@NonNull Activity activity, ArrayList<Watch> values) {
        super(activity, R.layout.item_row, values);
        this.activity = activity;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootView = convertView;

        if (rootView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            rootView = inflater.inflate(R.layout.item_row, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.item_title = rootView.findViewById(R.id.movie_title);
            viewHolder.item_genre = rootView.findViewById(R.id.movie_genre);
            viewHolder.item_time = rootView.findViewById(R.id.movie_time);
            viewHolder.item_year = rootView.findViewById(R.id.movie_year);
            viewHolder.item_checked = rootView.findViewById(R.id.check_box);
            rootView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rootView.getTag();
        String title = values.get(position).getName();
        holder.item_title.setText(title);

        String genre = values.get(position).getGenre().replace(",", "•");
        holder.item_genre.setText(genre);

        String year = "• " + values.get(position).getYear();
        holder.item_year.setText(year);

        String time = values.get(position).getTime();
        holder.item_time.setText(time);

        if (!values.get(position).isWatched()){
            byte[] imageAsBytes = Base64.decode(values.get(position).getImgUrl().getBytes(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            holder.item_checked.setImageBitmap(bitmap);
            holder.item_checked.setBackgroundResource(android.R.color.transparent);
        } else {
            holder.item_checked.setBackgroundResource(R.drawable.ic_check);
            holder.item_checked.setImageBitmap(null);
        }
        return rootView;
    }
}
