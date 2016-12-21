package edu.gdmec.s07150815.myguard.m2theftguard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.opengl.ETC1;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.gdmec.s07150815.myguard.R;

/**
 * Created by D on 2016/12/20.
 */
public class InterPasswordDialog extends Dialog implements View.OnClickListener{
  //对话框标题
    private TextView mTitleTV;
    //输入密码文本框
    private EditText mInterET;
    //确定按钮
    private Button mOKBtn;
    //取消按钮
    private Button mCancleBtn;
    //回调接口
    private MyCallBack myCallBack;
    private Context context;
    public InterPasswordDialog(Context context) {
        super(context);
        this.context= context;
    }
    public void setCallBack(MyCallBack myCallBack){
        this.myCallBack = myCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inter_password_dialog);
        initView();
    }

    private void initView() {
        mTitleTV = (TextView) findViewById(R.id.tv_interpwd_title);
        mInterET = (EditText) findViewById(R.id.et_inter_password);
        mOKBtn = (Button) findViewById(R.id.btn_comfirms);
        mCancleBtn = (Button) findViewById(R.id.btn_dismiss);
        mOKBtn.setOnClickListener(this);
        mCancleBtn.setOnClickListener(this);
    }

    public void setTitle(String title){
        if (!TextUtils.isEmpty(title)){
            mTitleTV.setText(title);
        }
    }

    public String getPassword(){
        return mInterET.getText().toString();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_comfirms:
                myCallBack.comfirm();
                break;
            case R.id.btn_dismiss:
                myCallBack.cancle();
                break;
        }
    }

    public interface MyCallBack {
         void comfirm();
         void cancle();
    }
}
