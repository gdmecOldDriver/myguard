package edu.gdmec.s07150815.myguard.m3communicationguard.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

import edu.gdmec.s07150815.myguard.m3communicationguard.db.dao.BlackNumberDao;

//已经修改类名字。注意receive
public class InterceptCallReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean BlacakNumberStatus =mSP.getBoolean("BlackNumStatus",true);
        if (!BlacakNumberStatus){
            //黑名单拦截关闭
            return;
        }
        BlackNumberDao dao=new BlackNumberDao(context);
        if (!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            String mIncomingNumber="";
            //如果是来电
            TelephonyManager tManager= (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()){
                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNumber=intent.getStringExtra("incoming number");
                   //在红米3手机中。电话振铃和切断时，NEW OUTGOING CALL都会发送两次，第二次呼入的广播信使才有电话号码，其余的
                    //其余的都没有 by york
                    if (mIncomingNumber==null){
                        return;
                    }
                    int blackContactMode=dao.getBlackContactMode(mIncomingNumber);
                     if (blackContactMode==1||blackContactMode==3){
                        //观察（另外一个应用程序的数据库变化）呼叫记录的变化，如果呼叫记录生成了，就把呼叫记录删除
                        Uri uri=Uri.parse("content://call_log/callls");
                        context.getContentResolver().registerContentObserver(
                                uri,
                                true,
                                new CallLogObserver(new Handler(),mIncomingNumber,
                                        context));
                        endCall(context);
                    }
                    break;
            }
        }
    }
    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;
        private Context context;

        public CallLogObserver(Handler handler, String incomingNumber, Context context) {

            super(handler);
            this.incomingNumber=incomingNumber;
            this.context=context;

        }
        //观察到数据库内容变化调用的方法

        @Override
        public void onChange(boolean selfChange) {
            Log.i("CallLogObserver","呼叫记录数据库的内容发生变化");
            context.getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber,context);
            super.onChange(selfChange);
        }
    }

    //清除呼叫记录
    public void deleteCallLog(String incomingNumber,Context context){
        ContentResolver resolver=context.getContentResolver();
        Uri uri=Uri.parse("content://call_log/calls");
        Cursor cursor=resolver.query(uri,new String[]{ "_id" },"number=?",
                new String[]{ incomingNumber },"_id  desc limit 1");
        if (cursor.moveToNext()){
            String id=cursor.getString(0);
            resolver.delete(uri,"_id=?",new String[]{ id });
        }
    }

    //挂断电话
    public void endCall(Context context) {
        try {
            for(int i = 0;i<50;i++){
                System.out.println("挂断电话");
            }
            Class clazz=context.getClassLoader().loadClass(
                    "android.os.ServiceManager");
            Method method=clazz.getDeclaredMethod("getService",String.class);
            IBinder iBinder= (IBinder) method.invoke(null,
                    Context.TELEPHONY_SERVICE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
