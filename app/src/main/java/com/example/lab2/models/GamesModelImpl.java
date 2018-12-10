package com.example.lab2.models;

import android.content.Context;

import com.example.lab2.entities.GbObjectsListResponse;
import com.example.lab2.fragments.Games;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesModelImpl {
    private Context mContext;

    public GamesModelImpll(Context context) {
        context = mContext;
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
                Games.this.call = call.clone();
                GbObjectsListResponse gbObjectsListResponse = response.body();
                if (gbObjectsListResponse != null) {
                    adapter.replaceAll(gbObjectsListResponse.getResults());
                }
            }

            @Override
            public void onFailure(Call<GbObjectsListResponse> call, Throwable t) {
                Games.this.call = call.clone();
                if (!call.isCanceled()) {
                    net_error.setVisibility(net_error.VISIBLE);
                }
            }
        });
    }
}
