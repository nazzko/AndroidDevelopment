package com.example.lab2.presenters;

import com.example.lab2.models.GamesModel;
import com.example.lab2.views.GamesView;

public class GamesPresenterImpl implements GamesPresenter {
    GamesModel mModel;
    GamesView mView;

    public GamesPresenterImpl(GamesView view, GamesModel model) {
        mModel = model;
        mView = view;
    }

    @Override
    public void onCreate() {

    }
}
