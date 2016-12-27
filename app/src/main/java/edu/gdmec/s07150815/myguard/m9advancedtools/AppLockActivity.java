package edu.gdmec.s07150815.myguard.m9advancedtools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m9advancedtools.fragment.AppLockFragment;
import edu.gdmec.s07150815.myguard.m9advancedtools.fragment.AppUnLockFragment;

/**
 * Created by 马达 on 2016/12/27.
 */

public class AppLockActivity extends FragmentActivity implements View.OnClickListener{
    private ViewPager mAppViewPager;
    List<Fragment> mFragments=new ArrayList<Fragment>();
    private TextView mLockTV;
    private TextView mUnLockTV;
    private View slideLockView;
    private View slideUnLockView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_lock);
        initView();
        initLinstener();
    }

    private void initLinstener() {
        mAppViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int arg0) {
                if (arg0==0){
                    slideUnLockView.setBackgroundResource(R.drawable.slide_view);
                    slideLockView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    //未加锁
                    mLockTV.setTextColor(getResources().getColor(R.color.black));
                    mUnLockTV.setTextColor(getResources().getColor(R.color.bright_red));
                }else {
                    slideLockView.setBackgroundResource(R.drawable.slide_view);
                    slideUnLockView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    //已加锁
                    mLockTV.setTextColor(getResources().getColor(R.color.bright_red));
                    mUnLockTV.setTextColor(getResources().getColor(R.color.black));
                }

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ImageView mLeftImgv= (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView)findViewById(R.id.tv_title)).setText("程序锁");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        mAppViewPager= (ViewPager) findViewById(R.id.vp_applock);
        mLockTV= (TextView) findViewById(R.id.tv_lock);
        mUnLockTV= (TextView) findViewById(R.id.tv_unlock);
        mLockTV.setOnClickListener(this);
        mUnLockTV.setOnClickListener(this);
        slideLockView=findViewById(R.id.view_slide_lock);
        slideUnLockView=findViewById(R.id.view_slide_unlock);
        AppUnLockFragment unLock=new AppUnLockFragment();
        AppLockFragment lock=new AppLockFragment();
        mFragments.add(unLock);
        mFragments.add(lock);
        mAppViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.tv_lock:
                mAppViewPager.setCurrentItem(1);
                break;
            case R.id.tv_unlock:
                mAppViewPager.setCurrentItem(0);
                break;
        }
    }

    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
