package com.example.erjike.bistu.MusicPlayer.matrix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.view.View;

import com.example.erjike.bistu.MusicPlayer.R;

import java.io.InputStream;

//使用Matrix旋转图片模拟碟片加载过程
public class LoadingDiscView extends View {
    private RefreshHandle refreshHandle;//TODO 自写自建类
    private Context context;
    /** 用于旋转的bitmap*/
    private Bitmap m_bmp_disc=null;
    private Matrix m_matrix_disc=new Matrix();
    /** 用于展现高亮背景的bitmap*/
    private Bitmap m_bmp_light=null;
    private Matrix m_matrix_light=new Matrix();
    /**Paint滤波器*/
    private PaintFlagsDrawFilter mSetfil=null;
    /*自声明一个画笔*/
    private Paint mypaint=null;
    /*图像缩放比例*/
    private float m_scale=2.0f;
    /*图像旋转速度*/
    private float m_disc_rot_speed=0;
    /*图像旋转状态,1为旋转，0为暂停*/
    private int m_state_play=1;
    /*图像旋转的最大速度*/
    private float m_disc_max=20f;


    public LoadingDiscView(Context context) {
        super(context);
        this.context=context;//常量赋值
        mSetfil=new PaintFlagsDrawFilter(0,Paint.FILTER_BITMAP_FLAG);//设置画布无锯齿
        initBitmap();

    }

    @SuppressLint("ResourceType")
    private boolean initBitmap() {
        mypaint=new Paint();
        mypaint.setAntiAlias(true);//线条锐化

        Resources res=context.getResources();
        InputStream is =res.openRawResource(R.drawable.loading_disc);
        m_bmp_disc=BitmapFactory.decodeStream(is);
        matrixPostTranslate(m_matrix_disc,m_bmp_disc);

        is=res.openRawResource(R.drawable.loading_light);
        m_bmp_light=BitmapFactory.decodeStream(is);
        matrixPostTranslate(m_matrix_light,m_bmp_light);
        return true;
    }

    private void matrixPostTranslate(Matrix matrix, Bitmap bitmap) {
        //TODO
        int tmp_width=bitmap.getWidth();
        int tmp_height=bitmap.getHeight();
        matrix.postTranslate(-tmp_width/2,-tmp_height/2);//设定平移位置
        matrix.postScale(m_scale,m_scale);//设置缩放比例
        matrix.postTranslate(123*m_scale,146*m_scale);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mSetfil);//设置Paint滤波器
        canvas.drawBitmap(m_bmp_disc,m_matrix_disc,mypaint);
        canvas.drawBitmap(m_bmp_light,m_matrix_light,mypaint);//绘制二者
    }
    public  void update(){
        //播放时
        if(m_disc_rot_speed>0.01||m_state_play==1){
            if(m_state_play==1&&m_disc_rot_speed<m_disc_max){
                m_disc_rot_speed+=(m_disc_max+0.5f-m_disc_rot_speed)/30;//有个变减速的加速到最大速度的过程
            }
            else if(m_disc_rot_speed>0.1){
                m_disc_rot_speed-=(m_disc_rot_speed)/40;
            }
            m_matrix_disc.postRotate(m_disc_rot_speed,123*m_scale,146*m_scale);//旋转
            invalidate();
        }
    }
    public void onRause(){
        refreshHandle.stop();
    }
    public void onResum(){
        refreshHandle.run();
    }

    public void setRefreshHandle(RefreshHandle refreshHandle) {
        this.refreshHandle=refreshHandle;
    }
}
