package com.example.lab2.network;

import android.graphics.Bitmap;

import java.sql.Blob;

public class FavGame {
    private String guid;
    private String name;
    private String deck;
    private String description;
    private Bitmap image;

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

    public void setId(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return this.name + " : " + this.deck + ":" + this.description;
    }
}
