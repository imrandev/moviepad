package com.nerdgeeks.moviepad.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nerdgeeks.moviepad.R;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreHolder> {

    private String[] genreList;
    private GenreListener genreListener;

    public GenreAdapter(String[] genreList) {
        this.genreList = genreList;
    }

    public void setGenreListener(GenreListener genreListener){
        this.genreListener = genreListener;
    }

    @NonNull
    @Override
    public GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre_list, parent, false);
        return new GenreHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GenreHolder holder, int position) {
        holder.genreBtn.setText(genreList[position]);

        holder.genreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genreListener.onClick(v, genreList[holder.getAdapterPosition()]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList.length;
    }

    class GenreHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button genreBtn;
        GenreHolder(View itemView) {
            super(itemView);
            genreBtn = itemView.findViewById(R.id.genreBtn);
        }

        @Override
        public void onClick(View v) {
            genreListener.onClick(v, genreList[getAdapterPosition()]);
        }
    }

    public interface GenreListener {
        void onClick(View v, String genre);
    }
}
