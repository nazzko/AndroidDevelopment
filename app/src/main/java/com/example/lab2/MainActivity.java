package com.example.lab2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.lab2.database.DatabaseHelper;
import com.example.lab2.fragments.FavoriteFragment;
import com.example.lab2.fragments.GamesFragment;
import com.example.lab2.fragments.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper databaseHelper;

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        GamesFragment gamesFragment = new GamesFragment();
        replaceFragment(gamesFragment);

        databaseHelper = DatabaseHelper.createInstance(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {
            return true;
        }
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.nav_games:
                fragment = new GamesFragment();
                break;
            case R.id.nav_favorite:
                fragment = new FavoriteFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new Fragment();
                break;
        }
        replaceFragment(fragment);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
