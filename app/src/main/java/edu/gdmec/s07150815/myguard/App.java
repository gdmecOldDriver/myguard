package edu.gdmec.s07150815.myguard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.Log;

/**
 * Created by D on 2016/12/21.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        correctSIM();
    }
    public void correctSIM(){
        //检查SIM卡是否发生变化
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取防盗保护的状态
        boolean protecting = sp.getBoolean("protecting",true);
        if (protecting){
            //得到绑定的sim卡串号
            String bindsim = sp.getString("sim","");
            //得到手机现在的sim卡串号
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //为了测试在手机序列号上dafa 已模拟sim卡被更换的情况
            String realsim = tm.getSimSerialNumber();
            if (bindsim.equals(realsim)){
                Log.i("","sim卡未发生变化，还是你的手机");
            }else{
                Log.i("","sim卡发生了变化");
                //由于系统版本不一，这里的发短信可能与其他手机版本不兼容
                String safenumber = sp.getString("safephone","");
                if (!TextUtils.isEmpty(safenumber)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safenumber,null,"你的手机卡已被更换",null,null);
                }
            }

        }
    }
}
