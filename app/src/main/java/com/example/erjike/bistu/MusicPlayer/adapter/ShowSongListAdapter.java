package com.example.erjike.bistu.MusicPlayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.erjike.bistu.MusicPlayer.R;
import com.example.erjike.bistu.MusicPlayer.model.ListNameModel;

import java.util.List;

public class ShowSongListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<ListNameModel> listname;

    public ShowSongListAdapter(Context context, LayoutInflater mInflater, List<ListNameModel> listname) {
        this.context = context;
        this.mInflater = mInflater;
        this.listname = listname;
    }
    //保存二级列表视图类
    private class HolderView{
        String musicId;
        TextView musicName;
        TextView artiseName;
        ImageView musicIcon;

    }

    //刷新数据
    public void flashData(List<ListNameModel> listname){
        this.listname=listname;
        this.notifyDataSetChanged();//刷新

    }
    @Override
    public int getGroupCount() {
        return listname.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listname.get(i).getList().size();
    }

    @Override
    public Object getGroup(int i) {
        return listname.get(i);
    }

   //获取二级列表内容
    @Override
    public Object getChild(int i, int i1) {
        return listname.get(i).getList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
    /*
    用于指定位置相应的组视图
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    private class HolderViewFather{
        TextView title;
        ImageView listIconType;
    }

    //设置一级列表的view
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        HolderViewFather holderViewFather;
        if(view==null){
            holderViewFather=new HolderViewFather();
            //绑定holder
            view=mInflater.inflate(R.layout.list_name_show_item,viewGroup,false);
            holderViewFather.title=(TextView)view.findViewById(R.id.list_name_text);
            holderViewFather.listIconType=(ImageView)view.findViewById(R.id.list_icon_type);



        }
        else {
            holderViewFather=(HolderViewFather)view.getTag();
        }
        //判断箭头向右还是向下
        if(b){
            holderViewFather.listIconType.setImageResource(R.drawable.down);
        }else{
            holderViewFather.listIconType.setImageResource(R.drawable.right);

        }

        holderViewFather.title.setText(listname.get(i).getTitle());
        return view;
    }

    //设置二级列表内容
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        //定义二级列表视图类
      HolderView childrenView;
      if(view==null){
          //绑定布局
          childrenView=new HolderView();
          view=mInflater.inflate(R.layout.music_list_item,viewGroup,false);
          childrenView.musicName=(TextView)view.findViewById(R.id.list_music_name);
          childrenView.artiseName=(TextView)view.findViewById(R.id.list_music_maker);
          childrenView.musicIcon=(ImageView)view.findViewById(R.id.music_icon);
      }
      else {
          childrenView=(HolderView)view.getTag();
      }
      /*
      设置对应的信息
       */
      childrenView.musicId=listname.get(i).getList().get(i1).getMusicId();
      childrenView.musicName.setText(listname.get(i).getList().get(i1).getMusicName());
      childrenView.artiseName.setText(listname.get(i).getList().get(i1).getArtiseName());
      childrenView.musicIcon.setImageURI(Uri.parse(listname.get(i).getList().get(i1).getImagUri()));
      //TODO 按钮对应点击事件??

        return view;
    }
    /*
        当选择节点时调用此方法（二级列表）
     */
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
