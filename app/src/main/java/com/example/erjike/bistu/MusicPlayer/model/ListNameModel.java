package com.example.erjike.bistu.MusicPlayer.model;

import java.util.List;

public class ListNameModel {
    private String title;
    private List<SearchMusicModel> list;//二级菜单内容，数据项

    public ListNameModel(String title, List<SearchMusicModel> list) {
        this.title = title;
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public List<SearchMusicModel> getList() {
        return list;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setList(List<SearchMusicModel> list) {
        this.list = list;
    }
}
