package edu.gdmec.s07150815.myguard.m4appmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

import edu.gdmec.s07150815.myguard.m4appmanager.entity.AppInfo;

/**
 * Created by D on 2016/12/19.
 */
public class EngineUtils {
    //开始应用程序
    public static void startApplication(Context context, AppInfo appInfo) {
        PackageManager pm = context.getPackageManager();
        Intent intent  = pm.getLaunchIntentForPackage(appInfo.packageName);
        if (intent!=null){
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"该应用没有启动界面",Toast.LENGTH_SHORT).show();
        }
    }
    //分享应用
    public static void shareApplication(Context context, AppInfo appInfo) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("test/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"推荐你使用一款软件，名称叫："+appInfo.appName+"下载路径：http://play.google.com/store/apps/detail?id="
        +appInfo.packageName);
        context.startActivity(intent);
    }
    //设置应用
    public static void SettingAppDetail(Context context, AppInfo appInfo) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+appInfo.packageName));
        context.startActivity(intent);
    }
    //卸载应用
    public static void uninstallApplication(Context context, AppInfo appInfo) {
        if (appInfo.isUserApp){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:"+appInfo.packageName));
            context.startActivity(intent);
        }else {
            //系统应用，root权限 利用linux命令删除文件。
            if (!RootTools.isRootAvailable()){
                Toast.makeText(context,"卸载系统应用，必须要root权限",Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                if (!RootTools.isAccessGiven()){
                    Toast.makeText(context,"请授权黑马小护卫root权限",Toast.LENGTH_SHORT).show();
                    return;
                }
                RootTools.sendShell("mount -o remount ,rw /system",3000);
                RootTools.sendShell("rm -r"+appInfo.apkPath,3000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
