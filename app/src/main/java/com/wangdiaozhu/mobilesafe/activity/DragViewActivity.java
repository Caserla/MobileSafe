package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;

/**
 * Created by Administrator on 2016/5/4.
 */
public class DragViewActivity extends Activity {

    private ImageView iv_drag;
    private int startX;
    private int startY;
    private int mScreenWidth;
    private int mScreenHeight;
    private TextView tv_bottom;
    private TextView tv_top;
    private  long[] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        tv_top = (TextView) findViewById(R.id.tv_top);
        iv_drag = (ImageView) findViewById(R.id.iv_drag);
        int  lastX =  PrefUtils.getInt("lastX",0,this);
      int lastY =  PrefUtils.getInt("lastY",0,this);
        if(lastY>mScreenHeight/2){


            tv_top.setVisibility(View.VISIBLE);
            tv_bottom.setVisibility(View.INVISIBLE);
        }else {
            tv_top.setVisibility(View.INVISIBLE);
            tv_bottom.setVisibility(View.VISIBLE);

        }

//        iv_drag.layout(lastX,lastY,lastX+iv_drag.getWidth(),lastY+iv_drag.getHeight());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_drag.getLayoutParams();
                   params.topMargin = lastY;
                  params.leftMargin = lastX;


        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:
//                      获得起始坐标点
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
//                        获得移动后坐标点
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
//                       计算偏移量
                        int dx = endX-startX;
                        int dy = endY - startY;
//                      根据偏移量更新位置
                        int l = iv_drag.getLeft() +dx;
                        int r = iv_drag.getRight() +dx;
                        int t = iv_drag.getTop() + dy;
                        int b = iv_drag.getBottom()+ dy;

                        if(l<0 || r>mScreenWidth){
                            return true;

                        }

                        if(t<0 || b>mScreenHeight-40){

                            return true;
                        }
                        if(t>mScreenHeight/2){


                            tv_top.setVisibility(View.VISIBLE);
                              tv_bottom.setVisibility(View.INVISIBLE);
                        }else {
                            tv_top.setVisibility(View.INVISIBLE);
                            tv_bottom.setVisibility(View.VISIBLE);

                        }
                         iv_drag.layout(l,t,r,b);
//                        重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:
//                       保存当前位置
                        PrefUtils.putInt("lastX",iv_drag.getLeft(), getApplicationContext());
                        PrefUtils.putInt("lastY",iv_drag.getTop(),getApplicationContext());



                        break;

                }
                return false;
            }
        });

        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if(SystemClock.uptimeMillis()- mHits[0] <=500){
//                  双击居中
                  iv_drag.layout((mScreenWidth/2-iv_drag.getWidth()/2),iv_drag.getTop(),(mScreenHeight/2
                          +iv_drag.getHeight()/2) ,iv_drag.getBottom());

                }
            }
        });
    }
}
