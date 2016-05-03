package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wangdiaozhu.mobilesafe.R;

/**
 * Created by Administrator on 2016/4/13.
 */
public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }



    @Override
    public void showPrevious() {

    }

    @Override
    public void showNext() {
        startActivity(new Intent(this,Setup2Activity.class));

        finish();
        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
    }
}
