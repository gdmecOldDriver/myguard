package edu.gdmec.s07150815.myguard.m4appmanager.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by D on 2016/12/19.
 */
public class AppInfo {
    public boolean isSelected = false;
    public String packageName;
    public Drawable icon;
    public String appName;
    public long appSize;
    public boolean isInRoom;
    public boolean isUserApp;
    public String apkPath;

    public  String getAppLocation(boolean isInRoom){
        if (isInRoom){
            return "手机内存";
        }else {
            return "外部存储";
        }
    }
}
