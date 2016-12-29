package edu.gdmec.s07150815.myguard.m8trafficMonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m8trafficMonitor.db.dao.TrafficDao;
import edu.gdmec.s07150815.myguard.m8trafficMonitor.service.TrafficMonitoringService;
import edu.gdmec.s07150815.myguard.m8trafficMonitor.utils.SystemInfoUtils;

public class TrafficMonitoringActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences mSP;
    private Button mCorrectFlowBtn;
    private TextView mTotalTV,mUsedTV,mToDayTV,mRemindTV;
    private ImageView mRemindIMGV;
    private TrafficDao dao;
    private CorrectFlowReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_traffic_monitoring);
        mSP = getSharedPreferences("config",MODE_PRIVATE);
        boolean flag = mSP.getBoolean("isset_operator",false);
        if (!flag){
            startActivity(new Intent(this,OperatorSetActivity.class));
            finish();
        }
        if (!SystemInfoUtils.isServiceRunning(this,"cn.edu.gdmec.chaos07150844.myguard.m8trafficmonitor.service.TrafficMonitoringService")){
            startService(new Intent(this, TrafficMonitoringService.class));
        }
        initView();
        registReceiver();
        initData();
    }

    private void initData() {
        long totalflow = mSP.getLong("totalflow",0);
        long userflow = mSP.getLong("usedflow",0);
        if (totalflow > 0 & userflow >= 0){
            float scale = userflow/totalflow;
            if (scale > 0.9){
                mRemindIMGV.setEnabled(false);
                mRemindTV.setText("你的套餐流量即将用完!");
            }else {
                mRemindIMGV.setEnabled(true);
                mRemindTV.setText("本月流量充足请放心使用");
            }
        }
        mTotalTV.setText("本月流量："+ Formatter.formatFileSize(this,totalflow));
        mUsedTV.setText("本月已用："+Formatter.formatFileSize(this,userflow));
        dao = new TrafficDao(this);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataString = sdf.format(date);
        long moblieGPRS = dao.getMoblieGPRS(dataString);
        if (moblieGPRS<0){
            moblieGPRS = 0;
        }
        mToDayTV.setText("本日已用："+Formatter.formatFileSize(this,moblieGPRS));
    }

    private void registReceiver() {
        receiver = new CorrectFlowReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver,filter);
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.light_green)
        );
        ImageView mLeftImgv = (ImageView)findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("运营商信息设置");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mCorrectFlowBtn = (Button)findViewById(R.id.btn_correction_flow);
        mCorrectFlowBtn.setOnClickListener(this);
        mTotalTV = (TextView) findViewById(R.id.tv_month_totalgprs);
        mUsedTV = (TextView) findViewById(R.id.tv_month_usedgprs);
        mToDayTV = (TextView) findViewById(R.id.tv_today_gprs);
        mRemindIMGV = (ImageView) findViewById(R.id.imgv_traffic_remind);
        mRemindTV = (TextView) findViewById(R.id.tv_traffic_remind);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_correction_flow:
                int i = mSP.getInt("operator",0);
                SmsManager smsManager = SmsManager.getDefault();
                switch (i){
                    case 0:
                        Toast.makeText(this,"您还没有设置运营商信息",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        smsManager.sendTextMessage("10086",null,"CXLL",null,null);
                        break;
                    case 2:
                        smsManager.sendTextMessage("10010",null,"LLCX",null,null);
                        break;
                    case 3:
                        break;
                }
        }
    }

    class CorrectFlowReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[])intent.getExtras().get("pdus");
            int j = 0;
            String body = "";
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                body += smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();
                if (!address.equals("10086")) {
                    return;
                }
            }

            System.out.println(body);
            String[] split = body.split("，");
            long left = 0;
            long used = 0;
            long beyond = 0;
            boolean boo = true;
            for (int i = 0;i<split.length;i++){
                System.out.println(i+split[i]);
                if (split[i].contains("您当月常用流量已用")){
                    String usedflow = split[i].substring(9,split[i].length());
                    used = getStringTofloat(usedflow);
                    System.out.println(usedflow);
                    System.out.println(used);
                }else if (boo & split[i].contains("可用")){
                    String leftflow = split[i].substring(2,split[i].length());
                    left = getStringTofloat(leftflow);
                    boo = false;
                    System.out.println(leftflow);
                    System.out.println(left);
                }else if (split[i].contains("套餐外流量")){
                    String beyondflow = split[i].substring(5,split[i].length());
                    beyond = getStringTofloat(beyondflow);
                }
            }
            SharedPreferences.Editor editor = mSP.edit();
            editor.putLong("totalflow",used+left);
            editor.putLong("usedflow",used+beyond);
            editor.commit();
            mTotalTV.setText("本月流量："+Formatter.formatFileSize(context,(used+left)));
            mUsedTV.setText("本月已用："+Formatter.formatFileSize(context,(used+beyond)));
        }

    }
    private long getStringTofloat(String str){
        long flow = 0;
        if (!TextUtils.isEmpty(str)){
            if (str.contains("K")){
                String [] split = str.split("K");
                float m = Float.parseFloat(split[0]);
                flow = (long) (m*1024);
            }else if (str.contains("M")){
                String [] split = str.split("M");
                float m = Float.parseFloat(split[0]);
                flow = (long) (m*1024*1024);
            }else if (str.contains("G")){
                String [] split = str.split("G");
                float m = Float.parseFloat(split[0]);
                flow = (long) (m*1024*1024*1024);
            }
        }
        return flow;
    }

    @Override
    protected void onDestroy() {
        if (receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();


    }
}
