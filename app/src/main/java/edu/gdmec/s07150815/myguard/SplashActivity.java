package edu.gdmec.s07150815.myguard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import edu.gdmec.s07150815.myguard.m1home.utils.MyUtils;
import edu.gdmec.s07150815.myguard.m1home.utils.VersionUpdateUtils;

public class SplashActivity extends AppCompatActivity {
    private TextView mVersionTV;
    private String mVersion;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mVersion = MyUtils.getVersion(getApplicationContext());
        initView();
        final VersionUpdateUtils updateUtils = new VersionUpdateUtils(mVersion,SplashActivity.this);
        new Thread(){
            @Override
            public void run() {
                updateUtils.getCloudVersion();
            };
        }.start();
    }
    /**初始化控件*/
    private void initView() {
        mVersionTV = (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("版本号"+mVersion);
    }
}
