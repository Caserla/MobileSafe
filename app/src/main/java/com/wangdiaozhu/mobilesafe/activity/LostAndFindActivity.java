package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class LostAndFindActivity extends Activity {

    private TextView tv_safe_home;
    private ImageView iv_lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          boolean isconfig =  PrefUtils.getBoolean("isconfig",false,this);
               if(!isconfig){
                  startActivity(new Intent(this,Setup1Activity.class));
                   finish();


               }else {setContentView(R.layout.activity_lost_and_find);

                   tv_safe_home = (TextView) findViewById(R.id.tv_safe_home);
                   iv_lock = (ImageView) findViewById(R.id.iv_lock);
               String phone =    PrefUtils.getString("save_phone","",this);
                   tv_safe_home.setText(phone);
                 boolean protect =   PrefUtils.getBoolean("protect",false,this);
                   if(protect){
                       iv_lock.setImageResource(R.drawable.lock);
                   }else {

                       iv_lock.setImageResource(R.drawable.unlock);
                   }
               }

    }

    public void reSetup(View v){

        startActivity(new Intent(this,Setup1Activity.class));
         finish();
    }
}
