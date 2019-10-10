package com.example.erjike.bistu.MusicPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.erjike.bistu.MusicPlayer.adapter.SearchLikeSongAdapter;
import com.example.erjike.bistu.MusicPlayer.netTools.ToolsInputLike;

import java.util.ArrayList;
import java.util.List;

public class SearchSongActivity extends AppCompatActivity {
    List<String> listName=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        final RecyclerView songLikeRecycler=findViewById(R.id.search_recyclerView);
        final EditText inputEdit=findViewById(R.id.search_input_text);
        final ImageView search_Image=(ImageView)findViewById(R.id.search_check_songs);
        final SearchLikeSongAdapter adapter=new SearchLikeSongAdapter(SearchSongActivity.this,listName);
        GridLayoutManager layoutManager = new GridLayoutManager(SearchSongActivity.this, 1);//一行显示一个
        songLikeRecycler.setLayoutManager(layoutManager);//绑定二者
        adapter.setFragmentManager(getSupportFragmentManager());
        adapter.setinputTextView(getWindow().getDecorView());
        songLikeRecycler.setAdapter(adapter);

        inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputEdit.getText().toString().length()>=1){
                    listName.clear();
                    listName.addAll(ToolsInputLike.getInputLike(inputEdit.getText().toString(),"10.3.149.67",true,SearchSongActivity.this));
                    //Log.i("LiksList.size", "afterTextChanged: "+listName.size());//数据正常
                    adapter.setNameList(listName);
                    adapter.notifyDataSetChanged();


                }
            }
        });

        search_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO,跳转到另一个搜索歌曲界面，调用ToolsInputLike中的getSearchSong方法
            }
        });
    }
}
