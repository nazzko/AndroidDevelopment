package com.example.lab2.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.TextView;

import com.example.lab2.interfaces.PrefsConst;
import com.example.lab2.R;
import com.example.lab2.adapters.GamesAdapter;
import com.example.lab2.activity.GameDetailsActivity;
import com.example.lab2.entities.GbObjectResponse;
import com.example.lab2.entities.GbObjectsListResponse;
import com.example.lab2.interfaces.GiantBombService;
import com.example.lab2.models.GamesModel;
import com.example.lab2.models.GamesModelImpl;
import com.example.lab2.network.RestApi;
import com.example.lab2.presenters.GamesPresenter;
import com.example.lab2.presenters.GamesPresenterImpl;
import com.example.lab2.views.GamesView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Games extends Fragment implements GamesAdapter.Callback, GamesView {

    private static final String EXTRA_GAME_GUID = "EXTRA_GAME_GUID";
    private static final int TOTAL_GAMES_COUNT = 64131;
    private GiantBombService service = RestApi.creteService(GiantBombService.class);
    private Random random = new Random();
    private GamesAdapter adapter = new GamesAdapter(this);

    @Nullable
    private Call<GbObjectsListResponse> call;
    private int gamesAmount;

    @BindView(R.id.rvGames)
    RecyclerView rvGames;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.net_error)
    public TextView net_error;
    private GamesPresenter mPresenter;

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
        toolbar.setTitle(R.string.games);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        gamesAmount = preferences.getInt(PrefsConst.SETTINGS_GAMES_AMOUNT, 10);
        setupRecyclerView(view);
        loadRandomGames();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadRandomGames();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onGameClick(GbObjectResponse game) {
        Intent intent = GameDetailsActivity.makeIntent(getContext(), game);
        intent.putExtra(EXTRA_GAME_GUID, game.getGuid());
        startActivity(intent);
    }


    public void setupRecyclerView(View view) {
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvGames.setLayoutManager(layoutManager);
        rvGames.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (call != null) {
            call.cancel();
        }
    }

    private void createPresenter() {
//        GamesModel model = new GamesModelImpl(getActivity().getApplication());
        mPresenter = new GamesPresenterImpl(this, model);
    }

}
