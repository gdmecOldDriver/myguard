package edu.gdmec.s07150815.myguard.m9advancedtools.fragment;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m9advancedtools.entity.AppInfo;
import edu.gdmec.s07150815.myguard.m9advancedtools.utils.AppInfoParser;
import edu.gdmec.s07150815.myguard.m9advancedtools.adapter.AppLockAdapter;
import edu.gdmec.s07150815.myguard.m9advancedtools.db.dao.AppLockDao;

/**
 * Created by ys on 2016/12/22.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AppLockFragment extends Fragment{
    private TextView mLockTV;
    private ListView mLockLV;
    private AppLockDao dao;
    List<AppInfo> mLockApps=new ArrayList<AppInfo>();
    private AppLockAdapter adapter;
    private Uri uri=Uri.parse("content://com.itcast.mobilesafe.applock");
    private Handler mHandle=new Handler() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    mLockApps.clear();
                    mLockApps.addAll((List<AppInfo>) msg.obj);
                    if (adapter == null) {
                        adapter = new AppLockAdapter(mLockApps, getActivity());
                        mLockLV.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    mLockTV.setText("加锁应用" + mLockApps.size() + "个");
                    break;
            }
        };
    };
    private List<AppInfo> appInfos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_applock,null);
        mLockTV= (TextView) view.findViewById(R.id.tv_lock);
        mLockLV = (ListView) view.findViewById(R.id.lv_lock);

        return view;
    }

    @Override
    public void onResume() {
        dao =new AppLockDao(getActivity());
        appInfos= AppInfoParser.getAppInfos(getActivity());
        fillData();
        initListener();
        getActivity().getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                fillData();
            }
        });
        super.onResume();
    }

    private void initListener() {
        mLockLV.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                //插播一个动画效果
                TranslateAnimation ta=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
                ta.setDuration(300);
                view.startAnimation(ta);
                new Thread(){
                    public void run(){
                        try {
                            Thread.sleep(300);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //删除数据库包名
                                dao.delete(mLockApps.get(position).packageName);
                                //更新界面
                                mLockApps.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    };
                }.start();
            }
        });
    }

    private void fillData() {
        final List<AppInfo>aInfos=new ArrayList<AppInfo>();
        new Thread(){
            public void run(){
                for (AppInfo appInfo:appInfos){
                    if (dao.find(appInfo.packageName)){
                        //已加锁
                        appInfo.isLock=true;
                        //m4 AppInfo内可能缺少isLock
                        aInfos.add(appInfo);
                    }
                }
                Message msg=new Message();
                msg.obj=aInfos;
                msg.what=10;
                mHandle.sendMessage(msg);
            };
        }.start();
    }

}
