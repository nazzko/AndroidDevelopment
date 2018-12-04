package com.example.lab2.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab2.R;
import com.example.lab2.database.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteFragment extends Fragment {

    private DatabaseHelper db;
    private FavoritesAdapter favoritesAdapter = new FavoritesAdapter();

    @BindView(R.id.rvGames)
    RecyclerView rvGames;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.favorite);
        setupRecyclerView(view);
        loadRandomGames();
    }



/*
    @Override
    public void onGameClick(GbObjectResponse game) {
        Intent intent = GameDetailsActivity.makeIntent(getContext(), game);
        startActivity(intent);
    }
*/

    private void loadRandomGames() {
        db = DatabaseHelper.createInstance(getContext());
        favoritesAdapter.replaceAll(db.getFromDb());
    }

    private void setupRecyclerView(View view) {
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvGames.setLayoutManager(layoutManager);
        rvGames.setAdapter(favoritesAdapter);
    }

}
