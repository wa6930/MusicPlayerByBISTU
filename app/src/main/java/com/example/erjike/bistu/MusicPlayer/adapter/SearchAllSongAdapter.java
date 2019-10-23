package com.example.erjike.bistu.MusicPlayer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erjike.bistu.MusicPlayer.PlayingMusicActivity;
import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.db.ListNameDBHelper;
import com.example.erjike.bistu.MusicPlayer.db.SongListDBHelper;
import com.example.erjike.bistu.MusicPlayer.filesTool.PlayListSharedPerferences;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;

import java.util.ArrayList;
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


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.songName.setText(nameList.get(position).getMusicName());
        holder.songId = nameList.get(position).getMusicId();
        holder.songImage.setImageURI(Uri.parse(nameList.get(position).getImagUri()));
        holder.songArtise.setText(nameList.get(position).getArtiseName());

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
                    Toast.makeText(mContext, "已将该歌曲" + music.getMusicName() + "添加到了歌单中", Toast.LENGTH_SHORT).show();
                    //实现页面跳转到播放界面与添加到当前列表数据库
                }
            });
            songAddToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO 实现弹出选择列表界面，之后将这首歌添加到对应数据库中

                    final List<String> listName=new ArrayList<>();
                    final ListNameDBHelper dbHelper=new ListNameDBHelper(mContext,"listName.db",null,1);//context不同不会影响本地地址变化
                    SQLiteDatabase db=dbHelper.getReadableDatabase();
                    listName.addAll(ListNameDBHelper.queryAll(db,mContext));
                    dbHelper.close();
                    db.close();//赋值完毕后关闭
                    //TODO 实现知道表名之后的数据添加
                    final AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                    View view1=LayoutInflater.from(mContext).inflate(R.layout.add_to_list_dialoge,null);
                    dialog.setTitle("添加到歌单");
                    dialog.setView(view1);
                    ListView listShow=(ListView) view1.findViewById(R.id.add_to_list_RecyclerView);
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(mContext,R.layout.support_simple_spinner_dropdown_item,listName);//使用的视图是系统自带的adapter默认视图
                    listShow.setAdapter(adapter);
                    listShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //点击添加
                            SongListDBHelper dbHelper1=new SongListDBHelper(mContext,listName.get(i),null,1);
                            SQLiteDatabase db1=dbHelper1.getWritableDatabase();
                            String musicId=songId;
                            String musicName=songName.getText().toString();
                            String artise=songArtise.getText().toString();
                            SearchMusicModel music=new SearchMusicModel();
                            music.setMusicId(musicId);
                            music.setMusicName(musicName);
                            music.setArtiseName(artise);
                            music.setImagUri("");
                            SongListDBHelper.insertSong(db1,music,mContext);
                            dbHelper1.close();
                            db1.close();
                            Toast.makeText(mContext,"已经将"+musicName+"添加到了"+listName.get(i)+"表单中！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.setPositiveButton("完成", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //无效果
                        }
                    });
                    dialog.show();



                }
            });

        }
    }
}
