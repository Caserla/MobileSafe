package com.wangdiaozhu.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2016/5/2.
 */
public class ServiceStatusService  {


    public static boolean isServiceRunning(String servicename,Context ctx){

        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

//        获取运行的服务，
        List<ActivityManager.RunningServiceInfo> runningServices =  am.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo runningServiceInfo: runningServices){


         String className = runningServiceInfo.service.getClassName();

            if(className.equals(servicename)){

                return true;

            }

        }

        return  false;
    }
}
