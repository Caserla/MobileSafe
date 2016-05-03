package com.wangdiaozhu.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.wangdiaozhu.mobilesafe.utils.PrefUtils;

/**
 * Created by Administrator on 2016/4/17.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

       boolean protect =  PrefUtils.getBoolean("protect", false, context);

        if(!protect){return;}else {
            String saveSim = PrefUtils.getString("bind_sim", null, context);
            if (!TextUtils.isEmpty(saveSim)) {

                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                String currentsim = tm.getSimSerialNumber() + "xxx";

                if (!saveSim.equals(currentsim)) {

                String save_phone = PrefUtils.getString("save_phone","",context);

                    SmsManager sm = SmsManager.getDefault();
                    sm.sendTextMessage(save_phone,null,"sim card changed!",null,null);
                }
            }
        }
    }
}
