package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.db.dao.BlackNumberDao;
import com.wangdiaozhu.mobilesafe.domain.BlackNumberInfo;
import com.wangdiaozhu.mobilesafe.utils.MD5Utils;
import com.wangdiaozhu.mobilesafe.utils.PrefUtils;
import com.wangdiaozhu.mobilesafe.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/5.
 */
public class BlackNumberActivity extends Activity {

    private ListView lv_black_number;
    private BlackNumberDao mblackNumberDao;
    private ArrayList<BlackNumberInfo> infos;
    private BlackNumberAdapter mAdapter;
    private ProgressBar pb_loading;
    private  int mIndex;
    private boolean isLoading;
    private int mTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
        mblackNumberDao = BlackNumberDao.getInstance(this);
        mTotalCount = mblackNumberDao.getTotalCount();
        lv_black_number = (ListView) findViewById(R.id.lv_black_number);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        lv_black_number.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE && !isLoading){

                    int lastVisiblePosition = lv_black_number.getLastVisiblePosition();

                    if(lastVisiblePosition >= infos.size()-1){

                       if(infos.size()<mTotalCount){
                           initData();
                       }else    {
                           ToastUtils.showToast(getApplicationContext(),"没有更多数据");
                       }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        initData();
    }


    public void addBlackNumber(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_add_black_number, null);// 给dialog设定特定布局
        // dialog.setView(view);
        dialog.setView(view, 0, 0, 0, 0);// 去掉上下左右边距, 兼容2.x版本
        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        final EditText et_black_number = (EditText) view.findViewById(R.id.et_black_number);
        final RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phone = et_black_number.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
              int  checkedRadioButtonId =  rg_group.getCheckedRadioButtonId();
                    int mode = 1;
                    switch (checkedRadioButtonId){
                        case R.id.rb_phone:
                            mode = 1;
                            break;
                        case R.id.rb_sms:
                            mode = 2;
                            break;
                        case R.id.rb_all:
                            mode = 3;
                            break;
                    }
                    mblackNumberDao.add(phone,mode);
                    BlackNumberInfo addInfo = new BlackNumberInfo();
                    addInfo.number = phone;
                    addInfo.mode = mode;
                  infos.add(0, addInfo);
                    mAdapter.notifyDataSetChanged();
                    dialog.dismiss();
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

    private void initData(){

        isLoading = true;
        pb_loading.setVisibility(View.VISIBLE);
         new Thread(){

             @Override
             public void run() {
//                 infos = mblackNumberDao.findALL();
                 if(infos == null){

                     infos =mblackNumberDao.findPart(mIndex);
                 }else {

                 ArrayList<BlackNumberInfo> partList =  mblackNumberDao.findPart(mIndex);

                     infos.addAll(partList);
                 }

                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if(mAdapter == null){

                             mAdapter = new BlackNumberAdapter();
                             lv_black_number.setAdapter(mAdapter);

                         }else {
//                         mAdapter = new BlackNumberAdapter();
//                         lv_black_number.setAdapter(mAdapter);
                             mAdapter.notifyDataSetChanged();

                         }
//                         mIndex += 20;
                         mIndex = infos.size();
                         pb_loading.setVisibility(View.GONE);
                         isLoading = false;
                     }
                 });
             }
         }.start();
    }

    class BlackNumberAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public BlackNumberInfo getItem(int position) {
            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;
            ViewHolder holder = null;
            if(convertView == null){


               view = View.inflate(getApplication(),R.layout.list_item_black_number,null);
                TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
                TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);
                ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
                holder = new ViewHolder();
                holder.tv_number = tv_number;
                holder.tv_mode = tv_mode;
                holder.iv_delete = iv_delete;
                view.setTag(holder);
            }else {

                view = convertView;
                 holder = (ViewHolder) view.getTag();
            }




            final BlackNumberInfo info = getItem(position);
          holder.tv_number.setText(info.number);
            switch (info.mode){

                case 1:
                   holder.tv_mode.setText("拦截电话");
                    break;
                case 2:
                    holder.tv_mode.setText("拦截短信");
                    break;
                case 3:
                    holder.tv_mode.setText("拦截全部");
                    break;

            }

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context mcontext = BlackNumberActivity.this;
                    AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                 final AlertDialog dialog =    builder.create();
                    View view1 = View.inflate(getApplicationContext(), R.layout.dialog_confirm_cancel, null);
                    Button btn_ok = (Button) view1.findViewById(R.id.btn_ok);
                    Button  btn_cancel = (Button) view1.findViewById(R.id.btn_cancel);
                     dialog.setView(view1);

                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mblackNumberDao.delete(info.number);
                            infos.remove(info);
                            mAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                              dialog.show();
                }
            });
            return view;
        }
    }
     static class ViewHolder{
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delete;
    }

}
