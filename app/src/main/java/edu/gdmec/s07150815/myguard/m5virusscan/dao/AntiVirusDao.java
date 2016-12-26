package edu.gdmec.s07150815.myguard.m5virusscan.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by D on 2016/12/19.
 */
public class AntiVirusDao {
    /**检查某个MD5是否有病毒
     * return null 表示扫描安全*/
    private static Context context;
    private static String dbname;
    public AntiVirusDao(Context context){
        this.context = context;
        dbname = "/data/data/"+context.getPackageName()+"/files/antivirus.db";
    }
    public String checkVirus(String md5){
        String desc = null;
        //打开病毒库
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbname,null,SQLiteDatabase.OPEN_READONLY);
         Cursor cursor = db.rawQuery("select desc from datable where md5=?"
                 ,new String[] {md5});
        if (cursor.moveToNext()){
            desc = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return desc;
    }

    /**判断数据库文件是否存在*/
    public boolean isDBExit(){
        File file = new File(dbname);
        return file.exists()&&file.length()>0;
    }

    /** 获取数据库版本号*/
    public String getDBVersionNum(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbname,null,SQLiteDatabase.OPEN_READONLY);
        String versionnumber = "0";
        Cursor cursor = db.rawQuery("select subcnt from version",null);
        if (cursor.moveToNext()){
            versionnumber = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return versionnumber;
    }
    /**更新数据库*/
    public void  updateDBVersion(int newversion){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbname,null,SQLiteDatabase.OPEN_READONLY);
        String versionnumber  = "0";
        ContentValues values = new ContentValues();
        values.put("subcnt",newversion);
        db.update("version",values,null,null);
        db.close();
    }
    /**更新病毒库的API*/
    public void  add(String desc,String md5){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbname,null,SQLiteDatabase.OPEN_READONLY);
        ContentValues values = new ContentValues();
        values.put("md5",md5);
        values.put("desc",desc);
        values.put("type",6);
        values.put("name","Android.Hark.i22hkt.a");
        db.insert("dataable",null,values);
        db.close();
    }
}
