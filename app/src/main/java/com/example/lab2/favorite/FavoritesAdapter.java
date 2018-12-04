package com.example.lab2.favorite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab2.R;
import com.example.lab2.games.GamesAdapter;
import com.example.lab2.network.FavGame;
import com.example.lab2.network.GbObjectResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<FavGame> favgames = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_game, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open description on favorite game click
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final FavoritesAdapter.ViewHolder holder, int position) {
        final FavGame game = favgames.get(position);
        holder.ivPicture.setImageBitmap(game.getImage());
        holder.tvName.setText(game.getName());
        holder.tvDeck.setText(game.getDeck());
    }

    @Override
    public int getItemCount() {
        return favgames.size();
    }

    public void addAll(List<FavGame> gamesToAdd) {
        favgames.addAll(gamesToAdd);
        notifyDataSetChanged();
    }

    public void replaceAll(List<FavGame> gamesToReplace) {
        favgames.clear();
        favgames.addAll(gamesToReplace);
        notifyDataSetChanged();



    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPicture)
        ImageView ivPicture;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDeck)
        TextView tvDeck;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
