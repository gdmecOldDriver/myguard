package edu.gdmec.s07150815.myguard.m2theftguard.utils;

import android.os.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by D on 2016/12/20.
 */
public class MD5Utils {
    public static String encode(String text){
        try{
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
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
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return "";
        }
    }
}
