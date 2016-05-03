package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;
import com.wangdiaozhu.mobilesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        et_phone = (EditText) findViewById(R.id.et_phone);
      String phone =   PrefUtils.getString("save_phone", "", this);
        et_phone.setText(phone);
    }


    @Override
    public void showPrevious() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.anim_previousin, R.anim.anim_previousout);
    }

    @Override
    public void showNext() {

        String phone = et_phone.getText().toString().trim();
        if(!TextUtils.isEmpty(phone)){
            PrefUtils.putString("save_phone",phone,this);
            startActivity(new Intent(this, Setup4Activity.class));

            finish();
            overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        }else {

            ToastUtils.showToast(this,"安全号码不能为空");

        }

    }

    public void selectContact(View view){
           startActivityForResult(new Intent(this, ContactActivity.class), 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){ String phone =   data.getStringExtra("phone");
            phone = phone.replace("-","").replace(" ","");
            et_phone.setText(phone);}

    }
}
