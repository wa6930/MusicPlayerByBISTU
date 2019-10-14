package com.example.erjike.bistu.MusicPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayingMusicActivity extends AppCompatActivity {
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
    //歌词
    RecyclerView lyrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_layout);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    private void init() {
        Intent intent=getIntent();
        //TODO 将歌曲传入，之后进行网络查询，调用查询方法返回相应数据
        musicName=(TextView)findViewById(R.id.playing_music_name);
        back=(ImageView)findViewById(R.id.playing_music_back);
        microphone=(ImageView)findViewById(R.id.playing_music_microphone);
        musicImage=(ImageView)findViewById(R.id.playing_music_imageView);
        musicLike=(ImageView)findViewById(R.id.playing_music_like);
        musicPlayingType=(ImageView)findViewById(R.id.playing_music_playing_type);
        musicCommit=(ImageView)findViewById(R.id.playing_music_commit);
        lastMusic=(ImageView)findViewById(R.id.playing_music_last_music);
        thisControl=(ImageView)findViewById(R.id.playing_music_controler);
        nextMusic=(ImageView)findViewById(R.id.playing_music_after_music);
        lyrics=(RecyclerView)findViewById(R.id.playing_music_lyrics);
        //TODO 所有相关功能的定义

    }
}
