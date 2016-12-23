package edu.gdmec.s07150815.myguard.m7processmanager.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import java.util.ArrayList;
import java.util.List;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m7processmanager.entity.TaskInfo;

/**
 * Created by ys on 2016/12/20.
 */

//任务信息&&进程信息的解析器

public class TaskInfoParser {
    //获取正在运行的所有的进程的信息
    //参数context 上下文
    //返回进程信息的集合

    public static List<TaskInfo> getRunningTaskInfos(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo>processInfos = am.getRunningAppProcesses();
        List<TaskInfo>taskInfos = new ArrayList<TaskInfo>();
        for(ActivityManager.RunningAppProcessInfo processInfo:processInfos){
            String packname = processInfo.processName;
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.packageName = packname;
            Debug.MemoryInfo[]  memoryinfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            long memsize = memoryinfos[0].getTotalPrivateDirty()*1024;
            taskInfo.appMemory = memsize;
            try {
                PackageInfo packaInfo = pm.getPackageInfo(packname,0);
                Drawable icon = packaInfo.applicationInfo.loadIcon(pm);
                taskInfo.appIcon = icon;
                String appname = packaInfo.applicationInfo.loadLabel(pm).toString();
                taskInfo.appName = appname;
                if((ApplicationInfo.FLAG_SYSTEM&packaInfo.applicationInfo.flags)!=0){
                    //系统进程
                    taskInfo.isUserApp = false;
                }else{
                    //用户进程
                    taskInfo.isUserApp = true;
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.appName = packname;
                taskInfo.appIcon = context.getResources().getDrawable(R.mipmap.ic_launcher);
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}
