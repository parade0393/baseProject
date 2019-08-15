package com.yunduo.wisdom.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePicker {

    /*显示时间选择器*/
    public static void pickTime(Context mContext, final TextView textView){
        TimePickerView pickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                textView.setText(dateFormat.format(date));
            }
        })
                .setType(new boolean[]{true,true,true,false,false,false})
                .setContentTextSize(18)
                .build();
        pickerView.show();
    }
}
