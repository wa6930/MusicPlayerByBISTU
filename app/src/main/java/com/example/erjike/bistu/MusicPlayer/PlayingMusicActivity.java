package com.example.erjike.bistu.MusicPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erjike.bistu.MusicPlayer.Service.MusicService;
import com.example.erjike.bistu.MusicPlayer.filesTool.PlayListSharedPerferences;
import com.example.erjike.bistu.MusicPlayer.matrix.LoadingDiscView;
import com.example.erjike.bistu.MusicPlayer.matrix.RefreshHandle;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;
import com.example.erjike.bistu.MusicPlayer.netTools.ToolsInputLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.wcy.lrcview.LrcView;

public class PlayingMusicActivity extends AppCompatActivity {
    public static final String TAG = "PlayingMusicActivity";
    int type = 0;//获取上一个界面传来的播放类型，0顺序，1随机
    private static final int UPDATE_PROGRESS = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    try{
                        int positon = myBinder.getCurrenPostion();//毫秒为单位的时间
                        seekBar.setProgress(positon);
                        lyrics.updateTime(positon);
                        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500);
                    }catch (Exception e){
                        Log.e(TAG, "handleMessage: e:"+e.getMessage());

                    }
                    //实现每隔500毫秒更新一次界面
                    break;
                default:
            }

        }
    };
    //动态显示内容
    private LoadingDiscView disc_motion;
    private RefreshHandle refreshHandle;
    RelativeLayout relativeLayout;
    //播放列表
    MusicService.MyBinder myBinder;
    List<SearchMusicModel> lSongList;
    List<SearchMusicModel> rSongList;
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

    //private MusicService.MyBinder musicControl;//控制音乐
    //歌词
    LrcView lyrics;//第三方库
    String lyricsString="";
    //歌词滚动使用了第三方库实现@https://github.com/wangchenyan/lrcview


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去除系统自带的actionBar,Activity时有效，AppCompatActivity无效，必须在setContentView前
        setContentView(R.layout.activity_playing_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.rgb(207, 234, 250));//与布局颜色相同
            //return;
        }
        getSupportActionBar().hide();//AppCompatActivity使用，隐藏actionBar
        init();
    }

    private void init() {
        Intent intent = getIntent();
        myBinder = (MusicService.MyBinder) intent.getSerializableExtra("binder");//获得并复制
        type = intent.getIntExtra("type", 0);//不存在就默认为0
        if (myBinder == null) {
            Log.i(TAG, "init: myBinder 为空！");
        }

        //将歌曲对应的播放内容添加到当前歌单链表正在播放歌曲（如何实现-本地）
        lSongList = new ArrayList<>();
        rSongList = new ArrayList<>();
        lSongList.addAll(PlayListSharedPerferences.loadLSharedPerference(PlayingMusicActivity.this));
        rSongList.addAll(PlayListSharedPerferences.loadRSharedPerference(PlayingMusicActivity.this));

        //Log.i(TAG, "init: myBinder is playing?+"+myBinder.isPlaying());//可以获得


        relativeLayout =(RelativeLayout)findViewById(R.id.playing_music_relativeLayout);
        musicName = (TextView) findViewById(R.id.playing_music_name);
        musicName.setText(intent.getStringExtra("name"));//歌名赋值
        back = (ImageView) findViewById(R.id.playing_music_back);

        microphone = (ImageView) findViewById(R.id.playing_music_microphone);
        //musicImage = (ImageView) findViewById(R.id.playing_music_imageView);  //不显示专辑
        musicLike = (ImageView) findViewById(R.id.playing_music_like);
        musicPlayingType = (ImageView) findViewById(R.id.playing_music_playing_type);
        musicCommit = (ImageView) findViewById(R.id.playing_music_commit);
        lastMusic = (ImageView) findViewById(R.id.playing_music_last_music);
        thisControl = (ImageView) findViewById(R.id.playing_music_controler);

        nextMusic = (ImageView) findViewById(R.id.playing_music_after_music);

        lyrics = (LrcView) findViewById(R.id.playing_music_lyrics);
        lyrics.setDraggable(false,null);//设置歌词不可拖动
        seekBar = (SeekBar) findViewById(R.id.playing_music_appCompateSeekBar);
        UpUI();

        //设定当seekBar改变时对应的播放状态改变
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
                myBinder.seekToPosition(seekBar.getProgress());
                if(lyrics.hasLrc()){
                    lyrics.updateTime(seekBar.getProgress());//拖动进度条使歌词时间戳改变
                }
                Log.i(TAG, "onStopTrackingTouch: i:" + seekBar.getProgress());
                //更改进度条后播放进度也会随之改变


            }
        });
        //实现动态显示图片
        disc_motion=new LoadingDiscView(this);
        refreshHandle=new RefreshHandle(disc_motion);
        relativeLayout.addView(disc_motion);
        if(myBinder.isPlaying()){
            refreshHandle.sendEmptyMessage(0);//判断是否播放
        }
        //初始化播放按钮
        if(myBinder.isPlaying()){
            thisControl.setImageResource(R.drawable.pause);//如果已经播放就显示暂停
        }
        else {
            thisControl.setImageResource(R.drawable.play);//反之播放
        }
        //所有相关功能的定义
        if(type==0){
            musicPlayingType.setImageResource(R.drawable.repeat);
        }
        else {
            musicPlayingType.setImageResource(R.drawable.random);
        }
        lastMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListToLast();
                UpUI();
            }
        });//上一首
        //结束activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//返回，结束该activity
            }
        });
        nextMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListToNext();
                UpUI();
            }
        });//下一首

        //控制播放，更换图标
        thisControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myBinder != null) {
                    //实现播放暂停操作
                    if (myBinder.isPlaying()) {
                        myBinder.pause();//暂停
                        Log.i(TAG, "onClick: Pause!");
                        refreshHandle.stop();
                        thisControl.setImageResource(R.drawable.play);
                    } else {
                        myBinder.play();//播放
                        thisControl.setImageResource(R.drawable.pause);
                        refreshHandle.run();
                    }
                } else {
                    Log.i(TAG, "onClick: myBinder为空！");
                }
            }
        });
        //点击打开评论界面
        musicCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开评论界面
                Intent intent2=new Intent(PlayingMusicActivity.this,CommentActivity.class);
                if(myBinder.getId()==null) {
                    intent2.putExtra("songId", rSongList.get(0).getMusicId());
                }else{
                    intent2.putExtra("songId", myBinder.getId());
                }
                startActivity(intent2);

            }
        });

    }

    public void ListToNext() {
        handler.removeCallbacksAndMessages(null);
        if (type == 0) {//顺序播放调用功能
            //TODO 多线程实现进度条更改
            if (rSongList.size() > 1) {//当rList有下一首的时候
                lSongList.add(rSongList.get(0));
                rSongList.remove(0);
                //将要播放的继续赋值为0
                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, PlayingMusicActivity.this);
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(PlayingMusicActivity.this, "您想要播放的音乐无版权，自动切换到下一首", Toast.LENGTH_SHORT).show();
                    ListToNext();//重新调用该方法
                }
                if (myBinder.isPlaying()) {
                    myBinder.pause();
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());//添加歌曲
                    myBinder.play();
                } else {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());//添加歌曲
                }

                musicName.setText(rSongList.get(0).getMusicName());
                Toast.makeText(PlayingMusicActivity.this, "下一首", Toast.LENGTH_SHORT).show();
            } else {
                lSongList.add(rSongList.get(0));
                rSongList.clear();
                rSongList.addAll(lSongList);
                lSongList.clear();
                //移动到第一首歌，上一首的链表就会变为空
                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, PlayingMusicActivity.this);
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(PlayingMusicActivity.this, "您想要播放的音乐无版权，自动切换到下一首", Toast.LENGTH_SHORT).show();
                    ListToNext();//重新调用该方法
                }
                if (myBinder.isPlaying()) {
                    myBinder.pause();
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                    myBinder.play();
                } else {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                }
                musicName.setText(rSongList.get(0).getMusicName());
                Toast.makeText(PlayingMusicActivity.this, "下一首", Toast.LENGTH_SHORT).show();
            }

        } else {//随机播放调用
            if (rSongList.size() > 1) {//有下一首歌
                Random random = new Random();
                int position = random.nextInt(rSongList.size());


                SearchMusicModel music = rSongList.remove(position);//首先获得该位置音乐
                lSongList.add(rSongList.get(0));
                SearchMusicModel lastmusic = rSongList.remove(0);//如果不是第一个，那么删除第一个
                if (lSongList.size() == 0) {
                    lSongList.remove(lSongList.size() - 1);//加回右链表
                    rSongList.add(lastmusic);
                }
                rSongList.add(0, music);


                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, PlayingMusicActivity.this);
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(PlayingMusicActivity.this, "您想要播放的音乐无版权，自动切换到下一首", Toast.LENGTH_SHORT).show();
                    ListToNext();//重新调用该方法
                }
                if (myBinder.isPlaying()) {
                    myBinder.pause();
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                    myBinder.play();
                } else {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                }

                musicName.setText(rSongList.get(0).getMusicName());
                Toast.makeText(PlayingMusicActivity.this, "下一首", Toast.LENGTH_SHORT).show();
            } else {
                lSongList.addAll(rSongList);
                rSongList.clear();
                ;
                rSongList.addAll(lSongList);
                lSongList.clear();
                ListToNext();//重新调用
            }


        }

    }//上一首

    public void ListToLast() {
        handler.removeCallbacksAndMessages(null);
        if (type == 0) {//顺序播放调用功能
            //播放上一首
            if (lSongList.size() > 0) {//当rList有下一首的时候
                rSongList.add(0, lSongList.get(lSongList.size() - 1));
                lSongList.remove(lSongList.size() - 1);
                //将要播放的继续赋值为0
                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, PlayingMusicActivity.this);//判断是否为空
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(PlayingMusicActivity.this, "您想要播放的音乐无版权，自动切换到上一首", Toast.LENGTH_SHORT).show();
                    ListToLast();//重新调用该方法
                }
                if (myBinder.isPlaying()) {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());//添加歌曲
                    myBinder.play();
                } else {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());//添加歌曲
                }

                musicName.setText(rSongList.get(0).getMusicName());
                Toast.makeText(PlayingMusicActivity.this, "上一首", Toast.LENGTH_SHORT).show();

            } else {
                lSongList.addAll(rSongList);
                rSongList.clear();
                rSongList.add(lSongList.remove(lSongList.size() - 1));
                //移动到第一首歌，上一首的链表就会变为空
                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, PlayingMusicActivity.this);
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(PlayingMusicActivity.this, "您想要播放的音乐无版权，自动切换到上一首", Toast.LENGTH_SHORT).show();
                    ListToLast();//重新调用该方法
                }
                if (myBinder.isPlaying()) {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                    myBinder.play();
                } else {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                }
                musicName.setText(rSongList.get(0).getMusicName());
                Toast.makeText(PlayingMusicActivity.this, "上一首", Toast.LENGTH_SHORT).show();

            }

        } else {//随机播放调用
            if (lSongList.size() > 0) {//有上一首歌
                Random random = new Random();
                int position = random.nextInt(lSongList.size());


                SearchMusicModel music = lSongList.remove(position);//首先获得该位置音乐
                rSongList.add(0, music);//将该音乐加入到播放处


                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, PlayingMusicActivity.this);
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(PlayingMusicActivity.this, "您想要播放的音乐无版权，自动切换到上一首", Toast.LENGTH_SHORT).show();
                    ListToLast();//重新调用该方法
                }
                if (myBinder.isPlaying()) {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                    myBinder.play();
                } else {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                }

                musicName.setText(rSongList.get(0).getMusicName());
                Toast.makeText(PlayingMusicActivity.this, "上一首", Toast.LENGTH_SHORT).show();
            } else {
                lSongList.addAll(rSongList);
                rSongList.clear();
                ListToLast();//重新调用
            }


        }

    }//下一首

    @Override
    public void onDestroy() {
        PlayListSharedPerferences.writeLSharedPerference(lSongList, PlayingMusicActivity.this);
        PlayListSharedPerferences.writeRSharedPerference(rSongList, PlayingMusicActivity.this);
        //摧毁该activity时，与本地同步
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        PlayListSharedPerferences.writeLSharedPerference(lSongList, PlayingMusicActivity.this);
        PlayListSharedPerferences.writeRSharedPerference(rSongList, PlayingMusicActivity.this);
        //停止该activity时，与本地同步
        super.onStop();
    }

    //更新UI
    public void UpUI() {
        if (myBinder != null) {

            seekBar.setMax(myBinder.getDuration());
            handler.sendEmptyMessage(UPDATE_PROGRESS);
            if(myBinder.getId()!=null&&myBinder.getId()!=""){
                Log.i(TAG, "UpUI: getId不为null"+myBinder.getId());
                lyricsString=ToolsInputLike.getLyrics(myBinder.getId(),MainActivity.HOST_IP,true,PlayingMusicActivity.this);
                Log.i(TAG, "UpUI: lyrics:"+lyricsString);
                lyrics.loadLrc(lyricsString);

            }
            else{
                Log.i(TAG, "UpUI: getId为空"+rSongList.get(0).getMusicId());
                lyricsString=ToolsInputLike.getLyrics(rSongList.get(0).getMusicId(),MainActivity.HOST_IP,true,PlayingMusicActivity.this);
                Log.i(TAG, "UpUI: lyrics:"+lyricsString);
                //以下内容均由第三方module实现,@https://github.com/wangchenyan/lrcview
                lyrics.loadLrc(lyricsString);



            }



        }

    }




}
