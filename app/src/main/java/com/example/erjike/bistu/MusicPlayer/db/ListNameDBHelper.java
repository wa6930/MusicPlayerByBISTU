package com.example.erjike.bistu.MusicPlayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListNameDBHelper extends SQLiteOpenHelper {
    private Context mContext;
    public static final String TABLE_NAME="listNameTable";
    public static final String TABLE_LIST1="dbName";
    public static final String CRATE_TABLE="create table "+TABLE_NAME+" ("
            +TABLE_LIST1+" TEXT)";

    public ListNameDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CRATE_TABLE);
        ContentValues contentValues1=new ContentValues();
        contentValues1.put(TABLE_LIST1,"默认歌单1");
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues1);
        ContentValues contentValues2=new ContentValues();
        contentValues2.put(TABLE_LIST1,"默认歌单2");
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues2);
        ContentValues contentValues3=new ContentValues();
        contentValues3.put(TABLE_LIST1,"默认歌单3");
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues3);
        Toast.makeText(mContext,"创建了新的表单！表单名为"+sqLiteDatabase.getPath(),Toast.LENGTH_LONG).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //查询全部列表名，加入到链表中
    public static List<String> queryAll(SQLiteDatabase db,Context context){
        List<String> output=new ArrayList<>();
        String TAG="ErJike's queryAll";
        Cursor search_cursor=db.query(TABLE_NAME,new String[]{TABLE_LIST1},TABLE_LIST1+" LIKE ?",new String[]{"%%"},null,null,null);
        while (search_cursor.moveToNext()){
            String listname=search_cursor.getString(search_cursor.getColumnIndex(TABLE_LIST1));
            output.add(listname);
        }
        return output;
    }

}
