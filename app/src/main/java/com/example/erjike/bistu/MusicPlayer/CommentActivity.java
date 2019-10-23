package com.example.erjike.bistu.MusicPlayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.erjike.bistu.MusicPlayer.filesTool.CommentSharedPerferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    EditText commentInput;//输入评论
    Button submit;//评论按钮
    ListView listView;//显示过往评论
    Intent intent;
    String songId;//从intent中获得的歌曲id
    List<String> commentList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
    }

    private void init() {
        commentInput=(EditText)findViewById(R.id.comment_editText);
        submit=(Button)findViewById(R.id.comment_button);
        listView=(ListView)findViewById(R.id.comment_listView);
        intent=getIntent();
        songId=intent.getStringExtra("songId");
        commentList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(CommentActivity.this,R.layout.support_simple_spinner_dropdown_item,commentList);
        listView.setAdapter(adapter);
        commentList.addAll(CommentSharedPerferences.loadSharedPerference(songId,CommentActivity.this));
        adapter.notifyDataSetChanged();

        //点击评论按钮之后
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String s=df.format(new Date());
                s+=": "+commentInput.getText().toString();
                CommentSharedPerferences.addNewComment(songId,s,CommentActivity.this);
                commentList.clear();
                commentList.addAll(CommentSharedPerferences.loadSharedPerference(songId,CommentActivity.this));
                adapter.notifyDataSetChanged();
            }
        });


    }
}
