package com.example.erjike.bistu.MusicPlayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erjike.bistu.MusicPlayer.PlayingMusicActivity;
import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.filesTool.PlayListSharedPerferences;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;

import java.util.List;
/*
    用于实现显示搜索结果链表
 */

public class SearchAllSongAdapter extends RecyclerView.Adapter<SearchAllSongAdapter.ViewHolder> {
    Context mContext;
    List<SearchMusicModel> nameList;//可以自己定义类型
    FragmentManager fragmentManager;
    View inputTextView;
    String songId;
    View view;
    TextView songName;
    TextView songArtise;
    ImageView songPlay;
    ImageView songAddToList;
    ImageView songImage;


    public void setNameList(List<SearchMusicModel> nameList) {
        this.nameList = nameList;
        this.notifyDataSetChanged();
    }


    //TODO 考虑是否让按钮可以实时显示是否在歌单内
    public SearchAllSongAdapter(Context mContext, List<SearchMusicModel> nameList) {
        this.mContext = mContext;
        this.nameList = nameList;
    }

    @NonNull
    @Override
    public SearchAllSongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        view = LayoutInflater.from(mContext).inflate(R.layout.search_song_list_item, parent, false);//导入视图
        songName = (TextView) view.findViewById(R.id.final_search_list_music_name);
        songArtise = (TextView) view.findViewById(R.id.final_search_list_music_artise);

        songPlay = (ImageView) view.findViewById(R.id.final_search_music_play);
        songAddToList = (ImageView) view.findViewById(R.id.final_list_music_add_to_list);
        songImage = (ImageView) view.findViewById(R.id.final_search_list_music_image);

        SearchAllSongAdapter.ViewHolder viewHolder = new SearchAllSongAdapter.ViewHolder(view);
//        songPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SearchMusicModel music = new SearchMusicModel();
//                music.setMusicId(songId);
//                music.setMusicName(songName.getText().toString());
//                PlayListSharedPerferences.addSongToPlayingList(music, mContext);
//                Toast.makeText(mContext,"已将该歌曲"+music.getMusicName()+"添加到了歌单中",Toast.LENGTH_LONG).show();
//                //实现页面跳转到播放界面与添加到当前列表数据库
//            }
//        });
        songAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 实现弹出选择列表界面，之后将这首歌添加到对应数据库中
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        songName.setText(nameList.get(position).getMusicName());
        songId = nameList.get(position).getMusicId();
        songImage.setImageURI(Uri.parse(nameList.get(position).getImagUri()));
        songArtise.setText(nameList.get(position).getArtiseName());

    }


    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FragmentManager fragmentManager;
        TextView songName;
        TextView songArtise;
        ImageView songPlay;
        ImageView songAddToList;
        ImageView songImage;
        String songId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //注意是itemView，而不是view
            songName = (TextView) itemView.findViewById(R.id.final_search_list_music_name);
            songArtise = (TextView) itemView.findViewById(R.id.final_search_list_music_artise);
            songPlay = (ImageView) itemView.findViewById(R.id.final_search_music_play);
            songAddToList = (ImageView) itemView.findViewById(R.id.final_list_music_add_to_list);
            songImage = (ImageView) itemView.findViewById(R.id.final_search_list_music_image);

            songPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SearchMusicModel music = new SearchMusicModel();
                    music.setMusicId(songId);
                    music.setMusicName(songName.getText().toString());
                    PlayListSharedPerferences.addSongToPlayingList(music, mContext);
                    Toast.makeText(mContext,"已将该歌曲"+music.getMusicName()+"添加到了歌单中",Toast.LENGTH_SHORT).show();
                    //实现页面跳转到播放界面与添加到当前列表数据库
                }
            });

        }
    }
}
