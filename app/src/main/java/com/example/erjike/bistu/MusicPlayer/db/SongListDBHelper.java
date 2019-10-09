package com.example.erjike.bistu.MusicPlayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

/*
用于显示当前歌单的本地化
 */
public class SongListDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME="playList";
    public static final String TABLE_LIST_1="songId";//歌曲id
    public static final String TABLE_LIST_2="songName";//歌名
    public static final String TABLE_LIST_3="imagURL";//封面图片URL
    public static final String TABLE_LIST_4="makerName";//作者名
    public static final String TABLE_LIST_5="isLike";//是否添加到喜欢
    public static final String TABLE_LIST_6="lyrics";//歌词
    public static final String TABLE_LIST_7="commit";//评论

    public static final String CREATE_BOOK="create table "+TABLE_NAME+" ("
            +TABLE_LIST_1+" TEXT primary key,"
            +TABLE_LIST_2+" TEXT,"
            +TABLE_LIST_3+" TEXT,"
            +TABLE_LIST_4+" TEXT,"
            +TABLE_LIST_5+" TEXT,"
            +TABLE_LIST_6+" TEXT,"
            +TABLE_LIST_7+" TEXT)";
    private Context mContext;

    public SongListDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);
        Toast.makeText(mContext,"创建了新的表单！表单名为"+sqLiteDatabase.getPath(),Toast.LENGTH_LONG).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //不会用到更新
    }

}
