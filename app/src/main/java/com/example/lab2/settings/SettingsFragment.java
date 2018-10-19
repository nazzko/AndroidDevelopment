package com.example.lab2.settings;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.lab2.PrefsConst;
import com.example.lab2.R;

import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends Fragment {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private Spinner spGamesAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefsEditor = prefs.edit();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
        spGamesAmount = view.findViewById(R.id.spGamesAmount);
        setSelections();
        setOnItemSelectedListeners();
    }

    private void setSelections() {
        int gamesAmount = prefs.getInt(PrefsConst.SETTINGS_GAMES_AMOUNT, 10);
        spGamesAmount.setSelection(getAmountIndex(gamesAmount));
    }

    private int getAmountIndex(int amount) {
        String[] optionsArray = getResources().getStringArray(R.array.amount_options);
        List<String> optionsList = Arrays.asList(optionsArray);
        return optionsList.indexOf(String.valueOf(amount));
    }

    private void setOnItemSelectedListeners() {
        spGamesAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String amountString = parent.getItemAtPosition(position).toString();
                int amountInt = Integer.parseInt(amountString);
                prefsEditor.putInt(PrefsConst.SETTINGS_GAMES_AMOUNT, amountInt).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

}
