package com.nerdgeeks.moviepad.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerdgeeks.moviepad.R;
import com.nerdgeeks.moviepad.data.domain.yts.Cast;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder> {

    private List<Cast> castList;
    private Context context;

    public CastAdapter(List<Cast> castList, Context context) {
        this.castList = castList;
        this.context = context;
    }

    @NonNull
    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cast, parent, false);
        return new CastHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CastHolder holder, int position) {
        Picasso.with(context)
            .load(castList.get(position).getUrlSmallImage())
            .into(holder.castCover);

        holder.castName.setText(castList.get(position).getName());
        holder.characterName.setText(castList.get(position).getCharacterName());
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    class CastHolder extends RecyclerView.ViewHolder {
        private ImageView castCover;
        private TextView castName, characterName;
        CastHolder(View itemView) {
            super(itemView);
            castCover = itemView.findViewById(R.id.image_cast);
            castName = itemView.findViewById(R.id.cast_name);
            characterName = itemView.findViewById(R.id.character_name);
        }
    }
}
