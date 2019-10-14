package com.example.erjike.bistu.MusicPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;


import com.example.erjike.bistu.MusicPlayer.adapter.SearchAllSongAdapter;
import com.example.erjike.bistu.MusicPlayer.adapter.ShowSongListAdapter;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;
import com.example.erjike.bistu.MusicPlayer.netTools.ToolsInputLike;

import java.util.ArrayList;
import java.util.List;

public class SearchedActivity extends AppCompatActivity {
    List<SearchMusicModel> list;
    RecyclerView recyclerView;
    SearchAllSongAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched);
        list = new ArrayList<SearchMusicModel>();
        Intent intent = getIntent();
        String text = intent.getStringExtra("name");
        list.addAll(ToolsInputLike.getSearchSongs(text, "10.3.149.67",true,SearchedActivity.this));
        initView();

    }

    public void initView() {
        recyclerView=(RecyclerView)findViewById(R.id.searched_list_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchedActivity.this,1));
        if(adapter==null){
            adapter=new SearchAllSongAdapter(SearchedActivity.this,list);
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
