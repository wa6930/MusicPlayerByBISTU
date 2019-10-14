package com.example.erjike.bistu.MusicPlayer.StringTool;

import android.util.Log;


import java.util.ArrayList;

public class StringToCut {
    private static String TAG = "ErJike's StringToCut";

    public static ArrayList<String> CutString(String str) {
        //Log.i(TAG, "CutStringToSentenceList str:"+str);
        ArrayList<String> sentencesList = new ArrayList<String>();
        String[] strArr = str.split("\\*");
        String sentence = "";

        //Log.i(TAG, "CutString: strArr.length:"+strArr.length+"\n+"+str);
        for (int i = 0; i < strArr.length; i++) {

            sentence=strArr[i];
            if(!sentence.equals("")&&sentence!=null){
                sentencesList.add(sentence);
                //Log.i(TAG, "CutString: "+sentence);测试正常
            }

        }

        return sentencesList;
    }

}
