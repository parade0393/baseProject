package com.yunduo.wisdom.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Created by jerry on 2018/2/27.
 */

public class ToastUtils {

    public static void showToast(Context context, int message) {
        if (null != context && message != 0) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void success(Context context,String msg){
        Toast success = Toasty.success(context, msg);
        success.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        success.show();
    }

    public static void error(Context context,String msg){
        Toast success = Toasty.error(context, msg);
        success.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        success.show();
    }

    public static void warning(Context context,String msg){
        Toast success = Toasty.warning(context, msg);
        success.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        success.show();
    }

    public static void normal(Context context,String msg){
        Toast success = Toasty.normal(context, msg);
        success.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        success.show();
    }

    public static void info(Context context,String msg){
        Toast success = Toasty.info(context, msg);
        success.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        success.show();
    }

}
