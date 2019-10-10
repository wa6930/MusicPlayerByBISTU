package com.example.erjike.bistu.MusicPlayer.model;

public class SearchMusicModel {
    String musicId;
    String musicName;
    String artiseName;
    String imagUrl;

    public SearchMusicModel() {
    }

    public SearchMusicModel(String musicId, String musicName, String artiseName, String imagUrl) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.artiseName = artiseName;
        this.imagUrl = imagUrl;
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

    public void setImagUrl(String imagUrl) {
        this.imagUrl = imagUrl;
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

    public String getImagUrl() {
        return imagUrl;
    }
}
