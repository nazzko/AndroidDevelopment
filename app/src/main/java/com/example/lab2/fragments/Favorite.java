package com.example.lab2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab2.R;
import com.example.lab2.adapters.FavoritesAdapter;
import com.example.lab2.database.DatabaseHelper;
import com.example.lab2.activity.FavGameDetailsActivity;
import com.example.lab2.entities.FavGame;
import com.example.lab2.models.FavoriteModel;
import com.example.lab2.models.FavoriteModelImpl;
import com.example.lab2.presenters.FavoritePresenter;
import com.example.lab2.presenters.FavoritePresenterImpl;
import com.example.lab2.views.FavoriteView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Favorite extends Fragment implements FavoritesAdapter.Callback, FavoriteView{

    private DatabaseHelper databaseHelper;
    private FavoritesAdapter favoritesAdapter = new FavoritesAdapter(this);

    @BindView(R.id.rvGames)
    RecyclerView rvGames;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    private FavoritePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        createPresenter();
        mPresenter.onCreate();
        return inflater.inflate(R.layout.fragment_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.favorite);
        setupRecyclerView(view);
        loadFavoriteGames();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFavoriteGames();
            }
        });
    }

    @Override
    public void onGameClick(FavGame game) {
        Intent intent = FavGameDetailsActivity.makeFavIntent(getContext(), game);
        startActivity(intent);
    }


    public void loadFavoriteGames() {
        databaseHelper = DatabaseHelper.createInstance(getContext());
        favoritesAdapter.replaceAll(databaseHelper.getFavoriteGames());
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setupRecyclerView(View view) {
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvGames.setLayoutManager(layoutManager);
        rvGames.setAdapter(favoritesAdapter);
    }

    private void createPresenter() {
//        FavoriteModel model = new FavoriteModelImpl(getActivity().getApplication());
//        mPresenter = new FavoritePresenterImpl(this, model);
    }
}
