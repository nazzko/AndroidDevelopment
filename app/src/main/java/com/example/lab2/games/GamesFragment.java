package com.example.lab2.games;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lab2.PrefsConst;
import com.example.lab2.R;
//import com.example.lab2.gamedetails.GameDetailsActivity;
import com.example.lab2.network.GbObjectResponse;
import com.example.lab2.network.GbObjectsListResponse;
import com.example.lab2.network.GiantBombService;
import com.example.lab2.network.RestApi;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesFragment extends Fragment implements GamesAdapter.Callback {

    private static final String TAG = "33__";
    private static final int TOTAL_GAMES_COUNT = 64131;

    private GiantBombService service = RestApi.creteService(GiantBombService.class);
    private Random random = new Random();
    private GamesAdapter adapter = new GamesAdapter(this);
    private RecyclerView rvGames;
    @Nullable
    private Call<GbObjectsListResponse> call;
    private int gamesAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.games);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        gamesAmount = preferences.getInt(PrefsConst.SETTINGS_GAMES_AMOUNT, 10);
        setupRecyclerView(view);
        loadRandomGames();
    }

    @Override
    public void onGameClick(GbObjectResponse game) {
//        Intent intent = GameDetailsActivity.makeIntent(getContext(), game);
//        startActivity(intent);
    }

    private void loadRandomGames() {
        if (call != null && call.isExecuted()) {
            return;
        }
        int offset = random.nextInt(TOTAL_GAMES_COUNT - gamesAmount + 1);
        call = service.getGames(gamesAmount, offset);
        //noinspection ConstantConditions
        call.enqueue(new Callback<GbObjectsListResponse>() {
            @Override
            public void onResponse(Call<GbObjectsListResponse> call, Response<GbObjectsListResponse> response) {
                GamesFragment.this.call = call.clone();
                GbObjectsListResponse gbObjectsListResponse = response.body();
                if (gbObjectsListResponse != null) {
                    adapter.replaceAll(gbObjectsListResponse.getResults());
                }
            }

            @Override
            public void onFailure(Call<GbObjectsListResponse> call, Throwable t) {
                GamesFragment.this.call = call.clone();
                if (!call.isCanceled()) {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView(View view) {
        rvGames = view.findViewById(R.id.rvGames);
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

}
