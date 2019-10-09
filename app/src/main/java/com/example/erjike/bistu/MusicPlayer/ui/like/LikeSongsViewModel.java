package com.example.erjike.bistu.MusicPlayer.ui.like;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LikeSongsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LikeSongsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}