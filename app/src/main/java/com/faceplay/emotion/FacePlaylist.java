package com.faceplay.emotion;

import java.util.List;

/**
 * Created by Ajay on 11/15/2015.
 */
public class FacePlaylist {
    private String imageURL;
    private String name;
    private int numberOfSongs;
    private String playlistURL;

    public String getPlaylistURL() {
        return playlistURL;
    }

    public void setPlaylistURL(String playlistURL) {
        this.playlistURL = playlistURL;
    }

    public FacePlaylist(String imageURL, String name, int numberOfSongs, String playlistURL) {
        this.imageURL = imageURL;
        this.name = name;
        this.numberOfSongs = numberOfSongs;
        this.playlistURL = playlistURL;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "FacePlaylist{" +
                "imageURL='" + imageURL + '\'' +
                ", name='" + name + '\'' +
                ", numberOfSongs=" + numberOfSongs +
                ", playlistURL='" + playlistURL + '\'' +
                '}';
    }
}
