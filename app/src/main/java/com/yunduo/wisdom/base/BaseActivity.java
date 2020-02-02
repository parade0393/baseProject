package com.yunduo.wisdom.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        initEvent();
        initData();
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
    }


    public void getDataFromNet(Observable observable, Observer observer){
        observable.observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
