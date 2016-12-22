package edu.gdmec.s07150815.myguard.m2theftguard;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import edu.gdmec.s07150815.myguard.R;

/**
 * Created by D on 2016/12/20.
 */
public class SetUp1Activity extends BaseSetUpActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up1);
        initView();
    }

    private void initView() {
        //设置第一个小圆点的颜色
        ((RadioButton)findViewById(R.id.rb_first)).setChecked(true);
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(SetUp2Activity.class);
    }

    @Override
    public void showPre() {
        Toast.makeText(this,"当前页面已经是第一页",Toast.LENGTH_SHORT).show();
    }
}
