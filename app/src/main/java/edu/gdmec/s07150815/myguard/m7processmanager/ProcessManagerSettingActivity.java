package edu.gdmec.s07150815.myguard.m7processmanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m7processmanager.utils.SystemInfoUtils;

public class ProcessManagerSettingActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private ToggleButton mShowSysAppsTgb;
    private ToggleButton mKillProcessTgb;
    private SharedPreferences mSP;
    private boolean running;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_process_manager_setting);
        mSP = getSharedPreferences("config",MODE_PRIVATE);
        initView();
    }
    //初始化控件
    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_green));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        ((TextView)findViewById(R.id.tv_title)).setText("进程管理设置");
        mShowSysAppsTgb = (ToggleButton) findViewById(R.id.tgb_showsys_process);
        mKillProcessTgb = (ToggleButton) findViewById(R.id.tgb_killproces_locksrceen);
        mShowSysAppsTgb.setChecked(mSP.getBoolean("showSystemProcess",true));
        running = SystemInfoUtils.isServiceRunning(this,"cn.itcast.mobilesafe.chapter07.service.autoKillProcessService");
        mKillProcessTgb.setChecked(running);
        initListener();
    }
//初始化监听
    private void initListener(){
        mKillProcessTgb.setOnCheckedChangeListener(this);
        mShowSysAppsTgb.setOnClickListener(this);//11111
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
