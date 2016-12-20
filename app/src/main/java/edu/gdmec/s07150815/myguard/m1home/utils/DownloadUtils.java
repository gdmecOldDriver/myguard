package edu.gdmec.s07150815.myguard.m1home.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

import edu.gdmec.s07150815.myguard.R;

public class DownloadUtils {
    //下载apk的方法
    public void downapk(String url,String targetFile,final MyCallBack myCallBack){
        //创建HttpUtils对象
        HttpUtils httpUtils = new HttpUtils();
        //调用HttpUtils下载的方法下载指定文件11
        httpUtils.download(url, targetFile, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
               myCallBack.onSuccess(arg0);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                myCallBack.onFailure(arg0,arg1);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                myCallBack.onLoading(total,current,isUploading);

            }
        });
    }
}
//借口，用于监听下载动态的接口11
interface MyCallBack{
    //下载成功是调用
    void onSuccess(ResponseInfo<File> arg0);
    //下载失败时调用
    void onFailure(HttpException arg0,String arg1);
    //下载中调用
    void onLoading(long total,long current,boolean isUploading);
}