package edu.gdmec.s07150815.myguard.m9advancedtools;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m2theftguard.utils.MD5Utils;

/**
 * Created by 马达 on 2016/12/27.
 */

public class EnterPswActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mAppIcon;
    private TextView mAppNameTV;
    private EditText mPswET;
    private ImageView mGoImgv;
    private LinearLayout mEnterPswLL;
    private SharedPreferences sp;
    private String password;
    private String packagename;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enter_psw);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        password=sp.getString("PhoneAntiThefePWD",null);
        Intent intent=getIntent();
        packagename=intent.getStringExtra("packagename");
        PackageManager pm=getPackageManager();
        initView();
        try{
            mAppIcon.setImageDrawable(pm.getApplicationInfo(packagename,0).loadIcon(pm));
            mAppNameTV.setText(pm.getApplicationInfo(packagename,0).loadLabel(pm).toString());
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }
    //初始化控件
    private void initView() {
        mAppIcon= (ImageView) findViewById(R.id.imgv_appicon_enterpsw);
        mAppNameTV= (TextView) findViewById(R.id.tv_appname_enterpsw);
        mPswET= (EditText) findViewById(R.id.et_psw_enterpsw);
        mGoImgv= (ImageView) findViewById(R.id.imgv_go_enterpsw);
        mEnterPswLL= (LinearLayout) findViewById(R.id.ll_enterpsw);
        mGoImgv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_go_enterpsw:
                //比较密码
                String inputpsw=mPswET.getText().toString().trim();
                if (TextUtils.isEmpty(inputpsw)){
                    startAnim();
                    Toast.makeText(this,"请输入密码！",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (!TextUtils.isEmpty(password)){
                        if (MD5Utils.encode(inputpsw).equals(password)){
                            //发送自定义的广播消息
                            Intent intent=new Intent();
                            intent.setAction("cn.itcast.mobilesafe.applock");
                            intent.putExtra("packagename",packagename);
                            finish();
                        }else{
                            startAnim();
                            Toast.makeText(this,"密码不正确",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void startAnim() {
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.shake);
        mEnterPswLL.startAnimation(animation);
    }
}
