package edu.gdmec.s07150815.myguard.m5virusscan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * Created by D on 2016/12/19.1
 */
public class MD5Utils {

    public static String getFileMd5(String path){
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            File file= new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len=fis.read(buffer))!=-1){
                digest.update(buffer,0,len);
            }
            byte[] result = digest.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b:result){
                int number = b&0xff;
                String hex = Integer.toHexString(number);
                if (hex.length()==1){
                    sb.append("0"+hex);

                }else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
