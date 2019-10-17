package com.example.erjike.bistu.MusicPlayer.ui.list;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.SearchSongActivity;
import com.example.erjike.bistu.MusicPlayer.adapter.PlayingListAdapter;
import com.example.erjike.bistu.MusicPlayer.filesTool.PlayListSharedPerferences;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;
import com.example.erjike.bistu.MusicPlayer.netTools.ToolsInputLike;


import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SongListFragment extends Fragment {

    final ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (MyMusicService.MyBinder) iBinder;//绑定成功
            if(myBinder==null){
                Log.i(TAG, "onServiceConnected: 连接后， myBinder便是空");
            }
            else {
                Log.i(TAG, "onServiceConnected: 异步信息没有加载而已myBinder+"+myBinder.toString());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "onServiceDisconnected: 绑定失败。");
            throw new NullPointerException();//服务绑定失败后，自动报错退出
        }
    };
    private SongListViewModel songListViewModel;
    private List<SearchMusicModel> lSongList;//上一首歌所在列表，最后一首播放了的在链表尾部
    private List<SearchMusicModel> rSongList;//当前歌曲和下一首所在列表，当前播放永远是链表第一
    private PlayingListAdapter adapter;
    private ImageView searchSongs;
    private ImageView clearList;
    private RecyclerView recyclerView;
    private RelativeLayout musicPlayLinearLayout;

    private MyMusicService.MyBinder myBinder;//播放器binder,用于控制MediaPlayer
    //播放界面的对应注册

    private ImageView musicPlay;
    private ImageView musicNext;
    private SeekBar musicSeekBar;
    private TextView musicName;
    private TextView musicArtise;
    //注册handler用于多线程更新UI
    private static final int UPDATE_PROGRESS = 0;//常量
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    //实现每隔500毫秒更新一次界面
                    int positon = myBinder.getCurrenPostion();//毫秒为单位的时间
                    musicSeekBar.setProgress(positon);
                    handler.sendEmptyMessageAtTime(UPDATE_PROGRESS, 500);
                    //实现更新进度条的操作
                    //步骤1.更新musicSeekBar
                    break;
                default:
            }

        }
    };

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        songListViewModel =
                ViewModelProviders.of(this).get(SongListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_song_list, container, false);
        searchSongs = root.findViewById(R.id.music_list_add);
        searchSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //搜索歌曲
                Intent intent = new Intent(inflater.getContext(), SearchSongActivity.class);
                inflater.getContext().startActivity(intent);
            }
        });
        clearList=(ImageView)root.findViewById(R.id.clear_list_icon);

        recyclerView = (RecyclerView) root.findViewById(R.id.music_list_recyclerView);
        musicPlayLinearLayout = (RelativeLayout) root.findViewById(R.id.music_player_relativeLayout);
        //播放界面
        musicPlay = (ImageView) root.findViewById(R.id.playing_music_control);
        musicSeekBar = (SeekBar) root.findViewById(R.id.playing_music_SeekBar);
        musicNext = (ImageView) root.findViewById(R.id.playing_music_next);
        musicName = (TextView) root.findViewById(R.id.playing_music_name);
        musicArtise = (TextView) root.findViewById(R.id.playing_music_maker);


        init();


        songListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //通过model传递数据,暂时不考虑用这个方法
            }
        });
        return root;
        //TODO
    }

    private void init() {
        lSongList = new ArrayList<>();
        rSongList = new ArrayList<>();
        lSongList = PlayListSharedPerferences.loadLSharedPerference(getContext());//从文件中读取两个链表
        rSongList = PlayListSharedPerferences.loadRSharedPerference(getContext());
        Log.i(TAG, "init: lSongList.size:"+lSongList.size()+" rSongList.size:"+rSongList.size());
        adapter = new PlayingListAdapter();
        adapter.setlList(lSongList);
        adapter.setrList(rSongList);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();
        clearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayListSharedPerferences.writeRSharedPerference(new ArrayList<SearchMusicModel>(),getContext());
                lSongList.clear();
                PlayListSharedPerferences.writeLSharedPerference(new ArrayList<SearchMusicModel>(),getContext());
                rSongList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        if (adapter.getItemCount() != 0) {
            //当播放列表不为空的时候，显示并更新播放器相关信息
            musicPlayLinearLayout.setVisibility(View.VISIBLE);
            SearchMusicModel music = rSongList.get(0);
            //读取该id，并调用Service方法播放
            Intent intentService = new Intent(getActivity(), MyMusicService.class);
            String url= ToolsInputLike.getMp3Url(music.getMusicId(), "10.3.149.67", true, getContext());
            Log.i(TAG, "init: url+"+url);
            //intentService.putExtra("MP3url",url);
            getActivity().startService(intentService);
            getActivity().bindService(intentService, myConnection, getActivity().BIND_AUTO_CREATE);
            //绑定后设置对应内容

            musicName.setText(music.getMusicName());
            musicArtise.setText(music.getArtiseName());
            //musicSeekBar.setMax(myBinder.getDuration());//设置seekBar最大值
            //绑定播放界面众按键的操作

            /*
                实现点击播放/暂停，操作
             */
            musicPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myBinder != null) {
                        //实现播放暂停操作
                        Log.i(TAG, "onClick: myBinder ok！");
                        if (myBinder.isPlaying()) {
                            myBinder.pause();//暂停
                        } else {
                            myBinder.play();//播放
                        }
                    }else{
                        Log.i(TAG, "onClick: myBinder为空！");
                    }

                }
            });
            musicNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //TODO 实现下一首操作
                }
            });


        } else {
            //当播放列表为空时，播放器隐藏
            musicPlayLinearLayout.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onResume() {
        //TODO 选择该界面后，更新进度条,更新歌曲名与作者名
        adapter.notifyDataSetChanged();//更新adapter显示
        super.onResume();
    }

    @Override
    public void onDestroy() {
        //TODO 退出应用后解除绑定
        super.onDestroy();
    }

    @Override
    public void onStop() {
        //TODO 页面不显示时，停止更新进度条进度
        super.onStop();
    }

    class MyMusicService extends Service {
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
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {//当加载完成后
                        //mediaPlayer.start();
                        musicSeekBar.setMax(myBinder.getDuration());//加载完毕后设置musicSeekBar长度
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.stop();//停止播放

                        //TODO audio播放完成后自动触发：需要完成，如果歌单下一首存在，读取歌单下一首歌，创建新的MediaPlayer的操作

                    }

                });


            } catch (Exception e) {
                Log.e(TAG, "onBind: Exception message:" + e.getMessage());
            }

            return new MyMusicService.MyBinder();//返回继承binder的自建类
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

}