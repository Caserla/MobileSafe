package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;
import com.wangdiaozhu.mobilesafe.utils.ToastUtils;
import com.wangdiaozhu.mobilesafe.view.SettingItemView;

/**
 * Created by Administrator on 2016/4/13.
 */
public class Setup2Activity extends BaseSetupActivity {


    private SettingItemView siv_bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        siv_bind = (SettingItemView) findViewById(R.id.siv_bind);

          String bindSim =  PrefUtils.getString("bind_sim", null, getApplicationContext());
             siv_bind.setTitle("点击绑定sim卡");
        if(TextUtils.isEmpty(bindSim)){

            siv_bind.setChecked(false);
            siv_bind.setDesc("sim未绑定");
        }else {

            siv_bind.setChecked(true);
            siv_bind.setDesc("sim已绑定");
        }
       siv_bind.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(siv_bind.isChecked()){

                   siv_bind.setChecked(false);
                   siv_bind.setDesc("sim未绑定");

                            PrefUtils.remove("bind_sim",getApplicationContext());
               }else {


                   siv_bind.setChecked(true);
                   siv_bind.setDesc("sim已绑定");
                   TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
             String serialNumber = tm.getSimSerialNumber();
                   PrefUtils.putString("bind_sim",serialNumber,getApplicationContext());
               }
           }
       });
    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.anim_previousin,R.anim.anim_previousout);
    }

    @Override
    public void showNext() {
        String bindSim =  PrefUtils.getString("bind_sim", null, getApplicationContext());

        if(TextUtils.isEmpty(bindSim)){

            ToastUtils.showToast(this,"必须绑定sim卡！");
            return;
        }
         startActivity(new Intent(this,Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
    }

}
