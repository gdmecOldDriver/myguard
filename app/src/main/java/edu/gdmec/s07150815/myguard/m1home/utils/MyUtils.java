package edu.gdmec.s07150815.myguard.m1home.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

import edu.gdmec.s07150815.myguard.R;

public class MyUtils {
//获取版本号
    //返回版本号
    public  static String getVersion(Context context){
        //PackageMananger 可以获取清单文件中的所有信息
        PackageManager manager = context.getPackageManager();
        try {
            //获取到一个应用程序的信息
            //getPackageName() 获取到当前程序的包名
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";//返回空
        }
    }
//安装新版本
    public static void installApk(Activity activity){
        Intent intent = new Intent("android.intent.action.VIEW");
        //添加默认分类
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(
                Uri.fromFile(new File("/mnt/sdcard/mobilesafe2.0.apk")),
                "application/vnd.android.package-archive");
        //如果开启的activity退出的时候，会回调当前activity的onActivityResult
        activity.startActivityForResult(intent,0);

    }

}
