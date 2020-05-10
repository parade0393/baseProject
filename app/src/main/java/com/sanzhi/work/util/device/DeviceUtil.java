package com.sanzhi.work.util.device;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/***
 *author: parade岁月
 *date:  2020/5/9 10:10
 *description：设备/应用相关信息工具
 */
public class DeviceUtil {

    /**
     * 返回版本名字
     * 对应build.gradle中的versionName
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 检查应用版本是否是线上最新的
     * @param context context
     * @param onLineVersion  线上应用的版本
     * @return true 最新，应用不需要跟新  false应用需要更新
     */
    public static boolean checkNewVersion(Context context,String onLineVersion){
        String buildVersion = getVersionName(context);
        if (buildVersion.equals(onLineVersion)) return true;
        String[] buildVersionArray = buildVersion.split("\\.");
        String[] onLineVersionArray = onLineVersion.split("\\.");

        int index = 0;
        //获取两个数组的长度的最小值
        int minLen = Math.min(buildVersionArray.length, onLineVersionArray.length);
        int diff = -1;
        //循环判断在minLen内，两个版本的的不同，并记录不同的索引
        while (index < minLen
                && (diff = Integer.parseInt(buildVersionArray[index])
                - Integer.parseInt(onLineVersionArray[index])) == 0) {
            index++;
        }

        //diff肯定不为-1
        if (diff == 0){
            //在最小长度内一致，则比较长度大的版本，如果长度大的是线上版本，则需要更新，反之，则不需要
            return buildVersion.length() > minLen;
        }else {
            return diff > 0;
        }
    }
}
