package edu.gdmec.s07150815.myguard.m8TrafficMonitor.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import edu.gdmec.s07150815.myguard.m8TrafficMonitor.db.TrafficOpenHelper;

/**
 * Created by Ivan on 2016/12/20.
 */
public class TrafficDao {


    private TrafficOpenHelper helper;

    public TrafficDao(Context context){

    helper = new TrafficOpenHelper(context);

    }

    public long getMoblieGPRS(String dataString){

        SQLiteDatabase db = helper.getReadableDatabase();

        long gprs = 0;
        Cursor cursor = db.rawQuery("select gprs from traffic where date=?",new String[] {
                "datetime("+dataString+")"});
        if(cursor.moveToNext()){
            String gprsStr = cursor.getString(0);
            if(!TextUtils.isEmpty(gprsStr))
                gprs = Long.parseLong(gprsStr);

        }else{
            gprs = -1;
        }

        return gprs;

    }

        public void insertTodayGPRS(long gprs){



        }













}
