package com.example.lab2.presenters;

import com.example.lab2.models.FavoriteModel;
import com.example.lab2.views.FavoriteView;

public class FavoritePresenterImpl implements FavoritePresenter{
    FavoriteModel mModel;
    FavoriteView mView;

    public FavoritePresenterImpl(FavoriteView view, FavoriteModel model) {
        mModel = model;
        mView = view;
    }

    @Override
    public void onCreate() {

    }

//
//    private void loadFavoriteGames() {
//
//    }
}
