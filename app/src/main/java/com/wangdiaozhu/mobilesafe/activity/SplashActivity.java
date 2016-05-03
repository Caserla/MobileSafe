package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;
import com.wangdiaozhu.mobilesafe.utils.StringUtils;
import com.wangdiaozhu.mobilesafe.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;
import android.widget.Toast;

public class SplashActivity extends Activity {

    private TextView tv_name;
    private String mUrl;
    private String mDes;
    private int mVersionCode;
    private String mVersionName;
    private  static  final int CODE_UPDATE_DIALOG = 1;
    private static  final  int CODE_ENTER_HOME = 2;
    private  static  final  int CODE_URL_ERROR = 3;
    private static final int CODE_NETWORK_ERROR = 4;
    private static final int CODE_JSON_ERROR = 5;
    private TextView tv_progress;
    private RelativeLayout rl_root;
    private Handler mhandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case CODE_UPDATE_DIALOG:
                   showUpdateDialog();
                   break;
               case CODE_ENTER_HOME:
                   enterHome();
                   break;
               case CODE_NETWORK_ERROR:
                   ToastUtils.showToast(getApplicationContext(),"网络连接错误");
                   enterHome();
                     break;
               case CODE_JSON_ERROR:
                  ToastUtils.showToast(getApplicationContext(),"数据解析异常");
                   enterHome();
                   break;
           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText("版本名："+getVersionName());
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);


        boolean autoUpdate = PrefUtils.getBoolean("auto_update",true,this);

        if(autoUpdate){checkVerson();}else {

            mhandler.sendEmptyMessageDelayed(CODE_ENTER_HOME,2000);
        }

        AlphaAnimation anim = new AlphaAnimation(0.2f,1);
        anim.setDuration(2000);
        rl_root.startAnimation(anim);
        copyDb("address.db");
    }


    private String getVersionName(){

        PackageManager pm = getPackageManager();
        try {

            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
               return "";
    }

    private int getVersionCode(){

        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
           return -1;

    }

    private void showUpdateDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现新版本:" + mVersionCode);
        builder.setMessage(mDes);

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                                   enterHome();
            }
        });
         builder.show();
    }
    protected void downloadApk(){
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    tv_progress.setVisibility(View.VISIBLE);
                    final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/app-release.apk";
                    HttpUtils utils = new HttpUtils();
                    utils.download(mUrl, path, new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            String p =   responseInfo.result.getAbsolutePath();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
                               startActivityForResult(intent, 0);
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            super.onLoading(total, current, isUploading);

                            int percent = (int) (100*current/total);
                            tv_progress.setText("下载进度："+percent+"%");
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {

                    Toast.makeText(getApplicationContext(),"SD卡不存在",Toast.LENGTH_SHORT).show();

                }


    }

    private void checkVerson(){
        new Thread(){
            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                HttpURLConnection connection = null;
                try {

                     connection = (HttpURLConnection) new URL("http://10.0.2.2:8080/mobilesafe.json")
                            .openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(2000);
                           connection.connect();
                 int responseCode =  connection.getResponseCode();

                    if(responseCode == 200){

                    InputStream is =  connection.getInputStream();
                      String result =  StringUtils.streamToString(is);

                        try {

                            JSONObject obj = new JSONObject(result);
                            mVersionName = obj.getString("versionName");
                           mVersionCode  = obj.getInt("versionCode");
                            mDes= obj.getString("des");
                            mUrl = obj.getString("url");
                            if(getVersionCode()<mVersionCode){

                                  System.out.println("有更新");
                                msg.what = CODE_UPDATE_DIALOG;
                            }else {
                                        msg.what = CODE_ENTER_HOME;
                                System.out.println("没有更新");
                            }
                        } catch (JSONException e) {
                            msg.what = CODE_JSON_ERROR;
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    msg.what = CODE_NETWORK_ERROR;
                    e.printStackTrace();
                } finally {
                    if(connection!= null){
                        connection.disconnect();
                    }

                    long endTime = System.currentTimeMillis();
                    long TimeUsed =  endTime  - startTime;
                    try {
                        if(TimeUsed<2000){

                            Thread.sleep(2000-TimeUsed);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mhandler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void enterHome(){

        startActivity(new Intent(this,HomeActivity.class)) ;
           finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    private void copyDb(String dbName){

        InputStream is = null;
        FileOutputStream fos = null;
        File file =  getFilesDir();
        File targetFile = new File(file,dbName);
       if(targetFile.exists()){

           return;

       }
        try {
            AssetManager assets =  getAssets();
           is =  assets.open(dbName);



            fos = new FileOutputStream(targetFile);

            int len = 0;
            byte[] bytes = new byte[1024];

            while ( (len = is.read(bytes)) != -1){

                fos.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
 }
