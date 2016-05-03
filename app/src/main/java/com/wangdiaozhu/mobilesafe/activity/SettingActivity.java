package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.service.AddressService;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;
import com.wangdiaozhu.mobilesafe.utils.ServiceStatusService;
import com.wangdiaozhu.mobilesafe.view.SettingItemClickView;
import com.wangdiaozhu.mobilesafe.view.SettingItemView;

/**
 * Created by Administrator on 2016/4/12.
 */
public class SettingActivity extends Activity {
    private SettingItemView sivUpdate;
    private SettingItemView sivAddress;
    private SettingItemClickView sicStyle;
    private String[] items = new String[]{"半透明","活力橙","卫士篮","金属灰","苹果绿"};;
    // private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // sp = getSharedPreferences("config", MODE_PRIVATE);
        initUpdate();
        initAddress();
        initAddressStyle();
    }

    private void initAddressStyle() {

        sicStyle = (SettingItemClickView) findViewById(R.id.sic_style);
        sicStyle.setTitle("归属地提示框风格");
        int style = PrefUtils.getInt("address_style",0,this);
        sicStyle.setDesc(items[style]);
        sicStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoiseDialog();
            }
        });
    }

    private void showChoiseDialog() {
        int style = PrefUtils.getInt("address_style",0,this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("归属地提示框风格");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                PrefUtils.putInt("address_style",which,getApplicationContext());
                dialog.dismiss();
                sicStyle.setDesc(items[which]);//更新描述
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void initAddress() {

        sivAddress = (SettingItemView) findViewById(R.id.siv_address);
        sivAddress.setTitle("电话归属地显示设置");

               boolean serviceRunning = ServiceStatusService.isServiceRunning("com.wangdiaozhu.mobilesafe.service.AddressService",this);

                 if(!serviceRunning){

                     sivAddress.setDesc("归属地显示设置已经关闭");

                 }else {

                     sivAddress.setChecked(serviceRunning);
                     sivAddress.setDesc("归属地显示设置已经开启");
                 }




        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent service = new Intent(getApplicationContext(), AddressService.class);

                if(sivAddress.isChecked()){

                    sivAddress.setChecked(false);
                    sivAddress.setDesc("归属地显示设置已经关闭");
                    stopService(service);//关闭归属地显示服务
                }else {

                    sivAddress.setChecked(true);
                    sivAddress.setDesc("归属地显示设置已经开启");
                    startService(service);//开启归属地显示服务
                }
            }
        });

    }

    /**
     * 初始化自动更新设置
     */
    private void initUpdate() {
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
         sivUpdate.setTitle("自动更新设置");

        boolean autoUpdate = PrefUtils.getBoolean("auto_update", true, this);
         if (autoUpdate) {

         sivUpdate.setChecked(true);
             sivUpdate.setDesc("自动更新已开启");
         } else {

         sivUpdate.setChecked(false);
             sivUpdate.setDesc("自动更新已关闭");
         }



        sivUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sivUpdate.isChecked()) {
                    sivUpdate.setChecked(false);
                     sivUpdate.setDesc("自动更新已关闭");
                    // sp.edit().putBoolean("auto_update", false).commit();
                    PrefUtils.putBoolean("auto_update", false,
                            getApplicationContext());
                } else {
                    sivUpdate.setChecked(true);
                     sivUpdate.setDesc("自动更新已开启");
                    // sp.edit().putBoolean("auto_update", true).commit();
                    PrefUtils.putBoolean("auto_update", true,
                            getApplicationContext());
                }
            }
        });
    }
    }



