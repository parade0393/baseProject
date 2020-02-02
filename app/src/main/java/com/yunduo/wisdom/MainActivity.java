package com.yunduo.wisdom;

import androidx.fragment.app.Fragment;

import com.next.easynavigation.view.EasyNavigationBar;
import com.yunduo.wisdom.base.BaseActivity;
import com.yunduo.wisdom.constant.Constant;
import com.yunduo.wisdom.util.eventbus.Event;
import com.yunduo.wisdom.util.eventbus.EventBusUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private EasyNavigationBar easy;
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
        easy = (EasyNavigationBar) findViewById(R.id.easy);
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
