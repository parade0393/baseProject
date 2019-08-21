package com.yunduo.wisdom.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;
import com.yunduo.wisdom.util.eventbus.EventBusUtil;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by parade岁月 on 2019/8/15 10:05
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(getLayoutId());
        afterSetContentView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView();
    }

    private void afterSetContentView() {
        ButterKnife.bind(this);
        if (isRegisteredEventBus()){
            EventBusUtil.register(this);
        }
        initView();
        initData();
        initEvent();
        if (Build.VERSION.SDK_INT >= 23){
            int permission = ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                checkUpdate();
            }
        }else {
            checkUpdate();
        }
    }

    /**
     * 获取当前的布局ID,加载布局界面
     * 交由子类实现
     * @return
     */
    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    /**
     * 是否注册时间分发 默认不注册
     * 重写此方法返回true来注册EventBus
     * @return true:注册；false：不注册
     */
    protected boolean isRegisteredEventBus(){
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisteredEventBus()){
            EventBusUtil.unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRegisteredEventBus()){
            if (!EventBusUtil.isRegistered(this)){
                EventBusUtil.register(this);
            }
        }
    }

    private void checkUpdate(){
        new PgyUpdateManager.Builder()
                .setForced(false)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
                .setUserCanRetry(false)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
                .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk， 默认为true
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is no new version");
                    }
                    @Override
                    public void onUpdateAvailable(final AppBean appBean) {
                        //有更新回调此方法
                        Log.d("pgyer", "there is new version can update"
                                + "new versionCode is " + appBean.getVersionCode());
                        //调用以下方法，DownloadFileListener 才有效；
                        //如果完全使用自己的下载方法，不需要设置DownloadFileListener

                        new AlertDialog.Builder(BaseActivity.this)
                                .setTitle("更新")
                                .setMessage("有新版本可以更新")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
                                    }
                                }).show();
                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        //更新检测失败回调
                        //更新拒绝（应用被下架，过期，不在安装有效期，下载次数用尽）以及无网络情况会调用此接口
                        Log.e("pgyer", "check update failed ", e);
                    }
                })
                .register();
    }


    public void getDataFromNet(Observable observable, Observer observer){
        observable.observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
