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
import com.example.erjike.bistu.MusicPlayer.db.ListNameDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ShowListFragment extends Fragment {

    private ShowListViewModel showListViewModel;
    ExpandableListView expandableListView;
    List<String> listName=new ArrayList<>();

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
        listName.addAll(ListNameDBHelper.queryAll(db,getContext()));//为链表赋值

        expandableListView=(ExpandableListView)root.findViewById(R.id.list_of_song_expandableListView);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //暂时不打算做功能
                return false;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //TODO 点击出现的编辑菜单
                return false;
            }
        });
        expandableListView.setGroupIndicator(null);
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