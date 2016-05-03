package com.wangdiaozhu.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.service.LocationService;
import com.wangdiaozhu.mobilesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/4/18.
 */
public class SmsReceiver extends BroadcastReceiver {

    private DevicePolicyManager mDBM;
    private ComponentName mDeviceAdminSample;

    @Override
    public void onReceive(Context context, Intent intent) {
       Object[] objects = (Object[]) intent.getExtras().get("pdus");

        for (Object obj : objects) {
        SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);

           String originatingAddress =  sms.getOriginatingAddress();
         String messageBody =  sms.getMessageBody();
        if("#*alarm*#".equals(messageBody)){

            MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
            player.setVolume(1f,1f);
            player.setLooping(true);
            player.start();
            abortBroadcast();
        }else if("#*location*#".equals(messageBody)){

            context.startService(new Intent(context, LocationService.class));
            abortBroadcast();
        }else if ("#*lockscreen*#".equals(messageBody)){
            mDeviceAdminSample = new ComponentName(context,AdminReceiver.class);
            mDBM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            if(mDBM.isAdminActive(mDeviceAdminSample)){

                mDBM.lockNow();
                mDBM.resetPassword("123",0);
            }else {

                ToastUtils.showToast(context,"您没有激活管理权限");

            }


        }else  if("#*wipedata*#".equals(messageBody)){

                mDBM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);

        }
        }
    }
}
