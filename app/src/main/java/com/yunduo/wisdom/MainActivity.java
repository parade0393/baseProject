package com.yunduo.wisdom;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yunduo.wisdom.base.BaseActivity;
import com.yunduo.wisdom.constant.Constant;
import com.yunduo.wisdom.util.eventbus.Event;
import com.yunduo.wisdom.util.eventbus.EventBusUtil;

public class MainActivity extends BaseActivity {

    private TextView textView;
    private Button btn_test;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        btn_test = (Button) findViewById(R.id.btn_test);
    }

    @Override
    protected void initEvent() {
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void initData() {
        EventBusUtil.sendEvent(new Event(Constant.EventCode.A,true));
    }

}
