package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.receiver.AdminReceiver;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_protect;
    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cb_protect = (CheckBox) findViewById(R.id.cb_protect);
        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
        boolean protect = PrefUtils.getBoolean("protect",false,getApplicationContext());
        if(protect){

            cb_protect.setChecked(true);
            cb_protect.setText("防盗保护已经开启");
        }else {cb_protect.setChecked(false);
                cb_protect.setText("您没有开启防盗保护");
        }
        cb_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                 cb_protect.setText("防盗保护已经开启");
                   PrefUtils.putBoolean("protect",true,getApplicationContext());
                }else {

                    cb_protect.setText("您没有开启防盗保护");
                    PrefUtils.putBoolean("protect", false, getApplicationContext());
                }
            }
        });
    }



    @Override
    public void showPrevious() {

        startActivity(new Intent(this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.anim_previousin, R.anim.anim_previousout);
    }

    @Override
    public void showNext() {
        PrefUtils.putBoolean("isconfig", true, this);

        if(!mDPM.isAdminActive(mDeviceAdminSample)){

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminSample);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"超级管理员。。。");
            startActivity(intent);

        }else {

            startActivity(new Intent(this, LostAndFindActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        }


    }


}
