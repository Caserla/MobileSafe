package com.wangdiaozhu.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

import com.wangdiaozhu.mobilesafe.utils.PrefUtils;

/**
 * Created by Administrator on 2016/4/18.
 */
public class LocationService extends Service {

    private LocationManager lm;
    private MyListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);
        listener = new MyListener();
      String bestProvider =  lm.getBestProvider(criteria,true);
        //noinspection ResourceType
        lm.requestLocationUpdates(bestProvider,0,0, listener);
    }
    class MyListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            String w ="w:"+ location.getLatitude();
            String j = "j:"+location.getLongitude();
            String accuracy = "accuracy:"+  location.getAccuracy();


            String result = j +"\n"+w+"\n"+accuracy;
            PrefUtils.getString("save_phone","",getApplicationContext());
           SmsManager sms =  SmsManager.getDefault();
             sms.sendTextMessage("",null,result,null,null);
            stopSelf();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //noinspection ResourceType
        lm.removeUpdates(listener);
        listener = null;
    }
}
