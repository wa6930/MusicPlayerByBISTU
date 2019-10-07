package com.example.erjike.bistu.MusicPlayer.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.erjike.bistu.MusicPlayer.R;

public class SongListFragment extends Fragment {

    private SongListViewModel songListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        songListViewModel =
                ViewModelProviders.of(this).get(SongListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_song_list, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        songListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
        //TODO
    }
}