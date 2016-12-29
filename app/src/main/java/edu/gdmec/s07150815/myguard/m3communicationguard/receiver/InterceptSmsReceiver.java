package edu.gdmec.s07150815.myguard.m3communicationguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;

import edu.gdmec.s07150815.myguard.m3communicationguard.db.dao.BlackNumberDao;

//注意，修改了receive
public class InterceptSmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean BlackNumStatus=mSP.getBoolean("BlackNumStatus",true);
        if (!BlackNumStatus){
            //黑名单拦截关闭
            return;
        }
        //如果是黑名单，则终止广播
        BlackNumberDao dao=new BlackNumberDao(context);
        Object[] objs= (Object[]) intent.getExtras().get("pdus");
        for (Object obj:objs){
            SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) obj);
            String sender=smsMessage.getOriginatingAddress();
            String body=smsMessage.getMessageBody();
            if (sender.startsWith("+86")){
                sender=sender.substring(3,sender.length());
            }
            int mode=dao.getBlackContactMode(sender);
            if (mode==2||mode==3){
                //需要按揭短信，拦截广播
                abortBroadcast();
            }
        }
    }
}
