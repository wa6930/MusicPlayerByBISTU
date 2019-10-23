package com.example.erjike.bistu.MusicPlayer.ui.like;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LikeSongsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LikeSongsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("喜欢页面，暂时用不到，未使用");
    }

    public LiveData<String> getText() {
        return mText;
    }
}