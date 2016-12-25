package edu.gdmec.s07150815.myguard.m10settings.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Ivan on 2016/12/22.
 */
public class SystemInfoUtils {

//判断一个服务是否处于运行状态 context上下文sss
    public static boolean isServiceRunning(Context context,String className){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(200);
        for (ActivityManager.RunningServiceInfo info : infos){
            String serviceClassName = info.service.getClassName();
            if (className.equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }
}
