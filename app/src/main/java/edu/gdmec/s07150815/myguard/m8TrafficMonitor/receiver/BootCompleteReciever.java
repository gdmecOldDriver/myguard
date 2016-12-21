package edu.gdmec.s07150815.myguard.m8trafficMonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.gdmec.s07150815.myguard.m8trafficMonitor.service.TrafficMonitoringService;
import edu.gdmec.s07150815.myguard.m8trafficMonitor.utils.SystemInfoUtils;

/**
 * Created by Ivan on 2016/12/20.
 */
public class BootCompleteReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if(!SystemInfoUtils.isServiceRunning(context,"edu.gdmec.s07150815.myguard.m8trafficMonitor.service.TrafficMonitoringService")){
            Log.d("traffic service","turn on");
            context.startService(new Intent(context, TrafficMonitoringService.class));
        }
    }
}
