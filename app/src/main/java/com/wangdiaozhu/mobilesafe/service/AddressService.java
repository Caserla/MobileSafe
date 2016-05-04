package com.wangdiaozhu.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.db.dao.AddressDao;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;


/**
 * Created by Administrator on 2016/5/2.
 */
public class AddressService extends Service {

    private TelephonyManager mTM;
    private MyListener listener;
    private innerReceiver mReceiver;
    private WindowManager mWM;
    private View view;
    private TextView tv_address;

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

//            Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();

                    showToast(address);
        }
    }

    public void showToast(String address){

        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

       view = View.inflate(this, R.layout.custom_toast,null);

        tv_address = (TextView) view.findViewById(R.id.tv_address);

                     int style =  PrefUtils.getInt("address_style",0,this);
        int[] bgIds = new int[]{R.drawable.call_locate_white,R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green};
        tv_address.setBackgroundResource(bgIds[style]);
        tv_address.setText(address);
             mWM.addView(view,params);
    }

    class MyListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){

                case TelephonyManager.CALL_STATE_RINGING:
                    String address = AddressDao.getAddress(incomingNumber);
                    showToast(address);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
                case TelephonyManager.CALL_STATE_IDLE:

                    if(mWM != null && view!= null){ mWM.removeView(view);   }

                    break;
            }
        }
    }
}
