# android 音乐播放器实验

--------------------

GitHub项目地址:https://github.com/wa6930/MusicPlayerByBISTU

## 1.实验目的

1.了解Android Service的基本概念；

2.理解并掌握使Service使用方法;

3.掌握MediaPlayer的使用方法。

4.实现属性动画

5.尝试引用第三方开源module

## 2.实验要求

### **必做**（均完成）

* 播放
* 暂停
* 时间轴
* 拖动到任何位置
* 上一首
* 下一首
* 歌单内容增减
* 歌单随机播放
* 歌单按序播放

### 选做（仅卡拉ok未实现）

* 随进度显示歌词
* 自动下载歌词
* 多个歌单
* 音乐效果
* 音乐特效
* ~~卡拉ok(未实现)~~
* 交互评价

## 3.实验内容

### 3.1.实验原理

本实验的歌曲与歌词，基于Node.js的网易云API实现。该开源API的GitHub地址为：https://github.com/Binaryify/NeteaseCloudMusicApi

配置好运行环境后，使用该API的默认设置运行，手机连接其本地ip

![image-20191023115024301](/Users/wangxiao/Library/Application Support/typora-user-images/image-20191023115024301.png)

本实验中所有的搜索、音乐、歌词均为从API中获得

###3.2.各文件功能说明

<img src="/Users/wangxiao/Library/Application Support/typora-user-images/image-20191023142319822.png" alt="image-20191023142319822" style="zoom:50%;" />

com.example.erjike.bistu.MusicPlayer

> adapter(书写各种adapter)
>
> > PlayingListAdapter(播放列表adapter)
> >
> > SearchAllSongAdapter(搜索结果adapter)
> >
> > SearchLikeSongAdapter(相似结果推荐列表adapter)
> >
> > ShowSongListAdapter(歌单显示adapter)

	> db(SQlite类的数据库与工具)
	>
	> > ListNameDBHelper(歌单一级数据库)
	> >
	> > PlayListDBHelper(当前歌单的存储)
	> >
	> > SearchAllSongLIstDBHelper(搜索本地存储)
	> >
	> > SongListDBHelper(存储本地歌单)

> filesTool(SharedPerferences工具类)
>
> > CommentSharedPerferences
> >
> > PlayListSharedPerferences

> Gson（利用Gson插件自动解析json所获得的model类）
>
> > Getlyrics(Gson自动生成获取歌词j)
> >
> > GetMp3Url(Gson获取音乐url)
> >
> > GetSearchSong(Gson获取搜索结果)
> >
> > InputLike(Gson获取相似歌曲)

> matrix(绘制属性动画)
>
> > LoadingDiscView(动画细节设置)
> >
> > RefreshHandle(相应handle)

> model(存储数据)
>
> > ListNameModel（歌单）
> >
> > SearchMusicModel(音乐)

> netTools(网络工具类)
>
> > ~~CommonTool（未使用）~~
> >
> > HttpUtil（实现okHttp3）
> >
> > ToolsInputLike(调用HttpUtil发送网络请求)

> Service(服务类)
>
> > MusicService(自定服务)

> StringTool(字符串工具类)
>
> > StringToCut(字符串切割)

> ui(BottomNavigationView绑定的三个对应视图)
>
> > ~~like(我喜欢的音乐，未使用)~~
> >
> > > ~~LikeSongsFragment(未使用)~~
> > > ~~LikeSongsViewModel(未使用)~~
> >
> > list(当前播放列表)
> >
> > > SongListFragment（对应Fragment）
> > > ~~SongListViewModel（用于数据传递，未使用）~~
> >
> > show（歌单列表）
> >
> > > ShowListFragment(显示歌单列表二重菜单)
> > > ~~ShowListViewModel(未使用)~~

>CommentActivity（评论页面）
>MainActivity（主页面）
>PlayingMusicActivity（播放页面）
>SearchedActivity（搜索结果页面）
>SearchSongActivity（搜索页面）

### 3.3.需求分析

作为一个网络音乐播放器，用户应该可以搜索音乐、播放音乐。输入歌名的时候应该有歌名推荐，该软件应该可以拥有多个歌单，用户可以选择往任意歌单中添加歌曲。歌单的内容应当是存放于本地中的，并且点击歌单可以将歌曲添加到当前播放列表中。歌单理论上应当可以添加，并且播放页面的歌词应当紧跟进度条并且可以动态改变。

###3.4.系统设计

####3.4.1界面设计

根据上述需求，设计大体界面结构，设计图如下：

播放列表如图：

![设计=音乐播放器-2](/Users/wangxiao/Downloads/设计=音乐播放器-2.jpg)

