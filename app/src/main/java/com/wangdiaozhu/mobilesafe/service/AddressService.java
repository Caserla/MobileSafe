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
import android.view.Gravity;
import android.view.MotionEvent;
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
    private int startX;
    private int startY;
    private int mScreenWidth;
    private int mScreenHeight;

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
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.LEFT + Gravity.TOP;

       view = View.inflate(this, R.layout.custom_toast,null);

        tv_address = (TextView) view.findViewById(R.id.tv_address);

                     int style =  PrefUtils.getInt("address_style",0,this);
        int[] bgIds = new int[]{R.drawable.call_locate_white,R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green};
        tv_address.setBackgroundResource(bgIds[style]);
        tv_address.setText(address);
        int  lastX =  PrefUtils.getInt("lastX",0,this);
        int lastY =  PrefUtils.getInt("lastY",0,this);
        params.x = lastX;
        params.y = lastY;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:
//                      获得起始坐标点
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
//                        获得移动后坐标点
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
//                       计算偏移量
                        int dx = endX-startX;
                        int dy = endY - startY;

                        params.x = params.x +dx;
                        params.y = params.y+ dy;

                        if(params.x<0){

                              params.x = 0;

                        }

                        if(params.x>mScreenWidth-view.getWidth()){

                            params.x = mScreenWidth - view.getWidth();



                        }
                        if(params.y<0){

                            params.y = 0;

                        }

                        if(params.y>mScreenHeight-view.getWidth() - 40){

                            params.y = mScreenHeight - view.getWidth()-40;



                        }
                        mWM.updateViewLayout(view,params);

//                        重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:
////                       保存当前位置
                        PrefUtils.putInt("lastX",params.x, getApplicationContext());
                        PrefUtils.putInt("lastY",params.y,getApplicationContext());



                        break;

                }





                return true;
            }
        });
             mWM.addView(view, params);
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
