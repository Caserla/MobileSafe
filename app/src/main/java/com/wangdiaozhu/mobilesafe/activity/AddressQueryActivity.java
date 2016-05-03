package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.db.dao.AddressDao;
import com.wangdiaozhu.mobilesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/5/2.
 */
public class AddressQueryActivity extends Activity {

    private EditText etNumber;
    private Button btnStart;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_query);

        etNumber = (EditText) findViewById(R.id.et_number);
        btnStart = (Button) findViewById(R.id.btn_start);
        tvResult = (TextView) findViewById(R.id.tv_result);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String number =  etNumber.getText().toString().trim();
                if(!TextUtils.isEmpty(number)){

                    String address = AddressDao.getAddress(number);

                    tvResult.setText(address);

                }else {

                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    etNumber.startAnimation(shake);
                       vibrator();

                }


            }
        });

//        文本变化监听器
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {



                String address = AddressDao.getAddress(s.toString());

                tvResult.setText(address);
            }
        });
    }
    public  void vibrator(){

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
       vibrator.vibrate(new long[]{1000,2000,2000,3000},-1);

    }
}
