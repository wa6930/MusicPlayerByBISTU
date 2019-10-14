package com.example.erjike.bistu.MusicPlayer.model;

public class SearchMusicModel {
    String musicId;
    String musicName;
    String artiseName;
    String imagUri;

    public SearchMusicModel() {
    }

    public SearchMusicModel(String musicId, String musicName, String artiseName, String imagUri) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.artiseName = artiseName;
        this.imagUri = imagUri;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setArtiseName(String artiseName) {
        this.artiseName = artiseName;
    }

    public void setImagUri(String imagUrl) {
        this.imagUri = imagUrl;
    }

    public String getMusicId() {
        return musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getArtiseName() {
        return artiseName;
    }

    public String getImagUri() {
        return imagUri;
    }
}
