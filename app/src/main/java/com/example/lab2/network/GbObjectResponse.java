package com.example.lab2.network;

import java.util.List;

public class GbObjectResponse {

    private long id;
    private String name;
    private String deck;
    private Image image;
    private String guid;
    private String description;
    private String locationCountry;
    private String locationCity;
    private List<Game> developedGames;

    public GbObjectResponse(long id, String name, String deck, String description) {
        this.id = id;
        this.name = name;
        this.deck = deck;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public List<Game> getDevelopedGames() {
        return developedGames;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public String getWebsite() {
        return website;
    }

    private String website;

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDeck() {
        return deck;
    }

    public String getGuid() {
        return guid;
    }

    public String getDescription() {
        return description;
    }

    public static class Image {

        private String smallUrl;

        private String superUrl;

        public String getSuperUrl() {
            return superUrl;
        }

        public String getSmallUrl() {
            return smallUrl;
        }
    }
}
