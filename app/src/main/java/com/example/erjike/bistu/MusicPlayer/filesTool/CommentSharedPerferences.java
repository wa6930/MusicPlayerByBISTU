package com.example.erjike.bistu.MusicPlayer.filesTool;

import android.content.Context;
import android.content.SharedPreferences;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentSharedPerferences {
    /*
        用于向文件中添加与歌词id相同的sharedPerferences文件来实现的
     */
    public static List<String> loadSharedPerference(String musicId,Context context){
        SharedPreferences sp=context.getSharedPreferences(musicId,Context.MODE_PRIVATE);
        List<String> output=new ArrayList<>();
        int size= sp.getInt("size",0);
        for(int i=0;i<size;i++){
            output.add(sp.getString("com_"+i,""));//读取
        }
        return output;
    }
    //添加评论段
    public static void writeSharedPerference(String musicId,List<String> comList,Context context){
        SharedPreferences sp=context.getSharedPreferences(musicId,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.putInt("size",comList.size());
        int i=0;
        for(String s : comList){

            editor.putString("com_"+i,s);
            i++;
        }
        editor.commit();
    }

    public static void addNewComment(String musicId, String comment, Context context){
        List<String> commentList=new ArrayList<>();
        commentList.addAll(CommentSharedPerferences.loadSharedPerference(musicId,context));
        commentList.add(comment);
        CommentSharedPerferences.writeSharedPerference(musicId,commentList,context);

    }
}
