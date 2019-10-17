package com.example.erjike.bistu.MusicPlayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;

import java.util.List;

public class PlayingListAdapter extends RecyclerView.Adapter<PlayingListAdapter.ViewHolder> {
    Context mContext;
    List<SearchMusicModel> rList;
    List<SearchMusicModel> lList;
    View view;
    String songId;
    TextView songName;
    ImageView toPlayActivity;
    LinearLayout linearLayout;

    public void setrList(List<SearchMusicModel> rList) {
        this.rList = rList;
        this.notifyDataSetChanged();//更新
    }

    public void setlList(List<SearchMusicModel> RList) {
        this.lList = RList;
        this.notifyDataSetChanged();//更新
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        view= LayoutInflater.from(mContext).inflate(R.layout.list_name_show_item,parent,false);
        songName=(TextView)view.findViewById(R.id.list_name_text);
        toPlayActivity=(ImageView)view.findViewById(R.id.list_icon_type);
        linearLayout=(LinearLayout)view.findViewById(R.id.list_linearLayout);
        toPlayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 跳转到音乐播放界面
            }
        });
        PlayingListAdapter.ViewHolder viewHolder=new PlayingListAdapter.ViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position+1!=lList.size()){
            linearLayout.setBackgroundColor(Color.WHITE);
            //除了正在播放的音乐，其他均为白色
        }
        if(position+1>lList.size()){
            int rPosition=position-lList.size();
            songId=rList.get(rPosition).getMusicId();
            songName.setText(rList.get(rPosition).getMusicName());
            songId=rList.get(rPosition).getMusicId();
        }
        else {
            songId=lList.get(position).getMusicId();
            songName.setText(lList.get(position).getMusicName());
            songId=lList.get(position).getMusicId();


        }

    }

    @Override
    public int getItemCount() {
        return rList.size()+lList.size();//返回二者总长度
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String musicId;
        TextView songName;
        ImageView toPlayActivity;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songName=(TextView)itemView.findViewById(R.id.list_name_text);
            toPlayActivity=(ImageView)itemView.findViewById(R.id.list_icon_type);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.list_linearLayout);
        }
    }
}
