package com.example.erjike.bistu.MusicPlayer.netTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.erjike.bistu.MusicPlayer.Gson.GetSearchSong;
import com.example.erjike.bistu.MusicPlayer.Gson.Getlyrics;
import com.example.erjike.bistu.MusicPlayer.Gson.InputLike;
import com.example.erjike.bistu.MusicPlayer.StringTool.StringToCut;
import com.example.erjike.bistu.MusicPlayer.model.SearchMusicModel;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


import static androidx.constraintlayout.widget.Constraints.TAG;

public class ToolsInputLike {
//    private   String hostIP="localhost";
//    private   String hostPort="3000";
//    private   String urlString="http://"+hostIP+":"+hostPort;
//    private String lyricsUrl="/lyric?id=";
//    private String searchsongUrl="/search?keywords=";
//    private String inputLike="/search/suggest?keywords=";

    //实现实时更改文字时的的模糊匹配
    public static List<String> getInputLike(String inputText, Context context) {
        return getInputLike(inputText, null, null, context);
    }

    public static List<String> getInputLike(String inputText, String hostWhat, boolean IpTruePortFalse, Context context) {
        if (IpTruePortFalse) {
            return getInputLike(inputText, hostWhat, null, context);
        } else {
            return getInputLike(inputText, null, hostWhat,context);
        }

    }

    public static List<String> getInputLike(String inputText, String hostIP, String hostPort, final Context context) {
        String mHostIP = "localhost";
        String mHostPort = "3000";
        if (hostIP != null && (!hostIP.equals(""))) {
            mHostIP = hostIP;
            Log.i(TAG, "getInputLike: hostIP=" + hostIP);
        }

        if (hostPort != null && !hostPort.equals("")) {
            mHostPort = hostPort;
            Log.i(TAG, "getInputLike: hostIP=" + hostPort);
        }
        List<String> resultName = new ArrayList<String>();
        String urlString = "http://" + mHostIP + ":" + mHostPort + "/search/suggest?keywords=" + inputText + "&type=mobile";
        Log.i(TAG, "getInputLike: urlString:" + urlString);
        HttpUtil.sendOkHttpRequest(urlString, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("ErJike's getInputLike", "获取联网数据失败！" + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //Log.i(TAG, "onResponse: response.body:"+response.body().string());//注意，这里是string，不是toString,toStirng 打印的是地址
                try {
                    List<String> Names = new ArrayList<String>();
                    Gson gson = new Gson();
                    InputLike translate = gson.fromJson(response.body().string(), InputLike.class);
                    if (translate == null) {
                        //Log.i(TAG, "onResponse: translate=null!");//测试可知不为null
                    }
                    InputLike.ResultBean resultBean = translate.getResult();
                    List<InputLike.ResultBean.AllMatchBean> allMatchBeans = resultBean.getAllMatch();
                    //Log.i(TAG, "onResponse: allMatchBeans size:" + allMatchBeans.size());
                    String logout = "";
                    for (int i = 0; i < allMatchBeans.size(); i++) {
                        InputLike.ResultBean.AllMatchBean allMatchBean = allMatchBeans.get(i);
                        //Log.i(TAG, "onResponse: allMatchBean.getKeyWord+"+allMatchBean.getKeyword());
                        Names.add(new String(allMatchBean.getKeyword()));
                        logout = logout + allMatchBean.getKeyword() + "*";
                    }
                    //Log.i(TAG, "onResponse: logout:"+logout);//logout可以使用
                    SharedPreferences.Editor editor =context.getSharedPreferences("likeSongs",Context.MODE_PRIVATE).edit();
                    editor.clear();
                    editor.putString("list",logout);
                    editor.apply();//一定他要有apply不然数据没有传入

                } catch (Exception e) {
                    Log.e(TAG, "onResponse: error+" + e.getMessage());
                }

            }
        });
        SharedPreferences preferences=context.getSharedPreferences("likeSongs",Context.MODE_PRIVATE);
        List<String> stringsList=new ArrayList<String>();
        String str=preferences.getString("list","");
        //Log.i(TAG, "getInputLike: str is :"+str);//str有值
        stringsList.addAll(StringToCut.CutString(str));

