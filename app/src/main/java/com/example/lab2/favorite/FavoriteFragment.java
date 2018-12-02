package com.example.lab2.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lab2.R;
import com.example.lab2.database.DatabaseAdapter;
import com.example.lab2.network.GbObjectResponse;

import java.util.List;

import butterknife.ButterKnife;

public class FavoriteFragment extends Fragment {
    private ListView gameList;
    ArrayAdapter<GbObjectResponse> arrayAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        DatabaseAdapter adapter = new DatabaseAdapter(getContext());
        adapter.open();

        List<GbObjectResponse> games = adapter.getFavGames();

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, games);
        gameList.setAdapter(arrayAdapter);
        adapter.close();
    }
}
