package com.example.erjike.bistu.MusicPlayer.ui.home;

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

public class LikeSongsFragment extends Fragment {

    private LikeSongsViewModel likeSongsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        likeSongsViewModel =
                ViewModelProviders.of(this).get(LikeSongsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_like_songs, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        likeSongsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
        //TODO
    }
}