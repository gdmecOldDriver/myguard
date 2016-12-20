package edu.gdmec.s07150815.myguard.m7processmanager.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by ys on 2016/12/20.
 */
public class TaskInfo {
    public String appName;
    public long appMemory;
    //用来标记app是否被选中1
    public boolean isChecked;
    public Drawable appIcon;
    public boolean isUserApp;
    public String packageName;
}
