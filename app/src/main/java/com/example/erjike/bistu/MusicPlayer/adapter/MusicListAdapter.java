package com.example.erjike.bistu.MusicPlayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.model.MusicListModel;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    RelativeLayout relativeLayout;
    Context mContext;
    TextView musicName;
    TextView musicMaker;
    ImageView musicPlay;
    ImageView musicAdd_or_Delete;
    ImageView musicLike;
    View view;
    private List<MusicListModel> musicList;//音乐列表
    ViewHolder viewHolder;

    public MusicListAdapter(List<MusicListModel> musicList,Context context) {
        this.musicList=musicList;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.music_list_item, parent, false);//导入视图
        viewHolder=new ViewHolder(view);
        relativeLayout=view.findViewById(R.id.list_music_relativeLayout);
        musicName = view.findViewById(R.id.list_music_name);
        musicMaker = view.findViewById(R.id.list_music_maker);
        musicPlay = view.findViewById(R.id.list_music_control);
        musicAdd_or_Delete = view.findViewById(R.id.list_music_move_or_add);
        musicLike = view.findViewById(R.id.list_music_add_to_like);
        musicPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 点击按钮，按钮图片变化，卡片背景变化，播放对应音乐
            }
        });
        musicAdd_or_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 点击按钮，按钮图片变化，删除或者添加到当前歌单
            }
        });
        musicLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 点击按钮，按钮内容变化，添加到我喜欢中，或是从我喜欢中删除
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //将对应内容写入到holder中
        MusicListModel musicListModel=musicList.get(position);
        holder.musicName.setText(musicListModel.getMusicName());
        holder.musicMaker.setText(musicListModel.getMusicMaker());


    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView musicName;
        TextView musicMaker;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            musicName = (TextView) itemView.findViewById(R.id.list_music_name);
            musicMaker = (TextView) itemView.findViewById(R.id.list_music_maker);
        }
    }


}
