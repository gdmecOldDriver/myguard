package edu.gdmec.s07150815.myguard.m2theftguard.service;

/**
 * Created by D on 2016/12/20.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

/**用于定位*/
public class GPSLocationService extends Service{
    private LocationManager lm;
    private MyListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lm= (LocationManager) getSystemService(LOCATION_SERVICE);
        listener= new MyListener();
        //criteria 查询条件
        //true只返回可用的位置提供者
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);
        String name= lm.getBestProvider(criteria,true);
        System.out.println("最好的位置提供者："+name);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            return;
        }
       lm.requestLocationUpdates(name, 0, 0, listener);
    }

    private class MyListener implements  LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            StringBuffer sb = new StringBuffer();
            sb.append("accuracy:"+location.getAccuracy()+"\n");
            sb.append("speed:"+location.getSpeed()+"\n");
            sb.append("jingdu:"+location.getLongitude()+"\n");
            sb.append("weidu:"+location.getLatitude()+"\n");
            String result = sb.toString();
            SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
            String safenumber = sp.getString("safephone","");
            SmsManager.getDefault().sendTextMessage(safenumber,null,result,null,null);
            stopSelf();
        }
            //当位置提供者状态发生变化时调用的方法
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
            //当某个位置提供者可用的时候调用的方法
        @Override
        public void onProviderEnabled(String provider) {

        }
            //当某个位置提供者 不可用的时候调用的方法
        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            return;
        }
        lm.removeUpdates(listener);
        listener=null;
    }
}
