package com.example.erjike.bistu.MusicPlayer.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import com.example.erjike.bistu.MusicPlayer.adapter.PlayingListAdapter;
import com.example.erjike.bistu.MusicPlayer.filesTool.PlayListSharedPerferences;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;

import java.util.List;

import static android.media.MediaPlayer.*;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class MusicService extends Service {
    private String url;
    private String nextUrl;
    private String lastUrl;
    private MediaPlayer mediaPlayer;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mediaPlayer = new MediaPlayer();//初始化mediaPlayer

    }


    @Override
    public IBinder onBind(Intent intent) {
        url = intent.getStringExtra("MP3url");//通过intent获取关键url
        //nextUrl = intent.getStringExtra("NextMP3url");//下一首歌的url地址
        try {
            mediaPlayer.reset();

            mediaPlayer.setDataSource(url);
            mediaPlayer.setLooping(false);//禁止单曲循环
            mediaPlayer.prepareAsync();//网络音乐调用异步方法
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {//当加载完成后
                    //mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();//停止播放

                    // audio播放完成后自动触发：需要完成，如果歌单下一首存在，读取歌单下一首歌，创建新的MediaPlayer的操作

                }

            });


        } catch (Exception e) {
            Log.e(TAG, "onBind: Exception message:" + e.getMessage());
        }

        return new MusicService.MyBinder();//返回继承binder的自建类
    }

    /*
        关闭线程时调用
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {

        String TAG = "ErJike's MusicService";

        /*
            true:播放中，false:暂停或者停止
         */
        public boolean isPlaying() {
            if (mediaPlayer.isPlaying()) return true;
            else return false;
        }


        //实现具体的播放功能
        //播放
        public void play() {
            mediaPlayer.start();


        }

        //继续播放
        public void goPray() {
            mediaPlayer.start();

        }

        public void goNextSong() {

        }

        //暂停
        public void pause() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }

        }

        //跳转到指定位置
        public void seekToPosition(int position) {
            mediaPlayer.seekTo(position);
        }


        /*
            返回毫秒为单位的播放时间
         */
        public int getCurrenPostion() {
            return mediaPlayer.getCurrentPosition();
        }

        //返回歌曲的长度，单位为毫秒
        public int getDuration() {
            return mediaPlayer.getDuration();
        }


    }
}
