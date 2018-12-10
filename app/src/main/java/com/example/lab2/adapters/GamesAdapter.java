package com.example.lab2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab2.R;
import com.example.lab2.network.GbObjectResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {

    private List<GbObjectResponse> games = new ArrayList<>();
    private final Callback callback;

    public GamesAdapter(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_game, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GbObjectResponse game = games.get(holder.getAdapterPosition());
                callback.onGameClick(game);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Log.d("44__", "position - " + position);
        final GbObjectResponse game = games.get(position);
        Picasso.get().load(game.getImage().getSmallUrl()).into(holder.ivPicture);
        holder.tvName.setText(game.getName());
        holder.tvDeck.setText(game.getDeck());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void addAll(List<GbObjectResponse> gamesToAdd) {
        games.addAll(gamesToAdd);
        notifyDataSetChanged();
    }

    public void replaceAll(List<GbObjectResponse> gamesToReplace) {
        games.clear();
        games.addAll(gamesToReplace);
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

    public interface Callback {
        void onGameClick(GbObjectResponse game);
    }

}
