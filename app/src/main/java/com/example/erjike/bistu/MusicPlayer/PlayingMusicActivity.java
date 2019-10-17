package com.example.erjike.bistu.MusicPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.erjike.bistu.MusicPlayer.Service.MusicService;

public class PlayingMusicActivity extends AppCompatActivity {
    private static final int UPDATE_PROGRESS=0;
    //标题
    TextView musicName;
    ImageView back;
    ImageView microphone;
    //内容
    ImageView musicImage;//专辑图片
    ImageView musicLike;
    ImageView musicPlayingType;
    ImageView musicCommit;//评论
    //控制
    ImageView lastMusic;
    ImageView thisControl;
    ImageView nextMusic;
    SeekBar seekBar;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_PROGRESS:
                    //updateProgress();
            }

        }
    };
    //private MusicService.MyBinder musicControl;//控制音乐
    //歌词
    RecyclerView lyrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_layout);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    private void init() {
        Intent intent = getIntent();

        String musicId = intent.getStringExtra("id");//获得点击歌曲的id
        //TODO 将歌曲对应的播放内容添加到当前歌单链表正在播放歌曲的后一个，然后将播放列表的指针指向下一个（如何实现？本地？用接口回调？）


        //TODO 将歌曲传入，之后进行网络查询，调用查询方法返回相应数据
        musicName = (TextView) findViewById(R.id.playing_music_name);
        back = (ImageView) findViewById(R.id.playing_music_back);
        microphone = (ImageView) findViewById(R.id.playing_music_microphone);
        musicImage = (ImageView) findViewById(R.id.playing_music_imageView);
        musicLike = (ImageView) findViewById(R.id.playing_music_like);
        musicPlayingType = (ImageView) findViewById(R.id.playing_music_playing_type);
        musicCommit = (ImageView) findViewById(R.id.playing_music_commit);
        lastMusic = (ImageView) findViewById(R.id.playing_music_last_music);
        thisControl = (ImageView) findViewById(R.id.playing_music_controler);
        nextMusic = (ImageView) findViewById(R.id.playing_music_after_music);
        lyrics = (RecyclerView) findViewById(R.id.playing_music_lyrics);
        seekBar = (SeekBar) findViewById(R.id.playing_music_SeekBar);
        //TODO 设定当seekBar改变时对应的播放状态改变
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // NOTDO

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //NOTDO

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO 更改进度条后播放进度也会随之改变


            }
        });
        //TODO 所有相关功能的定义

    }



}
