package edu.gdmec.s07150815.myguard.m1home;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m1home.adapter.HomeAdapter;
import edu.gdmec.s07150815.myguard.m2theftguard.LostFindActivity;
import edu.gdmec.s07150815.myguard.m2theftguard.dialog.InterPasswordDialog;
import edu.gdmec.s07150815.myguard.m2theftguard.dialog.SetUpPasswordDialog;
import edu.gdmec.s07150815.myguard.m2theftguard.receiver.MyDeviceAdminReciver;
import edu.gdmec.s07150815.myguard.m2theftguard.utils.MD5Utils;
import edu.gdmec.s07150815.myguard.m3communicationguard.SecurityPhoneActivity;
import edu.gdmec.s07150815.myguard.m4appmanager.AppManagerActivity;
import edu.gdmec.s07150815.myguard.m5virusscan.VirusScanActivity;
import edu.gdmec.s07150815.myguard.m6cleancache.CacheClearListActivity;
import edu.gdmec.s07150815.myguard.m7processmanager.ProcessManagerActivity;
/*import edu.gdmec.s07150815.myguard.m4appmanager.AppManagerActivity;
import edu.gdmec.s07150815.myguard.m5virusscan.VirusScanActivity;
import edu.gdmec.s07150815.myguard.m5virusscan.utils.MD5Utils;*/

public class HomeActivity extends AppCompatActivity {
    //声明GirdView 该控件类似listview
    private GridView gv_home;
    //存储手机防盗密码的sp
    private SharedPreferences msharedPreferences;
    //设备管理员
    private DevicePolicyManager policyManager;
    //申请权限
    private ComponentName componentName;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化布局
        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
        setContentView(R.layout.activity_home);
        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        //初始化GirdView
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        //设置条目的点击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //parent代表gridView,view代表每个条目的view对象，postion代表每个条目的位置
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://点击手机防盗
                        if (isSetUpPassword()) {
                            //弹出输入密码对话框
                            showInterPswdDialog();
                        } else {
                            //弹出设置密码对话框
                            showSetUpPswdDialog();
                        }
                        break;
                    //后续代码需要其他模块完成后才能启用
                    case 1://点击通讯卫士SecuritPhoneActivity
                        startActivity(SecurityPhoneActivity.class);
                        break;
                    case 2://软件管家
                        startActivity(AppManagerActivity.class);
                        break;
                    case 3://手机杀毒
                        startActivity(VirusScanActivity.class);
                        break;
                    case 4://缓存清理
                        startActivity(CacheClearListActivity.class);
                        break;

                    case 5://进程管理
                       startActivity(ProcessManagerActivity.class);
                        break;
                    case 6://流量统计
//                        startActivity(TrafficMonitorActivity.class);
//                        break;
                    case 7 ://高级工具
//                        startActivity(AdvancedToolsActivity.class);
//                        break;
                    case 8 ://设置中心
//                        startActivity(SettingsActivity.class);
//                        break;
                }
            }
        });
        //1.获取设备管理员
        policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        //本行代码需要“手机防盗模块才能启用”
        //2.申请权限,MyDeviceAdminReciever继承自DeviceAdminReciever
        componentName = new ComponentName(this,MyDeviceAdminReciver.class);
        //判断，如果没有权限则申请权限
        boolean active = policyManager.isAdminActive(componentName);
        if (!active) {
            //没有管理员的权限，则获取管理员的权限
            //写到这里
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION
                    , "获取超级管理员权限，用于远程锁屏和清除数据");
            startActivity(intent);
        }


    }


    //弹出设置密码对话框
    //本方法需要完成“手机防盗模块”之后才能启用
    private void showSetUpPswdDialog() {
        final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(HomeActivity
                .this);
        setUpPasswordDialog.setCallBack(new SetUpPasswordDialog.MyCallBack(){
            @Override
            public void ok(){
                String firstPwsd = setUpPasswordDialog.mFirstPWDET.getText().toString().trim();

                String affimrPwsd = setUpPasswordDialog.mAffirmET.getText().toString().trim();
                if(!TextUtils.isEmpty(firstPwsd)&&!TextUtils.isEmpty(affimrPwsd)){
                    if(firstPwsd.equals(affimrPwsd)){
                        //两次密码一致，存储密码
                        savePswd(affimrPwsd);
                        setUpPasswordDialog.dismiss();
                        //显示输入密码对话框
                        showInterPswdDialog();
                    }else{
                        Toast.makeText(HomeActivity.this,"两次密码不一致！",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(HomeActivity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            //这里不知道是不是打错了
            public void cancle(){
                setUpPasswordDialog.dismiss();
            }
        });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();
    }



    //弹出密码对话框
    //本方法需要完成“手机防盗模块”之后才能   启用
    private void showInterPswdDialog(){
        final String password = getPassword();
        final InterPasswordDialog mInPswdDialog = new InterPasswordDialog(HomeActivity.this);
        mInPswdDialog.setCallBack(new InterPasswordDialog.MyCallBack(){


            @Override
            public void comfirm() {
                if(TextUtils.isEmpty(mInPswdDialog.getPassword())){
                    Toast.makeText(HomeActivity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                }else if(password.equals(MD5Utils.encode(mInPswdDialog.getPassword()))){
                    //进入防盗主界面
                    mInPswdDialog.dismiss();
                    startActivity(LostFindActivity.class);
                }else{
                    //对话框消失
                    mInPswdDialog.dismiss();
                    Toast.makeText(HomeActivity.this,"密码错误，请重新输入!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle(){
                mInPswdDialog.dismiss();
            }
        });
        mInPswdDialog.setCancelable(true);
        //让对话框显示
        mInPswdDialog.show();
    }
    //保存密码，本方法需要完成“手机防盗模块”之后才能启用
    private void savePswd(String affirmPswd){
        SharedPreferences.Editor edit = msharedPreferences.edit();
        //为了防止用户隐私被泄露，因此需要加密密码
        edit.putString("PhoneAntiTheftPWD",MD5Utils.encode(affirmPswd));
        edit.commit();
    }
    //获取密码
    private String getPassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD", null);
        if (TextUtils.isEmpty(password)) {
            return "";//返回空？
        }
        return password;
    }

    //判断用户是否设置过手机防盗密码
    private boolean isSetUpPassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD", null);
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    //开启新的activity不关闭自己
    //什么意思
    //cls 新的activity的字节码
    public void startActivity(Class<?> cls) {
        //重写homeactivity内的startactivity方法
        Intent intent = new Intent(HomeActivity.this, cls);
        //即每次调用该方法都打开一次homeactivity/
        startActivity(intent);
    }

    //按两次返回键退出程序
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime )> 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keycode, event);
    }


}
