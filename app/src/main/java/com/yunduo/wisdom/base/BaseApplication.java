package com.yunduo.wisdom.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.yunduo.wisdom.util.cache.DataCenter;

/**
 * Created by parade岁月 on 2019/8/8 10:08
 */
public class BaseApplication  extends MultiDexApplication {

    private static BaseApplication app = null;
    private static Context mContext;
    private DataCenter dataCenter;

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mContext = getApplicationContext();
        /** 初始化腾讯tbs内核 */
        initX5();
    }

    public static void setmContext(Context mContext) {
        app.mContext = mContext;
    }

    public static Context getmContext() {
        return mContext;
    }

    private void initX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                if (arg0){
                    Log.i("wxq","X5内核加载成功");
                }else {
                    Log.i("wxq","X5内核加载失败");
                }
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }
}
