package com.wangdiaozhu.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.wangdiaozhu.mobilesafe.db.dao.AddressDao;


/**
 * Created by Administrator on 2016/5/2.
 */
public class AddressService extends Service {

    private TelephonyManager mTM;
    private MyListener listener;
    private innerReceiver mReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        listener = new MyListener();
        mTM.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);

        mReceiver = new innerReceiver();
        IntentFilter intentFilter = new IntentFilter();
          intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mReceiver, intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mTM.listen(listener,PhoneStateListener.LISTEN_NONE);
        listener = null;

//        注销去电广播
        unregisterReceiver(mReceiver);
        mReceiver = null;
    }

//监听去电广播（动态注册）
    class innerReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {

          String number =  getResultData();

           String address = AddressDao.getAddress(number);

            Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();
        }
    }
    class MyListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){

                case TelephonyManager.CALL_STATE_RINGING:
                    String address = AddressDao.getAddress(incomingNumber);
                    Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    }
}