设计后的播放页面如下：

<img src="/Users/wangxiao/Library/Application Support/typora-user-images/image-20191023134909412.png" alt="image-20191023134909412" style="zoom:30%;" />

播放界面设计如图：

![设计=音乐播放器-3](/Users/wangxiao/Downloads/设计=音乐播放器-3.jpg)设计后的播放界面如下：

<img src="/Users/wangxiao/Library/Application Support/typora-user-images/image-20191023135008893.png" alt="image-20191023135008893" style="zoom:30%;" />

#### 3.4.2底层逻辑设计

存储结构设计：

底层数据库结构如图，读取后的每一项加载为SearchMusicModel类的数据，其中歌曲id为主键，歌单名主键为歌单名。歌单名还是每个歌单的文件名。

![type](/Users/wangxiao/Downloads/type.png)

数据库最后使用的只有歌曲id和歌名，其他的均为默认值或者空值。并且播放列表最后使用了SharedPererence实现。

其实现，ToolsInputLike工具类部分代码如下：

```java
public static List<SearchMusicModel> loadRSharedPerference(Context context) {
    SharedPreferences sp = context.getSharedPreferences("RplayingList", Context.MODE_PRIVATE);
    List<SearchMusicModel> ouput = new ArrayList<>();
    int size = sp.getInt("Rsize", 0);
    for (int i = 0; i < size; i++) {
        SearchMusicModel musicModel = new SearchMusicModel();
        musicModel.setMusicId(sp.getString("r_id_" + i, ""));
        musicModel.setMusicName(sp.getString("r_name_" + i, ""));
        ouput.add(musicModel);
    }

    return ouput;

}

public static void writeRSharedPerference(List<SearchMusicModel> playingList, Context context) {
    SharedPreferences sp = context.getSharedPreferences("RplayingList", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.clear();
    editor.putInt("Rsize", playingList.size());//存入歌单长度
    int i = 0;
    for (SearchMusicModel music : playingList) {
        editor.putString("r_id_" + i, music.getMusicId());
        editor.putString("r_name_" + i, music.getMusicName());
        i++;
    }
    editor.commit();
    //全部添加
    //实现将当前右侧链表写入到文件中的操作
    // 修改存储于读取方法，从而保证文件正常
}
```

同理，评论的方式也是用类似的工具类来完成本地化存储的，文件名为歌曲id。

### 3.5.编码实现

##### 3.5.1.自定Service实现网络音频播放

在申请Service的Activity或者Fragment中，即SongListFragment中申请代码如下：

```java
.........//省略
Intent intentService = new Intent(getActivity(), MusicService.class);
Log.i(TAG, "init: music.getMusicId():" + music.getMusicId());
final String url = ToolsInputLike.getMp3Url(music.getMusicId(), MainActivity.HOST_IP, true, getContext());//绑定相关元素
Log.i(TAG, "init: url+" + url);
intentService.putExtra("MP3url", url);
//intentService.putExtra("MP3url",url);
getActivity().startService(intentService);
final ServiceConnection myConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        myBinder = (MusicService.MyBinder) iBinder;//绑定成功
        myBinder.setSeekBar(musicSeekBar);//绑定进度条
        myBinder.setContext(getContext());
        //加载成功
        new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPDATE_PROGRESS;
                handler.sendMessage(message);
            }
        }.run();//多线程更新界面

        adapter.setPlayImage(musicPlay);
        adapter.setShowMusicName(musicName);
        adapter.setMyBinder(myBinder);


    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.e("MainActivity", "onServiceDisconnected: 绑定失败。");
        throw new NullPointerException();//服务绑定失败后，自动报错退出
    }
};
......//省略
```

当绑定成功后，调用Service的onBind方法，如果想要实现播放功能，只需重写此处代码即可。

MusicService关键代码如下：

```java
@Override
public IBinder onBind(Intent intent) {//绑定
myBinder.mediaPlayer=new MediaPlayer();
            myBinder.mediaPlayer.reset();
            Log.i(TAG, "onBind: url:"+url);//第一次intent传入url
            myBinder.mediaPlayer.setDataSource(url);

            myBinder.mediaPlayer.setLooping(false);//禁止单曲循环
            myBinder.mediaPlayer.prepareAsync();//网络音乐调用异步方法
            
………………//代码省略

}
```

之后，通过MusicService中的重写的Binder方法来实现音乐的播放暂停即可。

#####3.5.2.handle的使用

随着音乐的播放，进度条应与音乐一同增长，且播放界面歌词应当随着音乐进度而自动滚动。当音乐播放时，播放界面的唱片仿照网易云的黑胶唱片一样旋转。而这些都需要通过handle来实现。

