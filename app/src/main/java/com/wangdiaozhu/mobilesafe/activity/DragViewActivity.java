package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.wangdiaozhu.mobilesafe.R;

/**
 * Created by Administrator on 2016/5/4.
 */
public class DragViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_drag_view);
    }
}
