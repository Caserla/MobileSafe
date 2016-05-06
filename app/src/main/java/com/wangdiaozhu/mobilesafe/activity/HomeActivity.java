package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.utils.MD5Utils;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;
import com.wangdiaozhu.mobilesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/4/12.
 */
public class HomeActivity extends Activity {

    private GridView gv_home;
    private String[] mHomeNames = new String[]{"手机防盗","通讯卫士","软件管理","进程管理",
            "流量统计","手机杀毒","缓存管理","高级工具","设置中心"};

    private  int[] mImagesIds = new int[]{R.drawable.home_safe,
            R.drawable.home_callmsgsafe,R.drawable.home_apps,R.drawable.home_taskmanager
    ,R.drawable.home_netmanager,R.drawable.home_trojan,
            R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        showSafeDialog();
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
                            break;
                    case 7:

                        startActivity(new Intent(getApplicationContext(),AToolsActivity.class));
                            break;
                    case 8:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        break;


                }
            }
        });

    }
    protected void showSafeDialog(){
        String pwd =  PrefUtils.getString("password",null,this);
        if (!TextUtils.isEmpty(pwd)){

        showInputPwdDialog();

        }else {
            showSetPwdDialog();
        }

    }
    private void showInputPwdDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_input_pwd, null);// 给dialog设定特定布局
        // dialog.setView(view);
        dialog.setView(view, 0, 0, 0, 0);// 去掉上下左右边距, 兼容2.x版本

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        final EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pwd = etPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    String savePwd = PrefUtils.getString("password", null,
                            getApplicationContext());
                    if(MD5Utils.encode(pwd).equals(savePwd)){
                        startActivity(new Intent(getApplicationContext(),LostAndFindActivity.class));
                        dialog.dismiss();
                    }else {

                        ToastUtils.showToast(getApplication(),"密码错误");

                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), "输入内容不能为空!");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showSetPwdDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_set_pwd, null);// 给dialog设定特定布局
        // dialog.setView(view);
        dialog.setView(view, 0, 0, 0, 0);// 去掉上下左右边距, 兼容2.x版本

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        final EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
        final EditText etPwdConfirm = (EditText) view
                .findViewById(R.id.et_pwd_confirm);

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pwd = etPwd.getText().toString().trim();
                String pwdConfirm = etPwdConfirm.getText().toString().trim();

                if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(pwdConfirm)) {
                    if (pwd.equals(pwdConfirm)) {
                        // 保存密码
                        PrefUtils.putString("password", MD5Utils.encode(pwd),
                                getApplicationContext());
                        dialog.dismiss();
                               startActivity(new Intent(getApplicationContext(),LostAndFindActivity.class));
                        // 跳到手机防盗
//                        startActivity(new Intent(getApplicationContext(),
//                                LostAndFindActivity.class));
                    } else {
                        ToastUtils.showToast(getApplicationContext(),
                                "两次密码不一致!");
                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), "输入内容不能为空!");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class HomeAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return mHomeNames.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

          View view =   View.inflate(getApplicationContext(), R.layout.list_item_home, null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name.setText(mHomeNames[position]);
            iv_icon.setImageResource(mImagesIds[position]);

            return view;
        }
    }
}