        if (stringsList.size() == 0) {
            Log.i(TAG, "getInputLike: outputList为Null！");//Debug
        }

        return stringsList;
    }


    public static List<SearchMusicModel> getSearchSongs(String inputText) {
        return getSearchSongs(inputText, null, null);
    }

    public static List<SearchMusicModel> getSearchSongs(String inputText, String hostWhat, boolean IpTruePortFalse) {
        if (IpTruePortFalse) {
            return getSearchSongs(inputText, hostWhat, null);
        } else {
            return getSearchSongs(inputText, null, hostWhat);
        }

    }

    public static List<SearchMusicModel> getSearchSongs(String inputText, String hostIP, String hostPort) {
        String mHostIP = "localhost";
        String mHostPort = "3000";
        if (hostIP != null && !hostIP.equals("")) {
            mHostIP = hostIP;
        }

        if (hostPort != null && !hostPort.equals("")) {
            mHostPort = hostPort;
        }
        String urlString = "http://" + mHostIP + ":" + mHostPort + "/search?keywords=" + inputText + "&type=1";
        final List<SearchMusicModel> listSearchMusic = new ArrayList<SearchMusicModel>();
        HttpUtil.sendOkHttpRequest(urlString, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "onFailure: 网络内容接受失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {
                    Gson gson = new Gson();

                    GetSearchSong translate = gson.fromJson(response.body().string(), GetSearchSong.class);

                    GetSearchSong.ResultBean resultBean = translate.getResult();
                    List<GetSearchSong.ResultBean.SongsBean> listSongsBean = resultBean.getSongs();
                    for (GetSearchSong.ResultBean.SongsBean songsBean : listSongsBean) {
                        SearchMusicModel searchMusicModel = new SearchMusicModel();
                        searchMusicModel.setMusicName(songsBean.getName());
                        searchMusicModel.setMusicId(String.valueOf(songsBean.getId()));
                        GetSearchSong.ResultBean.SongsBean.AlbumBean albumBean = songsBean.getAlbum();
                        GetSearchSong.ResultBean.SongsBean.AlbumBean.ArtistBean artistBean = albumBean.getArtist();
                        searchMusicModel.setImagUrl(artistBean.getImg1v1Url());
                        searchMusicModel.setArtiseName(artistBean.getName());
                        listSearchMusic.add(searchMusicModel);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: error+" + e.getMessage());
                }
            }
        });
        return listSearchMusic;
    }

    //获得歌词
    public static String getLyrics(String inputId) {
        return getLyrics(inputId);
    }

    public static String getLyrics(String inputId, String hostWhat, boolean IpTruePortFalse) {
        if (IpTruePortFalse) {
            return getLyrics(inputId, hostWhat, null);
        } else {
            return getLyrics(inputId, null, hostWhat);
        }

    }

    public static String getLyrics(String inputId, String hostIP, String hostPort) {
        String mHostIP = "localhost";
        String mHostPort = "3000";
        if (hostIP != null && !hostIP.equals("")) {
            mHostIP = hostIP;
        }

        if (hostPort != null && !hostPort.equals("")) {
            mHostPort = hostPort;
        }
        String urlString = "http://" + mHostIP + ":" + mHostPort + "/lyric?id=" + inputId;
        final String[] output = {""};
        HttpUtil.sendOkHttpRequest(urlString, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "onFailure: 获得失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    Gson gson = new Gson();
                    Getlyrics translate = gson.fromJson(response.body().string(), Getlyrics.class);
                    Getlyrics.LrcBean lrcBean = translate.getLrc();
                    output[0] = lrcBean.getLyric();

                } catch (Exception e) {
                    Log.e(TAG, "onResponse: error+" + e.getMessage());
                }
            }
        });
        return output[0];
    }
}
