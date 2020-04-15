package com.sanzhi.work.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by parade岁月 on 2019/8/8 12:17
 */
public class CommonUtils {

    /**
     * 把字符串解析为Html填充到对应的TextView
     *
     * @param view    需要填充的TextView
     * @param content 需要填充的内容
     */
    public static void parseHtml(TextView view, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
        } else {
            view.setText(Html.fromHtml(content));
        }
    }

    /**
     * 获取URL中的参数
     * @param url URL
     * @param name 参数的key
     * @return
     */
    public static String getParamByUrl(String url, String name) {
        url += "&";
        String pattern = "(\\?|&){1}#{0,1}" + name + "=[a-zA-Z0-9]*(&{1})";

        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(url);
        if (m.find()) {
            System.out.println(m.group(0));
            return m.group(0).split("=")[1].replace("&", "");
        } else {
            return null;
        }
    }

    /**
     * 从assets资源下读取json文件
     * @param context 上下文
     * @param fileName 文件名称 assets下的相对路径
     * @return 读取的json字符串
     */
    public static String getJsonFromeAsset(Context context, String fileName){
        StringBuilder builder = new StringBuilder();

        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void diallPhone(Context context, String phoneNum) {
        if (TextUtils.isEmpty(phoneNum))return;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
}
