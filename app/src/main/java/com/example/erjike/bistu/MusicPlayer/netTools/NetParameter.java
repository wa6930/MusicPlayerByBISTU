package com.example.erjike.bistu.MusicPlayer.netTools;

public class NetParameter {
    private   String hostIP="localhost";
    private   String hostPort="3000";
    private   String urlString="http://"+hostIP+":"+hostPort;
    private String lyricsUrl="/lyric?id=";
    private String searchsongUrl="/search?keywords=";
    private String inputLike="search/suggest?keywords=";

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public String getHostIP() {
        return hostIP;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }


}
