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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erjike.bistu.MusicPlayer.MainActivity;
import com.example.erjike.bistu.MusicPlayer.PlayingMusicActivity;
import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.SearchSongActivity;
import com.example.erjike.bistu.MusicPlayer.Service.MusicService;
import com.example.erjike.bistu.MusicPlayer.adapter.PlayingListAdapter;
import com.example.erjike.bistu.MusicPlayer.filesTool.PlayListSharedPerferences;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;
import com.example.erjike.bistu.MusicPlayer.netTools.ToolsInputLike;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SongListFragment extends Fragment {

    private SongListViewModel songListViewModel;
    private List<SearchMusicModel> lSongList;//上一首歌所在列表，最后一首播放了的在链表尾部
    private List<SearchMusicModel> rSongList;//当前歌曲和下一首所在列表，当前播放永远是链表第一
    private PlayingListAdapter adapter;
    private ImageView searchSongs;
    private ImageView clearList;
    private ImageView playType;
    private RecyclerView recyclerView;
    private RelativeLayout musicPlayLinearLayout;

    private MusicService.MyBinder myBinder;//播放器binder,用于控制MediaPlayer

    //播放界面的对应注册

    private ImageView musicPlay;
    private ImageView musicNext;
    private ImageView musicLast;
    private SeekBar musicSeekBar;
    private TextView musicName;
    private TextView musicArtise;
    /*
     type=0,顺序播放 Type=1,随机播放
     */
    int type = 0;
    //注册handler用于多线程更新UI
    private static final int UPDATE_PROGRESS = 0;//常量
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    //实现每隔500毫秒更新一次界面
                    //if(myBinder.getCurrenPostion()!=null){
                    try {
                        int positon = myBinder.getCurrenPostion();//毫秒为单位的时间
                        musicSeekBar.setProgress(positon);
                        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500);
                    }catch (Exception e){
                        Log.e(TAG, "handleMessage: e"+e.getMessage() );
                    }

                    //}

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
        clearList = (ImageView) root.findViewById(R.id.clear_list_icon);
        playType = (ImageView) root.findViewById(R.id.music_list_button_of_change);

        recyclerView = (RecyclerView) root.findViewById(R.id.music_list_recyclerView);

        musicPlayLinearLayout = (RelativeLayout) root.findViewById(R.id.music_player_relativeLayout);
        //播放界面
        musicPlay = (ImageView) root.findViewById(R.id.playing_music_control);
        musicSeekBar = (SeekBar) root.findViewById(R.id.playing_music_SeekBar);
        musicNext = (ImageView) root.findViewById(R.id.playing_music_next);
        musicLast = (ImageView) root.findViewById(R.id.playing_music_last);
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
    }

    private void init() {
        lSongList = new ArrayList<>();
        rSongList = new ArrayList<>();
        lSongList = PlayListSharedPerferences.loadLSharedPerference(getContext());//从文件中读取两个链表
        rSongList = PlayListSharedPerferences.loadRSharedPerference(getContext());
        Log.i(TAG, "init: lSongList.size:" + lSongList.size() + " rSongList.size:" + rSongList.size());
        adapter = new PlayingListAdapter();
        adapter.setlList(lSongList);
        adapter.setrList(rSongList);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();

        clearList.setOnClickListener(new View.OnClickListener() {
            //清空列表
            @Override
            public void onClick(View view) {
                PlayListSharedPerferences.writeRSharedPerference(new ArrayList<SearchMusicModel>(), getContext());
                lSongList.clear();
                PlayListSharedPerferences.writeLSharedPerference(new ArrayList<SearchMusicModel>(), getContext());
                rSongList.clear();
                adapter.notifyDataSetChanged();
                musicPlayLinearLayout.setVisibility(View.INVISIBLE);
            }
        });
        if (adapter.getItemCount() != 0) {
            //当播放列表不为空的时候，显示并更新播放器相关信息
            musicPlayLinearLayout.setVisibility(View.VISIBLE);
            SearchMusicModel music = rSongList.get(0);
            //读取该id，并调用Service方法播放
            Intent intentService = new Intent(getActivity(), MusicService.class);
            Log.i(TAG, "init: music.getMusicId():" + music.getMusicId());
            final String url = ToolsInputLike.getMp3Url(music.getMusicId(), MainActivity.HOST_IP, true, getContext());
            Log.i(TAG, "init: url+" + url);
            intentService.putExtra("MP3url", url);
            //intentService.putExtra("MP3url",url);
            getActivity().startService(intentService);
            final ServiceConnection myConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    myBinder = (MusicService.MyBinder) iBinder;//绑定成功
                    myBinder.setSeekBar(musicSeekBar);//绑定进度条
                    myBinder.setContext(getContext());
                    //加载成功
                    new Thread() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = UPDATE_PROGRESS;
                            handler.sendMessage(message);
                        }
                    }.run();//多线程更新界面

                    adapter.setPlayImage(musicPlay);
                    adapter.setShowMusicName(musicName);
                    adapter.setMyBinder(myBinder);


                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    Log.e("MainActivity", "onServiceDisconnected: 绑定失败。");
                    throw new NullPointerException();//服务绑定失败后，自动报错退出
                }
            };
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
                        if (myBinder.isPlaying()) {
                            myBinder.pause();//暂停
                            Log.i(TAG, "onClick: Pause!");
                            musicPlay.setImageResource(R.drawable.play);
                        } else {
                            myBinder.play();//播放
                            Log.i(TAG, "onClick: PLaying!,url=" + url);

                            musicPlay.setImageResource(R.drawable.pause);
                        }
                    } else {
                        Log.i(TAG, "onClick: myBinder为空！");
                    }

                }
            });

            musicPlayLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myBinder!=null){
                        Intent intent=new Intent(getActivity(), PlayingMusicActivity.class);
                        intent.putExtra("binder",myBinder);//传递Binder
                        intent.putExtra("type",type);
                        intent.putExtra("name",musicName.getText().toString());
                        PlayListSharedPerferences.writeLSharedPerference(lSongList,getContext());
                        PlayListSharedPerferences.writeRSharedPerference(rSongList,getContext());
                        //每次切换到别的activit时都会保存一次链表，从而与本地同步

                        getActivity().startActivity(intent);

                    }

                }
            });

            musicNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListToNext();
                }
            });

            musicLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListToLast();
                }
            });

            playType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //切换播放模式
                    if (type == 0) {
                        playType.setImageResource(R.drawable.random);
                        Toast.makeText(getContext(), "进入随机播放，自动回到播放列表首", Toast.LENGTH_SHORT).show();
                        lSongList.addAll(rSongList);
                        rSongList.clear();
                        rSongList.addAll(lSongList);
                        lSongList.clear();
                        String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, getContext());
                        if (url.equals("") || url == null) {
                            //重调该方法
                            Toast.makeText(getContext(), "您想要播放的音乐无版权，", Toast.LENGTH_SHORT).show();
                        } else {
                            myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());//添加歌曲
                            musicName.setText(rSongList.get(0).getMusicName());

                        }
                        adapter.notifyDataSetChanged();


                        type = 1;
                    } else {
                        playType.setImageResource(R.drawable.repeat);
                        Toast.makeText(getContext(), "顺序播放", Toast.LENGTH_SHORT).show();
                        type = 0;
                    }

                }
            });

        } else {
            //当播放列表为空时，播放器隐藏
            musicPlayLinearLayout.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onStart() {

        //每次重新进入都更新组件
        lSongList.clear();
        rSongList.clear();
        lSongList.addAll(PlayListSharedPerferences.loadLSharedPerference(getContext()));//从文件中读取两个链表
        rSongList.addAll(PlayListSharedPerferences.loadRSharedPerference(getContext()));
        if (musicPlayLinearLayout.getVisibility() == View.INVISIBLE && (lSongList.size() != 0 || rSongList.size() != 0)) {
            musicPlayLinearLayout.setVisibility(View.VISIBLE);
            if (myBinder.isPlaying()) {

            } else {
                myBinder.pause();
                myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
            }

        }
        adapter.notifyDataSetChanged();
        super.onStart();
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
        PlayListSharedPerferences.writeLSharedPerference(lSongList,getContext());
        PlayListSharedPerferences.writeRSharedPerference(rSongList,getContext());
        //摧毁该activity时，与本地同步
        super.onDestroy();

    }

    @Override
    public void onStop() {
        PlayListSharedPerferences.writeLSharedPerference(lSongList,getContext());
        PlayListSharedPerferences.writeRSharedPerference(rSongList,getContext());
        //TODO 页面不显示时，停止更新进度条进度
        super.onStop();
    }

    public void ListToNext() {
        handler.removeCallbacksAndMessages(null);//停止更新线程
        if (type == 0) {//顺序播放调用功能
            //TODO 多线程实现进度条更改
            if (rSongList.size() > 1) {//当rList有下一首的时候
                lSongList.add(rSongList.get(0));
                rSongList.remove(0);
                //将要播放的继续赋值为0
                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, getContext());
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(getContext(), "您想要播放的音乐无版权，自动切换到下一首", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "下一首", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else {
                lSongList.add(rSongList.get(0));
                rSongList.clear();
                rSongList.addAll(lSongList);
                lSongList.clear();
                //移动到第一首歌，上一首的链表就会变为空
                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, getContext());
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(getContext(), "您想要播放的音乐无版权，自动切换到下一首", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "下一首", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
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


                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, getContext());
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(getContext(), "您想要播放的音乐无版权，自动切换到下一首", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "下一首", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else {
                lSongList.addAll(rSongList);
                rSongList.clear();
                ;
                rSongList.addAll(lSongList);
                lSongList.clear();
                ListToNext();//重新调用
            }


        }

    }

    public void ListToLast() {
        handler.removeCallbacksAndMessages(null);//停止更新线程
        if (type == 0) {//顺序播放调用功能
            //播放上一首
            if (lSongList.size() > 0) {//当rList有下一首的时候
                rSongList.add(0,lSongList.get(lSongList.size()-1));
                lSongList.remove(lSongList.size()-1);
                //将要播放的继续赋值为0
                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, getContext());//判断是否为空
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(getContext(), "您想要播放的音乐无版权，自动切换到上一首", Toast.LENGTH_SHORT).show();
                    ListToLast();//重新调用该方法
                }
                if (myBinder.isPlaying()) {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());//添加歌曲
                    myBinder.play();
                } else {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());//添加歌曲
                }

                musicName.setText(rSongList.get(0).getMusicName());
                Toast.makeText(getContext(), "上一首", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else {
                lSongList.addAll(rSongList);
                rSongList.clear();
                rSongList.add(lSongList.remove(lSongList.size()-1));
                //移动到第一首歌，上一首的链表就会变为空
                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, getContext());
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(getContext(), "您想要播放的音乐无版权，自动切换到上一首", Toast.LENGTH_SHORT).show();
                    ListToLast();//重新调用该方法
                }
                if (myBinder.isPlaying()) {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                    myBinder.play();
                } else {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                }
                musicName.setText(rSongList.get(0).getMusicName());
                Toast.makeText(getContext(), "上一首", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

        } else {//随机播放调用
            if (lSongList.size() > 0) {//有上一首歌
                Random random = new Random();
                int position = random.nextInt(lSongList.size());


                SearchMusicModel music = lSongList.remove(position);//首先获得该位置音乐
                rSongList.add(0,music);//将该音乐加入到播放处


                String url = ToolsInputLike.getMp3Url(rSongList.get(0).getMusicId(), MainActivity.HOST_IP, true, getContext());
                if (url.equals("") || url == null) {
                    //重调该方法
                    Toast.makeText(getContext(), "您想要播放的音乐无版权，自动切换到上一首", Toast.LENGTH_SHORT).show();
                    ListToLast();//重新调用该方法
                }
                if (myBinder.isPlaying()) {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                    myBinder.play();
                } else {
                    myBinder.setNewMediaPlayer(rSongList.get(0).getMusicId());
                }

                musicName.setText(rSongList.get(0).getMusicName());
                Toast.makeText(getContext(), "上一首", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else {
                lSongList.addAll(rSongList);
                rSongList.clear();
                ListToLast();//重新调用
            }


        }

    }


}