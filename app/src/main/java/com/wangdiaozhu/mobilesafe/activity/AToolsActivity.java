package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.wangdiaozhu.mobilesafe.R;

/**
 * Created by Administrator on 2016/5/2.
 */
public class AToolsActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public  void addressQuery(View v){

        startActivity(new Intent(getApplicationContext(),AddressQueryActivity.class));

    }
}