PlayingMusicActivity的handle如下，其实现了同步进度条、歌词的作用。

```java
private Handler handler = new Handler() {
    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case UPDATE_PROGRESS:
                //实现每隔500毫秒更新一次界面
                try {
                    int positon = myBinder.getCurrenPostion();
                    //毫秒为单位的时间
                    musicSeekBar.setProgress(positon);
                    handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500);
                }catch (Exception e){
                    Log.e(TAG, "handleMessage: e"+e.getMessage() );
                }


                //实现更新进度条的操作
                //步骤1.更新musicSeekBar
                break;
            default:
        }

    }
};
```

同理，控制唱片旋转的自建handle，RefreshHandle如下：

```java
public class RefreshHandle extends Handler {
    LoadingDiscView loadingDiscView;

    public RefreshHandle(LoadingDiscView loadingDiscView) {
        this.loadingDiscView = loadingDiscView;
        loadingDiscView.setRefreshHandle(this);
    }

    public void run() {
        loadingDiscView.update();
        removeCallbacksAndMessages(null);
        sendEmptyMessageDelayed(0,65);
    }

    public void stop() {
        removeCallbacksAndMessages(null);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case 0:
                run();
                break;
        }
    }
}
```

##### 3.5.3.开源歌词滚动插件的使用

该项目的GitHub地址为：https://github.com/wangchenyan/lrcview

在build.gradle中添加其Gradle，implementation 'me.wcy:lrcview:2.1.0'之后便导入成功

显示歌词的布局与方法均为该开源项目封装。

```xml
<me.wcy.lrcview.LrcView
    android:id="@+id/playing_music_lyrics"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

之后将网络工具类获得的String型歌词导入到该控件中即可。

```java
lyricsString=ToolsInputLike.getLyrics(rSongList.get(0).getMusicId(),MainActivity.HOST_IP,true,PlayingMusicActivity.this);
Log.i(TAG, "UpUI: lyrics:"+lyricsString);
//以下内容均由第三方module实现,@https://github.com/wangchenyan/lrcview
lyrics.loadLrc(lyricsString);
```

### 3.6系统测试

#### 3.6.1.搜索内容测试

点击添加歌曲-输入浮夸-点击添加播放列表按钮-点击添加歌单按钮-返回播放列表界面

测试成功，歌曲成功的添加到了播放列表与歌单中。

<img src="/Users/wangxiao/Library/Application Support/typora-user-images/image-20191023153856676.png" alt="image-20191023153856676" style="zoom:30%;" />

<img src="/Users/wangxiao/Library/Application Support/typora-user-images/image-20191023153948595.png" alt="image-20191023153948595" style="zoom:30%;" />

#### 3.6.2.播放内容测试

1.点击任意歌曲，歌曲应当自动播放

测试失败，界面显示正常但无法自动播放，个人猜测是因为网络请求为异步请求，而自动播放代码执行时mediaPlay并没有就绪，截图不再展示。

解决方法：稍后等待歌曲加载成功后，重新点击播放按钮即可。

2.点击下一首/上一首，歌曲若为播放状态则自动播放下一首/上一首

测试失败，原理同上，截图不再展示。

3.点击下一首/上一首，歌曲若为暂停状态则自动载入下一首/上一首

测试成功，截图不再展示。

4.随机播放与顺序播放模式下的上一首与下一首

测试成功，截图不再展示

5.播放界面的动画

测试成功，动画正常播放

<img src="/Users/wangxiao/Library/Application Support/typora-user-images/image-20191023155521939.png" alt="image-20191023155521939" style="zoom:30%;" />

## 4.问题分析

本次实验中，我解决了很多问题，也遇到了很多问题。比较困难的，是网络异步信息如何与本地内容同步的问题。当网速较慢的时候，该软件要等很久才能正常初始化将要播放的内容，包括歌词音乐资源等很多内容有时会因为未成功载入而显示错误。虽然可以完成本次实验，但是对于这方面的内容，我还应当更加深入的学习。

## 5.心得体会

本次实验，进一步培养了我实现一个工程的操作能力，以及程序设计能力。本次的实验中，我尽量规范格式，在大框架设计好的前提下，一步一步的完成相对应的小功能。在实验中，我遇到了非常多的困难，花费了大量的时间调试。

这次实验极大的震撼了我，即使是一些很小的功能与交互背后，也有可能涵盖了很高的代码量。如何快速完成任务的同时，在有限的时间内尽量增加程序的鲁棒性与减少程序的bug几率，也是这次实验的挑战。

一方面我既要学习别的科目，又要抽出大量的课下时间完成该实验。这进一步锻炼了我的编码能力。

















