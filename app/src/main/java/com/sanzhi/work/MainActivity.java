package com.sanzhi.work;

import androidx.fragment.app.Fragment;

import com.sanzhi.work.base.BaseActivity;
import com.sanzhi.work.constant.Constant;
import com.sanzhi.work.util.eventbus.Event;
import com.sanzhi.work.util.eventbus.EventBusUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


    private String[] tabs;
    private int[] normalICon;
    private int[] selectIcon;
    private List<Fragment> fragments;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        EventBusUtil.sendEvent(new Event(Constant.EventCode.A,true));
        fragments = new ArrayList<>();
    }

}
