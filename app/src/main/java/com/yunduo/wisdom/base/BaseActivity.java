package com.yunduo.wisdom.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by parade岁月 on 2019/8/15 10:05
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(getLayoutId());
        afterSetContentView();
    }

    private void afterSetContentView() {
        ButterKnife.bind(this);
        initView();
        initEvent();
        initData();
    }

    protected abstract void initData();

    protected abstract void initEvent();

    protected abstract void initView();

    /**
     * 获取当前的布局ID,加载布局界面
     * 交由子类实现
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    private void unsubscribe() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }
}
