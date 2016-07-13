package com.github.winter.library.app;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by Winter on 2016/7/9.
 * Description the application of app init Bmob
 * E-mail huang.wqing@gmail.com
 */
public class LibApplication extends Application {
    private static LibApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initBmob();
    }

    private void initBmob() {
        Bmob.initialize(this, "246b1a8c451705f8464aa4a8003a78c8");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

    public synchronized static LibApplication getAppContext() {
        if (null == instance) {
            instance = new LibApplication();
        }
        return instance;
    }
}
