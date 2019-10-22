package com.example.erjike.bistu.MusicPlayer.matrix;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.logging.LogRecord;

public class RefreshHandle extends Handler {
    LoadingDiscView loadingDiscView;

    public RefreshHandle(LoadingDiscView loadingDiscView) {
        this.loadingDiscView = loadingDiscView;
        loadingDiscView.setRefreshHandle(this);
    }

    public void run() {
        loadingDiscView.update();
        removeCallbacksAndMessages(null);
        sendEmptyMessageDelayed(0,65);
    }

    public void stop() {
        removeCallbacksAndMessages(null);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case 0:
                run();
                break;
        }
    }
//TODO
}
