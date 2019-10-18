package com.example.erjike.bistu.MusicPlayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erjike.bistu.MusicPlayer.MainActivity;
import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.Service.MusicService;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;
import com.example.erjike.bistu.MusicPlayer.netTools.ToolsInputLike;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PlayingListAdapter extends RecyclerView.Adapter<PlayingListAdapter.ViewHolder> {
    Context mContext;
    List<SearchMusicModel> rList;
    List<SearchMusicModel> lList;
    View view;
    String songId;
    TextView songName;
    ImageView toPlayActivity;
    LinearLayout linearLayout;
    MusicService.MyBinder myBinder;
    TextView showMusicName;
    ImageView playImage;

    public void setShowMusicName(TextView showMusicName) {
        this.showMusicName = showMusicName;
    }

    public void setPlayImage(ImageView playImage) {
        this.playImage = playImage;
    }

    public void setMyBinder(MusicService.MyBinder myBinder) {
        this.myBinder = myBinder;
    }

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //if(position!=lList.size()){
            linearLayout.setBackgroundColor(Color.WHITE);//暂时不考虑播放高亮
            //除了正在播放的音乐，其他均为白色
        //}
        if(position+1>lList.size()){
            int rPosition=position-lList.size();
            holder.musicId=rList.get(rPosition).getMusicId();
            holder.songName.setText(rList.get(rPosition).getMusicName());
            //holder.musicMaker=rList.get(rPosition).getArtiseName();
            //holder.musicId=rList.get(rPosition).getMusicId();
        }
        else {
            holder.musicId=lList.get(position).getMusicId();
            holder.songName.setText(lList.get(position).getMusicName());
            //holder.musicId=lList.get(position).getMusicId();
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myBinder!=null){
                   //TODO 调用赋值与播放操作
                    myBinder.pause();
                    myBinder.setNewMediaPlayer(holder.musicId);
                    String url=ToolsInputLike.getMp3Url(holder.musicId, MainActivity.HOST_IP,true,mContext);
                    if(url.equals("")||url==null){
                        Log.i(TAG, "onClick: 不进行界面更改");
                    }else {
                        showMusicName.setText(holder.songName.getText().toString());
                        playImage.setImageResource(R.drawable.pause);
                    }


                }
                else{
                    Log.e(TAG, "onClick: 在setOnClickListener中binder为空");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return rList.size()+lList.size();//返回二者总长度
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String musicMaker;
        String musicId;
        TextView songName;
        ImageView toPlayActivity;
        LinearLayout linearLayout;
        MusicService.MyBinder myBinder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songName=(TextView)itemView.findViewById(R.id.list_name_text);
            toPlayActivity=(ImageView)itemView.findViewById(R.id.list_icon_type);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.list_linearLayout);
        }
    }
}
