package edu.gdmec.s07150815.myguard.m8trafficMonitor.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.gdmec.s07150815.myguard.m8trafficMonitor.db.dao.TrafficDao;

/**
 * Created by Ivan on 2016/12/20.
 */
public class TrafficMonitoringService extends Service {

    private long mOldRxBytes;
    private long mOldTxBytes;
    private TrafficDao dao;
    private SharedPreferences mSp;
    private long usedFlow;
    boolean flag = true;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mOldRxBytes = TrafficStats.getMobileRxBytes();
        mOldTxBytes = TrafficStats.getMobileTxBytes();
        dao = new TrafficDao(this);
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        mThread.start();

    }


    private Thread mThread = new Thread() {

        public void run() {
            while (flag) {

                try {
                    Thread.sleep(2000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateTodayGPRS();
            }


        }

        private void updateTodayGPRS() {

            usedFlow = mSp.getLong("usedflow",0);
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if(calendar.DAY_OF_MONTH == 1 & calendar.HOUR_OF_DAY == 0 & calendar.MINUTE<1&calendar.SECOND<30){
                usedFlow = 0;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataString = sdf.format(date);
            long mobileGPRS = dao.getMoblieGPRS(dataString);
            long mobileRxBytes = TrafficStats.getMobileRxBytes();
            long moblieTxBytes = TrafficStats.getMobileTxBytes();
            long newGPRS = (mobileRxBytes + moblieTxBytes)-mOldRxBytes-mOldTxBytes;
            mOldRxBytes = mobileRxBytes;
            mOldTxBytes = moblieTxBytes;
            if(newGPRS<0){
                newGPRS = mobileRxBytes + moblieTxBytes;
            }
            if(mobileGPRS==-1){
                dao.insertTodayGPRS(newGPRS);
            }else{
                if(mobileGPRS<0){
                    mobileGPRS=0;
                }
                dao.UpdateTodayGPRS(mobileGPRS+newGPRS);
            }
            usedFlow = usedFlow + newGPRS;
            SharedPreferences.Editor edit = mSp.edit();
            edit.putLong("usedflow",usedFlow);
            edit.commit();





        }


    };

    @Override
    public void onDestroy() {

        if (mThread!=null&!mThread.isInterrupted()){
            flag = false;
            mThread.interrupt();
            mThread = null;
        }
        super.onDestroy();

    }
}