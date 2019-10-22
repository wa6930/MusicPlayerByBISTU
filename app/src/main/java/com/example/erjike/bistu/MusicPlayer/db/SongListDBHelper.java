package com.example.erjike.bistu.MusicPlayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;

/*
用于显示当前歌单的本地化
 */
public class SongListDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME="playList";
    public static final String TABLE_LIST_1="songId";//歌曲id
    public static final String TABLE_LIST_2="songName";//歌名
    public static final String TABLE_LIST_3="imagURL";//封面图片URL
    public static final String TABLE_LIST_4="makerName";//作者名
    public static final String TABLE_LIST_5="lyrics";//歌词
    public static final String TABLE_LIST_6="commiter";//评论

    public static final String CREATE_BOOK="create table "+TABLE_NAME+" ("
            +TABLE_LIST_1+" TEXT primary key,"
            +TABLE_LIST_2+" TEXT,"
            +TABLE_LIST_3+" TEXT,"
            +TABLE_LIST_4+" TEXT,"
            +TABLE_LIST_5+" TEXT,"
            +TABLE_LIST_6+" TEXT)";
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
    public static void insertSong(SQLiteDatabase db, SearchMusicModel music,Context context){
        String TAG="ErJike's insertSong";
        Cursor search_cursor=db.query(TABLE_NAME,new String[]{TABLE_LIST_1,TABLE_LIST_2,TABLE_LIST_3,TABLE_LIST_4,TABLE_LIST_5,TABLE_LIST_6}
        ,TABLE_LIST_1+"=?",new String[]{music.getMusicId()},null,null,null);
        if(search_cursor.moveToNext()){//存在即更新
            ContentValues contentValues = new ContentValues();

            //id
            if (music.getMusicId().equals("") || music.getMusicId() == null) {
                contentValues.put(TABLE_LIST_1, "空");
            } else {
                contentValues.put(TABLE_LIST_1, music.getMusicId());
            }
            //name
            if (music.getMusicName().equals("") || music.getMusicName() == null) {
                contentValues.put(TABLE_LIST_2, "空");
            } else {
                contentValues.put(TABLE_LIST_2, music.getMusicName());
            }
            //imagUrl
            if (music.getImagUri().equals("") || music.getImagUri() == null) {
                contentValues.put(TABLE_LIST_3, "");
            } else {
                contentValues.put(TABLE_LIST_3, music.getImagUri());
            }
            //artiseName
            if (music.getArtiseName().equals("") || music.getArtiseName() == null) {
                contentValues.put(TABLE_LIST_4, "空");
            } else {
                contentValues.put(TABLE_LIST_4, music.getArtiseName());
            }


            db.update(TABLE_NAME, contentValues, TABLE_LIST_1 + "=?", new String[]{music.getMusicId()});
            Log.i(TAG, "addSongToShowList: 对应歌曲更新：" + music.getMusicName());

        }
        else{//不存在即添加
            ContentValues contentValues=new ContentValues();
            if (music.getMusicId().equals("") || music.getMusicId() == null) {
                contentValues.put(TABLE_LIST_1, "空");
            } else {
                contentValues.put(TABLE_LIST_1, music.getMusicId());
            }
            //name
            if (music.getMusicName().equals("") || music.getMusicName() == null) {
                contentValues.put(TABLE_LIST_2, "空");
            } else {
                contentValues.put(TABLE_LIST_2, music.getMusicName());
            }
            //imagUrl
            if (music.getImagUri().equals("") || music.getImagUri() == null) {
                contentValues.put(TABLE_LIST_3, "");
            } else {
                contentValues.put(TABLE_LIST_3, music.getImagUri());
            }
            //artiseName
            if (music.getArtiseName().equals("") || music.getArtiseName() == null) {
                contentValues.put(TABLE_LIST_4, "空");
            } else {
                contentValues.put(TABLE_LIST_4, music.getArtiseName());

            }

            db.insert(TABLE_NAME, null, contentValues);
            Log.i(TAG, "addSongToShowList: 对应歌曲添加：" + music.getMusicName());

        }
        search_cursor.close();//关闭查询

        // 用于添加歌曲到本SQlite数据库中

    }

}
