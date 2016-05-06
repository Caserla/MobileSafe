package com.wangdiaozhu.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wangdiaozhu.mobilesafe.domain.BlackNumberInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/5.
 */
public class BlackNumberDao {

private  static BlackNumberDao blackNumberDao = null;
    private final BlackNumberOpenHelper helper;

    private  BlackNumberDao(Context context){

        helper = new BlackNumberOpenHelper(context);

}
    public static  BlackNumberDao getInstance(Context context){


                    synchronized (BlackNumberDao.class){

                        if(blackNumberDao == null){

                            blackNumberDao = new BlackNumberDao(context);

                    }

                }

        return  blackNumberDao;

    }
    public void add(String number,int mode){

        SQLiteDatabase database =  helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number",number);
        values.put("mode",mode);
        database.insert("blacknumber",null,values);
        database.close();
    }

    public  void delete(String number){

        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete("blacknumber", "number=?", new String[]{number});
        database.close();
    }

    public void  update(String number, int mode){

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number",number);
        values.put("mode",mode);

        database.update("blacknumber", values, "number=?", new String[]{number});
        database.close();

    }

    public   boolean find(String number){

        SQLiteDatabase database = helper.getWritableDatabase();
     Cursor cursor =  database.query("blacknumber", new String[]{"number", "mode"}, "number=?", new String[]{number}, null, null, null);

        boolean exist = false;
        if(cursor.moveToFirst()){

              exist = true;
        }
        cursor.close();
        database.close();
        return exist;
    }

    public int findmode(String number){

        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor =  database.query("blacknumber", new String[]{"number", "mode"}, "number=?", new String[]{number}, null, null, null);

       int mode = -1;
        if(cursor.moveToFirst()){

           mode = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return mode;
    }
//    查询所有黑名单

    public  ArrayList<BlackNumberInfo> findALL(){

        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor =  database.query("blacknumber", null, null, null, null, null, null);


        ArrayList<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
       while (cursor.moveToNext()){

          String number = cursor.getString(0);

           int mode = cursor.getInt(1);

           BlackNumberInfo info = new BlackNumberInfo();
           info.number = number;
           info.mode = mode;
           infos.add(info);

       }
        cursor.close();
        database.close();
        return infos;
    }
    //分页查询20条数据

    public  ArrayList<BlackNumberInfo> findPart(int index){

        SQLiteDatabase database = helper.getWritableDatabase();
             Cursor cursor =   database.rawQuery("select number,mode from blacknumber order by _id desc limit ?,20", new String[]{index + ""});


        ArrayList<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()){

            String number = cursor.getString(0);

            int mode = cursor.getInt(1);

            BlackNumberInfo info = new BlackNumberInfo();
            info.number = number;
            info.mode = mode;
            infos.add(info);

        }
        cursor.close();
        database.close();
        return infos;
    }

    public  int getTotalCount(){

        SQLiteDatabase database = helper.getWritableDatabase();
      Cursor cursor =  database.rawQuery("select count(*) from blacknumber", null);

        int count = -1;
        if (cursor.moveToFirst()){

            count = cursor.getInt(0);

        }
        cursor.close();
        database.close();
        return  count;
    }


}
