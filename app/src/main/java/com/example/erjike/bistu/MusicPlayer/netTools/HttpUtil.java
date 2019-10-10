package com.example.erjike.bistu.MusicPlayer.netTools;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    /**
     * 使用OkHttp网络工具发送网络请求
     * */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){

        //创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        //创建Request对象，装上地址
        Request request = new Request.Builder().url(address).build();

        //发送请求，返回数据需要自己重写回调方法
        client.newCall(request).enqueue(callback);

    }

}