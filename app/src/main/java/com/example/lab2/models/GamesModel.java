package com.example.lab2.models;


public interface GamesModel {
    void loadRandomGames(Result result);
    interface Result {
        void onSuccess(ArrayList<Hit> hitList);
        void onFailure(Throwable throwable);
    }
}
