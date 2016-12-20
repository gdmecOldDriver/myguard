package edu.gdmec.s07150815.myguard.m4appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m4appmanager.adapter.AppManagerAdapter;
import edu.gdmec.s07150815.myguard.m4appmanager.entity.AppInfo;

/**
 * Created by D on 2016/12/19.
 */
public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener{
    /**手机剩余内存TextView*/
    private TextView mPhoneMemoryTV;
    /**展示SD卡剩余内存TextView*/
    private TextView mSDMemoryTV;
    private ListView mListView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
    private List<AppInfo> systemAppInfos = new ArrayList<AppInfo>();
    private AppManagerAdapter adapter;
    /**接收应用程序卸载成功的广播*/
    private UninstallRececiver receciver;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 10:
                    if (adapter == null){
                        adapter  = new AppManagerAdapter(userAppInfos,systemAppInfos,AppManagerActivity.this);

                    }
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 15:
                    adapter.notifyDataSetChanged();
                    break;
            }
        };
    };
    private TextView mAppNumTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_manager);
        //注册广播
        receciver = new UninstallRececiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(receciver,intentFilter);
        initView();
    }
    /**初始化控件*/
    private void initView() {
      /*  findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_yellow));
        ImageView mLeftImgv = findViewById(R.id.imav_leftbtn);*/
    }

    @Override
    public void onClick(View v) {

    }

    private class UninstallRececiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }


}
