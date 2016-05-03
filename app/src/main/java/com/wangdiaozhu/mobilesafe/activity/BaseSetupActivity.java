package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/4/17.
 */
public abstract class BaseSetupActivity extends Activity {

    private GestureDetector mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

              if(Math.abs(e2.getRawY()-e1.getRawY())>100){

                  ToastUtils.showToast(getApplicationContext(),"不能这样划哦");
                        return true;
              }
                if(Math.abs(velocityX)<100){
                    ToastUtils.showToast(getApplicationContext(),"划的太慢哦");
                    return true;
                }
                if(e2.getRawX()-e1.getRawX()>200){

                    showPrevious();
                    return  true;
                }
                if(e1.getRawX()-e2.getRawX()>200){

                    showNext();

                    return  true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public  void previous(View v){

        showPrevious();
    }
    public void next(View v){
        showNext();
    }
    public abstract  void showPrevious();


    public abstract  void showNext()  ;


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
