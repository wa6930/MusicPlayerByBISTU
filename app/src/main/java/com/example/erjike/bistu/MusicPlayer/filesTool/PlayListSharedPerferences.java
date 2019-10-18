package com.example.erjike.bistu.MusicPlayer.filesTool;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;

import java.util.ArrayList;
import java.util.List;

public class PlayListSharedPerferences {
    public static List<SearchMusicModel> loadRSharedPerference(Context context) {
        SharedPreferences sp = context.getSharedPreferences("RplayingList", Context.MODE_PRIVATE);
        List<SearchMusicModel> ouput = new ArrayList<>();
        int size = sp.getInt("Rsize", 0);
        for (int i = 0; i < size; i++) {
            SearchMusicModel musicModel = new SearchMusicModel();
            musicModel.setMusicId(sp.getString("r_id_" + i, ""));
            musicModel.setMusicName(sp.getString("r_name_" + i, ""));
            ouput.add(musicModel);
        }

        return ouput;

    }

    public static void writeRSharedPerference(List<SearchMusicModel> playingList, Context context) {
        SharedPreferences sp = context.getSharedPreferences("RplayingList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putInt("Rsize", playingList.size());//存入歌单长度
        int i = 0;
        for (SearchMusicModel music : playingList) {
            editor.putString("r_id_" + i, music.getMusicId());
            editor.putString("r_name_" + i, music.getMusicName());
            i++;
        }
        editor.commit();
        //全部添加
        //实现将当前右侧链表写入到文件中的操作
        //TODO 修改存储于读取方法，从而保证文件正常
    }

    public static List<SearchMusicModel> loadLSharedPerference(Context context) {
        SharedPreferences sp = context.getSharedPreferences("LplayingList", Context.MODE_PRIVATE);
        List<SearchMusicModel> ouput = new ArrayList<>();
        int size = sp.getInt("Lsize", 0);
        for (int i = 0; i < size; i++) {
            SearchMusicModel musicModel = new SearchMusicModel();
            musicModel.setMusicId(sp.getString("l_id_" + i, ""));
            musicModel.setMusicName(sp.getString("l_name_" + i, ""));
            ouput.add(musicModel);
        }

        return ouput;

    }

    public static void writeLSharedPerference(List<SearchMusicModel> playingList, Context context) {
        SharedPreferences sp = context.getSharedPreferences("LplayingList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putInt("Lsize", playingList.size());//存入歌单长度
        int i = 0;
        for (SearchMusicModel music : playingList) {
            editor.putString("l_id_" + i, music.getMusicId());
            editor.putString("l_name_" + i, music.getMusicName());
            i++;
        }
        editor.commit();

        //实现将当前左链表写入到文件中的操作
    }

    public static void addSongToPlayingList(SearchMusicModel music, Context context) {
        List<SearchMusicModel> musicModelsRList = new ArrayList<>();
        musicModelsRList.addAll(PlayListSharedPerferences.loadRSharedPerference(context));
        if (musicModelsRList.size() > 0) {
            SearchMusicModel lastPlay = musicModelsRList.get(0);
            List<SearchMusicModel> musicModelsLList = new ArrayList<>();
            musicModelsLList.addAll(PlayListSharedPerferences.loadLSharedPerference(context));
            musicModelsLList.add(lastPlay);
            PlayListSharedPerferences.writeLSharedPerference(musicModelsLList, context);
            musicModelsRList.set(0, music);

        }
        else {
            musicModelsRList.add(music);
        }
        PlayListSharedPerferences.writeRSharedPerference(musicModelsRList, context);


        //对当前文件进行操作，如果可以再顺便重新绑定一下Service，从而直接播放添加进的歌曲
    }
    public static List<SearchMusicModel> RDeleteMusic(SearchMusicModel music,Context context){

        //TODO 当想要删除左链表时
        return null;
    }

    public static List<SearchMusicModel> LDeleteMusic(SearchMusicModel music,Context context){
        //TODO 当想要删除右链表时
        return null;

    }

}
