package edu.gdmec.s07150815.myguard.m4appmanager.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.gdmec.s07150815.myguard.m4appmanager.entity.AppInfo;

/**
 * Created by D on 2016/12/19.
 */
public class AppInfoParser {
    public static List<AppInfo> getAppInfos(Context context){
        //得到一个java保证的包管理器。
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for(PackageInfo packInfo:packageInfos){
            AppInfo appinfo = new AppInfo();
            String packname = packInfo.packageName;
            appinfo.packageName = packname;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            appinfo.icon = icon;
            String appname = packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName = appname;
            //应用程序apk包的路径
            String apkpath = packInfo.applicationInfo.sourceDir;
            appinfo.apkPath= apkpath;
            File file = new File(apkpath);
            long appSize = file.length();
            appinfo.appSize =file.length();
            appinfo.appSize = appSize;
            //应用程序安装的位置
            int flags = packInfo.applicationInfo.flags;//二进制映射 大bit-map
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE&flags)!=0)
            {
                //外部存储
                appinfo.isInRoom = false;
            }else {
                //手机内存
                appinfo.isInRoom = true;
            }
            if ((ApplicationInfo.FLAG_SYSTEM&flags)!=0){
                //系统应用
                appinfo.isUserApp=false;
            }else {
                appinfo.isUserApp=true;
            }
            appInfos.add(appinfo);
            appinfo=null;
        }
        return appInfos;
    }
}
