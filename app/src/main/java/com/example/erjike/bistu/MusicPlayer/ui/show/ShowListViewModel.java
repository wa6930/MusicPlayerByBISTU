package com.example.erjike.bistu.MusicPlayer.ui.show;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShowListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShowListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}