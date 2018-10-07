package com.example.lab2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.lab2.Adapter.DataModel;
import com.example.lab2.Adapter.PersonAdapter;

import java.util.ArrayList;

public class UserDataActivity extends AppCompatActivity {

    private ArrayList<DataModel> users = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        init();
    }

    private void readFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("userS", Context.MODE_PRIVATE);
        if (sharedPref.getAll().size() > 0) {
            for (int id = 1; id <= sharedPref.getAll().size() / 4; id++) {
                users.add(new DataModel(
                        sharedPref.getString("name" + id, ""),
                        sharedPref.getString("surname" + id, ""),
                        sharedPref.getString("email" + id, ""),
                        sharedPref.getString("phone" + id, "")));
            }
        }
    }

    private void init() {
        mRecyclerView = findViewById(R.id.resView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        readFromPreferences();
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new PersonAdapter(users);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }
}
