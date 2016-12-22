package edu.gdmec.s07150815.myguard.m2theftguard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import edu.gdmec.s07150815.myguard.R;

/**
 * Created by D on 2016/12/20.
 */
public class SetUp2Activity extends BaseSetUpActivity implements View.OnClickListener{
    private TelephonyManager mTelephonyManager;
    private Button mBinSIMBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up2);
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        initView();
    }

    private void initView() {
        //设置第二个小圆点的颜色
        ((RadioButton)findViewById(R.id.rb_second)).setChecked(true);
        mBinSIMBtn = (Button) findViewById(R.id.btn_bind_sim);
        mBinSIMBtn.setOnClickListener(this);
        if (isBind()){
            mBinSIMBtn.setEnabled(false);
        }else {
            mBinSIMBtn.setEnabled(true);
        }
    }

    @Override
    public void showNext() {
        if (!isBind()){
            Toast.makeText(this,"你没有绑定SIM卡！",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityAndFinishSelf(SetUp3Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp1Activity.class);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_bind_sim:
                    bindSIM();
                    break;
            }
    }
    /**
     * 绑定SIM卡
     * */
    private void bindSIM() {
        if (!isBind()){
            String simSerialNumber = mTelephonyManager.getSimSerialNumber();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("sim",simSerialNumber);
            editor.commit();
            Toast.makeText(this,"SIM卡绑定成功！",Toast.LENGTH_SHORT).show();
            mBinSIMBtn.setEnabled(false);
        }else {
            Toast.makeText(this,"SIM卡已经绑定！",Toast.LENGTH_SHORT).show();
            mBinSIMBtn.setEnabled(false);
        }
    }

    public boolean isBind() {
        String simString = sp.getString("sim",null);
        if (TextUtils.isEmpty(simString)){
            return false;
        }else {
            return true;
        }
    }
}
