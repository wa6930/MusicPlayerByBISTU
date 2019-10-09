package com.example.erjike.bistu.MusicPlayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
        Toast.makeText(mContext,"创建了新的表单！表单名为"+sqLiteDatabase.getPath(),Toast.LENGTH_LONG).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
