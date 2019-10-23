package com.example.erjike.bistu.MusicPlayer.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;


import com.example.erjike.bistu.MusicPlayer.MainActivity;
import com.example.erjike.bistu.MusicPlayer.adapter.PlayingListAdapter;
import com.example.erjike.bistu.MusicPlayer.filesTool.PlayListSharedPerferences;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;
import com.example.erjike.bistu.MusicPlayer.netTools.ToolsInputLike;

import java.io.Serializable;
import java.util.List;

import static android.media.MediaPlayer.*;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class MusicService extends Service {
    private String url;
    final MyBinder myBinder=new MyBinder();





    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");


    }


    @Override
    public IBinder onBind(Intent intent) {


        url = intent.getStringExtra("MP3url");//通过intent获取关键url
        //nextUrl = intent.getStringExtra("NextMP3url");//下一首歌的url地址
        try {
            myBinder.mediaPlayer=new MediaPlayer();
            myBinder.mediaPlayer.reset();
            Log.i(TAG, "onBind: url:"+url);//
            myBinder.mediaPlayer.setDataSource(url);

            myBinder.mediaPlayer.setLooping(false);//禁止单曲循环
            myBinder.mediaPlayer.prepareAsync();//网络音乐调用异步方法
            myBinder.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {//当加载完成后
                    Log.i(TAG, "onPrepared: mediaPlayer:加载成功！");
                    //mediaPlayer.start();
                    myBinder.seekBar.setMax(mediaPlayer.getDuration());//加载完毕后设置musicSeekBar长度
                    myBinder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            //Log.i(TAG, "onProgressChanged: i:"+i);//测试成功，可以正确更新
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            myBinder.seekToPosition(seekBar.getProgress());
                            Log.i(TAG, "onStopTrackingTouch: i:"+seekBar.getProgress());
                        }
                    });
                    Log.i(TAG, "onPrepared: getDuration:"+mediaPlayer.getDuration());

                }
            });
            myBinder.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();//停止播放

                    //TODO audio播放完成后自动触发：需要完成，如果歌单下一首存在，读取歌单下一首歌，创建新的MediaPlayer的操作

                }

            });


        } catch (Exception e) {
            Log.e(TAG, "onBind: Exception message:" + e.getMessage());
        }

        return myBinder;//返回继承binder的自建类
    }

    /*
        关闭线程时调用
     */
    @Override
    public boolean onUnbind(Intent intent) {


        Log.d(TAG, "onUnbind");
        if (myBinder.mediaPlayer.isPlaying()) {
            myBinder.mediaPlayer.stop();
        }
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder implements Serializable {//实现serializable从而可以通过intent传值
        private SeekBar seekBar;
        String id=null;
        MediaPlayer mediaPlayer;
        Context context;

        public MediaPlayer getMediaPlayer() {
            return mediaPlayer;
        }

        public String getId() {
            return id;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setMediaPlayer(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }
        public void setNewMediaPlayer(String musicid){
            id=musicid;
            String url=ToolsInputLike.getMp3Url(musicid,MainActivity.HOST_IP,true,context);
            if(url==null||url.equals("")){
                Toast.makeText(context,"无该歌曲版权，无法播放",Toast.LENGTH_SHORT).show();
                //TODO 当下一首为无版权歌曲时，如何操作呢
                return;
            }
            closeMedia();
            mediaPlayer=new MediaPlayer();
            mediaPlayer.reset();
            Log.i(TAG, "onBind: url:"+url);
            try {
                mediaPlayer.setDataSource(url);
            }catch (Exception e){
                Log.e(TAG, "setNewMediaPlayer: e:"+e.getMessage());
            }


            mediaPlayer.setLooping(false);//禁止单曲循环
            mediaPlayer.prepareAsync();//网络音乐调用异步方法
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {//当加载完成后
                    Log.i(TAG, "onPrepared: mediaPlayer:加载成功！");
                    //mediaPlayer.start(); //不默认播放
                    myBinder.seekBar.setMax(mediaPlayer.getDuration());//加载完毕后设置musicSeekBar长度
                    myBinder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            //Log.i(TAG, "onProgressChanged: i:"+i);//测试成功，可以正确更新
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            myBinder.seekToPosition(seekBar.getProgress());
                            Log.i(TAG, "onStopTrackingTouch: i:"+seekBar.getProgress());
                        }
                    });
                    Log.i(TAG, "onPrepared: getDuration:"+mediaPlayer.getDuration());

                }
            });
            myBinder.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    closeMedia();

                }

            });

            //实现重新配置mediaPlayer
        }

        public SeekBar getSeekBar() {
            return seekBar;
        }

        public void setSeekBar(SeekBar seekBar) {
            if(mediaPlayer.isPlaying()||mediaPlayer.isLooping()){
                seekBar.setMax(mediaPlayer.getDuration());
            }
            this.seekBar = seekBar;
        }

        String TAG = "ErJike's MusicService";

        /*
            true:播放中，false:暂停或者停止
         */
        public boolean isPlaying() {
            try {
                if(mediaPlayer!=null){
                    if (mediaPlayer.isPlaying()) return true;
                    else return false;
                }else return false;
            }
            catch (Exception e){
                return false;
            }



        }


        //实现具体的播放功能
        //播放
        public void play() {
            try{
                mediaPlayer.start();
                Log.i(TAG, "play: ");

            }catch (Exception e){
                Log.e(TAG, "play: e"+e.getMessage() );
            }



        }




        //暂停
        public void pause() {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    Log.i(TAG, "pause: ");
                }
            }catch (Exception e){
                Log.e(TAG, "pause: e"+e.getMessage() );
            }


        }

        //跳转到指定位置
        public void seekToPosition(int position) {
            try {
                mediaPlayer.seekTo(position);
            }catch (Exception e){
                Log.e(TAG, "seekToPosition: e"+e.getMessage() );
            }
        }


        /*
            返回毫秒为单位的播放时间
         */
        public int getCurrenPostion() {
            try {
                return mediaPlayer.getCurrentPosition();
            }catch (Exception e){
                Log.e(TAG, "getCurrenPostion: e"+e.getMessage() );
                return 1000;
            }

        }

        //返回歌曲的长度，单位为毫秒
        public int getDuration() {
            try {
                return mediaPlayer.getDuration();
            }catch (Exception e){
                Log.e(TAG, "getDuration: e"+e.getMessage() );
                return 1000;
            }
        }
        /**
         * 关闭播放器
         */
        public void closeMedia() {
            try {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.release();
                }
            }
            catch (Exception e){
                Log.e(TAG, "closeMedia: e"+e.getMessage() );
            }

        }


    }
}

