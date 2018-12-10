package com.example.lab2.entities;

import android.graphics.Bitmap;

import java.sql.Blob;

public class FavGame {
    private final String guid;
    private final String name;
    private final String deck;
    private final String description;
    private final Bitmap image;

    public FavGame(String guid, String name, String deck, String description, Bitmap image) {
        this.guid = guid;
        this.name = name;
        this.deck = deck;
        this.description = description;
        this.image = image;
    }

    public String getId() {
        return guid;
    }

    public String getName() {
        return name;
    }

    public String getDeck() {
        return deck;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public String toString() {
        return this.name + " : " + this.deck + ":" + this.description;
    }
}
