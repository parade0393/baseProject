package com.yunduo.wisdom.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunduo.wisdom.util.eventbus.EventBusUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by parade岁月 on 2019/8/16 0:04
 */
public abstract class BaseFragment extends Fragment {

    private View mRootView;
    protected Context mContext;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mContext != null){
            mContext = context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRootView == null){
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        if (isRegisteredEventBus()){
            EventBusUtil.register(this);
        }
        initViews();
        initDatas();
        setEvents();
        return mRootView;
    }

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initDatas();

    protected abstract void setEvents();

    public final <T extends View> T findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    public void getDataFromNet(Observable observable, Observer observer){
        observable.observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 是否注册时间分发 默认不注册
     * 重写此方法返回true来注册EventBus
     * @return true:注册；false：不注册
     */
    protected boolean isRegisteredEventBus(){
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (isRegisteredEventBus()){
            EventBusUtil.unregister(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRegisteredEventBus()){
            if (!EventBusUtil.isRegistered(this)){
                EventBusUtil.register(this);
            }
        }
    }
}
