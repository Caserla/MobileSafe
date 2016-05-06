package com.wangdiaozhu.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wangdiaozhu.mobilesafe.R;
import com.wangdiaozhu.mobilesafe.db.dao.BlackNumberDao;
import com.wangdiaozhu.mobilesafe.domain.BlackNumberInfo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
        mblackNumberDao = BlackNumberDao.getInstance(this);
        lv_black_number = (ListView) findViewById(R.id.lv_black_number);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        initData();
    }

    private void initData(){
        pb_loading.setVisibility(View.VISIBLE);
         new Thread(){

             @Override
             public void run() {
                 infos = mblackNumberDao.findALL();
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         mAdapter = new BlackNumberAdapter();
                         lv_black_number.setAdapter(mAdapter);
                         pb_loading.setVisibility(View.GONE);
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




            BlackNumberInfo info = getItem(position);
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
            return view;
        }
    }
     static class ViewHolder{
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delete;
    }

}
