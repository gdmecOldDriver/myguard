package edu.gdmec.s07150815.myguard.m5virusscan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import edu.gdmec.s07150815.myguard.R;

/**
 * Created by D on 2016/12/19.
 */
public class VirusScanActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mLastTimeTV;
    private SharedPreferences mSP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virus_scan);
        mSP = getSharedPreferences("config",MODE_PRIVATE);
        copyDB("antivirus.db");
        initView();
    }

    @Override
    protected void onResume() {
        String string = mSP.getString("lastVirusScan","您还没有查杀病毒!");
        mLastTimeTV.setText(string);
        super.onResume();
    }

    private void initView() {
    }

    private void copyDB(final String dbname) {
        new Thread(){
            @Override
            public void run() {
               try{
                   File file = new File(getFilesDir(),dbname);
                   if (file.exists()&&file.length()>0){
                       Log.i("VirusScanActivity","数据库已保存");
                       return;
                   }
                   InputStream is = getAssets().open(dbname);
                   FileOutputStream fos = openFileOutput(dbname,MODE_PRIVATE);
                   byte[] buffer = new byte[1024];
                   int len = 0;
                   while ((len = is.read(buffer))!=-1){
                       fos.write(buffer,0,len);
                   }
                   is.close();
                   fos.close();
               }catch (Exception e){
                   e.printStackTrace();
               }

            };
        }.start();
    }
    /** 初始化控件*/
    /*private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("病毒查杀");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mLastTimeTV = (TextView) findViewById(R.id.tv_lastscantime);
        findViewById(R.id.rl_allscanvirus).setOnClickListener(this);
    }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.rl_allscanvirus:
                startActivity(new Intent(this,VirusScanActivity.class));
                break;
        }
    }

}
