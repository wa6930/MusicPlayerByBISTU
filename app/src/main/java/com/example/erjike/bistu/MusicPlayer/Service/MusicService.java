package com.example.erjike.bistu.MusicPlayer.Service;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class MusicService {
    String TAG = "ErJike's MusicService";
    private static final File PATH = Environment.getDataDirectory();//获取不同环境不同路径
    public final int RANDOM = 1;
    public final int IN_ORDER = 0;
    public int nextType = 0;
    public List<String> musicList;//存放音乐的绝对路径
    public MediaPlayer player;
    public int songNum;//当前播放的歌曲在list的下标
    public String songName;//当前歌曲名

    //虚拟类：用于实现查找所有.mp3文件
    class MusicFilter implements FilenameFilter {

        @Override
        public boolean accept(File file, String s) {
            return (s.endsWith(".mp3"));//.mp3结尾为true
        }


    }

    public MusicService() {
        super();
        //初始化
        player = new MediaPlayer();
        musicList = new ArrayList<String>();
        try {
            File MUSIC_PATH = new File(PATH, "music");//获取根目录下的music
            if (MUSIC_PATH.listFiles(new MusicFilter()).length > 0) {//找到文件了
                for (File file : MUSIC_PATH.listFiles(new MusicFilter())) {
                    musicList.add(file.getAbsolutePath());
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "MusicService: 文件读取异常！");
        }
    }

    /*******实现基本的播放，暂停，下一首，上一首************/
    //播放
    public void play() {
        try {
            player.reset();//重置多媒体
            String dataSourse = musicList.get(songNum);//获得当前需要播放音乐的路径
            player.setAudioSessionId(AudioManager.STREAM_MUSIC);//获取播放类型
            player.setDataSource(dataSourse);//设定播放路径
            player.prepare();//准备
            player.start();//开始播放

            //setOnConpletionListener 当该对象播放完成时发生
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    next();//播放完成后，自动播放下一首
                }
            });


        } catch (Exception e) {
            Log.e(TAG, "play:播放异常，异常为：" + e.getMessage());
        }

    }

    //继续播放
    public void goPlay() {
        int position = getCurrentProgress();
        player.seekTo(position);//设置当前MediaPlayer的播放位置，单位毫秒
        try {
            player.prepare();

        } catch (Exception e) {
            Log.e(TAG, "goPlay: 发生异常，异常为：" + e.getMessage());
        }
        player.start();

    }

    //获取当前进度
    public int getCurrentProgress() {
        if(player!=null&player.isPlaying()){
            return player.getCurrentPosition();
        }
        else if (player!=null & (!player.isPlaying())){
            return player.getCurrentPosition();

        }

        return 0;
    }

    //下一首
    public void next() {
        if (nextType == 0) {
            //TODO 顺序播放
        } else {
            //TODO 随机播放
        }

    }

    //上一首
    public void last() {
        if (nextType == 0) {
            //TODO 顺序播放
        } else {
            //TODO 随机播放
        }
    }

    //暂停
    public void pause() {
        //TODO
    }

    //停止
    public void stop() {
        //TODO
    }
    /*****************************************************/

}
