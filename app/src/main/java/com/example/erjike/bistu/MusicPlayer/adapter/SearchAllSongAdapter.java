package com.example.erjike.bistu.MusicPlayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erjike.bistu.MusicPlayer.R;

import java.util.List;

public class SearchAllSongAdapter extends RecyclerView.Adapter<SearchAllSongAdapter.ViewHolder> {
    Context mContext;
    List<String> nameList;//可以自己定义类型
    FragmentManager fragmentManager;
    View inputTextView;

    public void setinputTextView(View textView) {
        inputTextView = textView;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    View view;
    TextView songName;
    //TODO 考虑是否让按钮可以实时显示是否在歌单内
    public SearchAllSongAdapter(Context mContext, List<String> nameList) {
        this.mContext = mContext;
        this.nameList=nameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        view = LayoutInflater.from(mContext).inflate(R.layout.search_like_song_list_item, parent, false);//导入视图
        songName=(TextView)view.findViewById(R.id.search_list_music_name);
        ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.setFragmentManager(fragmentManager);

        //TODO 实现对应几个按钮的绑定与功能


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        songName.setText(nameList.get(position));
        songName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputText=(EditText) inputTextView.findViewById(R.id.search_input_text);
                inputText.setText(songName.getText());
            }
        });

    }



    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FragmentManager fragmentManager;
        TextView songName;

        public void setFragmentManager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        public void setSongName(TextView songName) {
            this.songName = songName;
        }


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            songName=(TextView)itemView.findViewById(R.id.search_list_music_name);
        }
    }
}
