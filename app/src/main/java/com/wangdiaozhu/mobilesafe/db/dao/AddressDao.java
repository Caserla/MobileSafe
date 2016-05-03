package com.wangdiaozhu.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/5/2.
 * 归属地查询数据库的封装
 */
public class AddressDao {



    private  static  final  String PATH = "/data/data/com.wangdiaozhu.mobilesafe/files/address.db";

    public static String getAddress(String number) {

        String address = "未知号码";

        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);

        if (number.matches("^1[3-8]\\d{9}$")) {

            Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{number.substring(0, 7)});

            if (cursor.moveToFirst()) {

                address = cursor.getString(0);

            }
             cursor.close();


        }else {

            switch (number.length()){

                case 3:
                    address = "报警电话";
                    break;
                case 4:
                    address = "模拟器";
                    break;
                case 5:
                    address = "客服电话";
                     break;
                case 7:
                case 8:
                    address = "本地电话";
                    break;
                default:
                    if(number.startsWith("0")&&(number.length()>=11 && number.length()<=12)){

                     Cursor cursor =  database.rawQuery("select location from data2 where area =?", new String[]{number.substring(1, 4)});

                        if(cursor.moveToFirst()){

                            address = cursor.getString(0);

                        }
                        cursor.close();
                        if("未知号码".equals(address)){

                            cursor = database.rawQuery("select location from data2 where area =?",new String[]{number.substring(1,3)});

                            if(cursor.moveToFirst()){

                                address = cursor.getString(0);

                            }

                            cursor.close();
                        }
                    }
                    break;
            }

        }
        database.close();
        return address;

    }
}
