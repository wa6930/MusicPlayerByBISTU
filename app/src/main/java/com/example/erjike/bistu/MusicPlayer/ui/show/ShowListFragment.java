package com.example.erjike.bistu.MusicPlayer.ui.show;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.adapter.ShowSongListAdapter;
import com.example.erjike.bistu.MusicPlayer.db.ListNameDBHelper;
import com.example.erjike.bistu.MusicPlayer.db.PlayListDBHelper;
import com.example.erjike.bistu.MusicPlayer.model.ListNameModel;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;

import java.util.ArrayList;
import java.util.List;

public class ShowListFragment extends Fragment {

    private ShowListViewModel showListViewModel;
    ExpandableListView expandableListView;
    ShowSongListAdapter adapter;
    List<String> listName=new ArrayList<>();
    List<ListNameModel> listNameModel=new ArrayList<ListNameModel>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        showListViewModel =
                ViewModelProviders.of(this).get(ShowListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_show_list, container, false);
        /*
        设置二级菜单相关
         */
        ListNameDBHelper dbHelper=new ListNameDBHelper(getContext(),"listName.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        listName.addAll(ListNameDBHelper.queryAll(db,getContext()));//获得歌单名字
        for(int i=0;i<listName.size();i++){
            String  name=listName.get(i);
            PlayListDBHelper playListDBHelper=new PlayListDBHelper(getContext(),name,null,1);
            SQLiteDatabase playDb=playListDBHelper.getReadableDatabase();
            List<SearchMusicModel> musicList=new ArrayList<>();
            musicList.addAll(PlayListDBHelper.getSearchSongList(playDb,getContext()));
            ListNameModel NameModel=new ListNameModel(name,musicList);
            listNameModel.add(NameModel);
        }

        expandableListView=(ExpandableListView)root.findViewById(R.id.list_of_song_expandableListView);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //点击组菜单时会出现的功能
                return true;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //TODO 点击出现的编辑菜单
                return true;
            }
        });
        if(adapter==null){
            adapter=new ShowSongListAdapter(getContext(),getLayoutInflater(),listNameModel);
            expandableListView.setAdapter(adapter);
        }else {
            adapter.flashData(listNameModel);
        }

        expandableListView.setGroupIndicator(null);//无系统自带箭头
        expandableListView.setSelection(0);



        showListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               //TODO 关于操作逻辑
            }
        });
        return root;
        //TODO
    }
}