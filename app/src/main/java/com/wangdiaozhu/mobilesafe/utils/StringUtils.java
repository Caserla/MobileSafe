package com.wangdiaozhu.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/4/11.
 */
public class StringUtils {

    public  static  String  streamToString(InputStream is ) throws IOException{

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int len = 0;

        byte[] buffer = new byte[1024];

            while ((len = is.read(buffer)) != -1){

                out.write(buffer,0,len);
            }
                     is.close();
                     out.close();
                   return out.toString();
    }
}
