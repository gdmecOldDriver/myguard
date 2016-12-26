package edu.gdmec.s07150815.myguard.m9advancedtools.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import edu.gdmec.s07150815.myguard.m9advancedtools.entity.AppInfo;

/**
 * Created by ys on 2016/12/22.
 */
//工具类用来获取应用信息。此类重复
public class AppInfoParser {
    //获取手机里面的所有应用程序
    //context 上下文
    public static List<edu.gdmec.s07150815.myguard.m9advancedtools.entity.AppInfo> getAppInfos(Context context){
      //得到一个java保证的 包管理器
        PackageManager pm=context.getPackageManager();
        List<PackageInfo>packInfos=pm.getInstalledPackages(0);
        List<AppInfo>appinfos=new ArrayList<AppInfo>();
        for (PackageInfo packInfo:packInfos){
            AppInfo appinfo=new AppInfo();
            String packname=packInfo.packageName;
            appinfo.packageName=packname;
            Drawable icon=packInfo.applicationInfo.loadIcon(pm);
            appinfo.icon=icon;
            String appname=packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName=appname;
            //应用程序apk包的路径
            String apkpath=packInfo.applicationInfo.sourceDir;
            appinfo.apkPath=apkpath;
            appinfos.add(appinfo);
            appinfo=null;
        }
        return appinfos;
    }

}
