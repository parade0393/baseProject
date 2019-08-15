package com.yunduo.wisdom.util;

import android.os.Build;
import android.text.Html;
import android.widget.TextView;

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
}
