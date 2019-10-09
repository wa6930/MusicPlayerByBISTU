package com.example.erjike.bistu.MusicPlayer.model;

public class MusicListModel {
    String musicName;
    String musicMaker;

    public MusicListModel(String musicName, String musicMaker) {
        this.musicName = musicName;
        this.musicMaker = musicMaker;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setMusicMaker(String musicMaker) {
        this.musicMaker = musicMaker;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getMusicMaker() {
        return musicMaker;
    }
}
