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
    //视图是否已经完成初始化
    private boolean isViewCreated;
    /**是否已经预加载过数据 第一次加载改变状态 防止由于viewpager的缓存页面数据
     *  确保只加载当前tab页面，并且是从空数据开始加载*/
    private boolean isLoad;

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
        //事件监听要放在视图初始化时候和数据初始化之前
        initViews();
        setEvents();
        initDatas();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isViewCreated = true;
        isCanLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    private void isCanLoadData() {
        if (!isViewCreated){
            return;
        }

        //确保只加载当前tab页面，并且是从空数据开始加载 否则由于viewpager的缓存，从相邻tab页切过来的时候，还有页面缓存数据
        if (getUserVisibleHint() && !isLoad){
            lazyLoad();
            isLoad = true;
        }
    }

    /**
     * 设置布局
     * @return fragment的布局资源
     */
    protected abstract int getLayoutId();

    /**
     * 此方法内进行布局绑定、View初始化等操作
     */
    protected abstract void initViews();

    /**
     *在布局加载后执行(有可能布局还不可见)，建议在此方法内加载数据和处理布局显示数据
     */
    protected abstract void initDatas();

    /**
     * 预加载时用这个方法，从空数据开始加载当前页面
     */
    protected abstract void lazyLoad();

    /**
     *建议在此方法内绑定设置监听器、设置执行回调事件等操作
     */
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
