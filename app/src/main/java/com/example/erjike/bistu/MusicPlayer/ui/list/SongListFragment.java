package com.example.erjike.bistu.MusicPlayer.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.SearchSongActivity;

public class SongListFragment extends Fragment {

    private SongListViewModel songListViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        songListViewModel =
                ViewModelProviders.of(this).get(SongListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_song_list, container, false);
        ImageView searchSongs=root.findViewById(R.id.music_list_add);
        searchSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(inflater.getContext(), SearchSongActivity.class);
                inflater.getContext().startActivity(intent);
            }
        });

        songListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               //TODO 当切换成了当前界面之后
            }
        });
        return root;
        //TODO
    }
}