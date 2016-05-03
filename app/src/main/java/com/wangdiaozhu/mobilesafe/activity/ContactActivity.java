package com.wangdiaozhu.mobilesafe.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wangdiaozhu.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactActivity extends AppCompatActivity {

    private ListView lv_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        lv_contact = (ListView) findViewById(R.id.lv_contact);
        final ArrayList< HashMap<String,String >> list = readContact();
        lv_contact.setAdapter(new SimpleAdapter(this,list,R.layout.list_item_contact,new String[]{"name","phone"},new int[]{R.id.tv_name,R.id.tv_phone}));
          lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              HashMap<String,String> map = list.get(position);
              String phone = map.get("phone");
                  Intent data = new Intent();
                  data.putExtra("phone",phone);
              setResult(0,data);
         finish();
              }

          });

    }

    private  ArrayList< HashMap<String,String >> readContact(){

      Cursor cursor = getContentResolver().query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"contact_id"}, null, null, null);
        ArrayList< HashMap<String,String >> list = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){//遍历所有联系人

         String contactid =  cursor.getString(0);
           Cursor dataCursor = getContentResolver().query(Uri.parse("content://com.android.contacts/data"), new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{contactid}, null);
            HashMap<String,String > map = new HashMap<String,String>();
            while (dataCursor.moveToNext()){//遍历单个联系人的信息

              String data = dataCursor.getString(0);
              String type = dataCursor.getString(1);

                if("vnd.android.cursor.item/phone_v2".equals(type)){

                     map.put("phone",data);

                }else if("vnd.android.cursor.item/name".equals(type)){
                          map.put("name",data);

                }
            }
             dataCursor.close();
             if(!TextUtils.isEmpty(map.get("name"))&&!TextUtils.isEmpty(map.get("phone"))){
                 list.add(map);
             }
        }
        cursor.close();
        return list;
    }
}
