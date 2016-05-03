package com.wangdiaozhu.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/13.
 */
public class ToastUtils {

    public static void showToast(Context ctx,String text){

        Toast.makeText(ctx,text,Toast.LENGTH_SHORT).show();

    }
}
