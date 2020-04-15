package com.sanzhi.work.util.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 封装6.0 需要动态请求的权限
 * Created by lipeilong on 2016/7/26.
 * <p/>
 * 这些权限需要动态申请：
 * ACCESS_COARSE_LOCATION、 ACCESS_FINE_LOCATION、 ADD_VOICEMAIL、 BODY_SENSORS
 * CALL_PHONE、 CAMERA、 GET_ACCOUNTS、 PROCESS_OUTGOING_CALLS、READ_CALENDAR
 * READ_CALL_LOG、 READ_CELL_BROADCASTS、 READ_CONTACTS、 READ_EXTERNAL_STORAGE
 * READ_PHONE_STATE、READ_SMS、RECEIVE_MMS、RECEIVE_SMS、RECEIVE_WAP_PUSH、RECORD_AUDIO
 * SEND_SMS、USE_SIP、WRITE_CALENDAR、WRITE_CALL_LOG、WRITE_CONTACTS、WRITE_EXTERNAL_STORAGE
 */
public class EasyPermissionsUtil {
    /**
     * 读SDCARD 权限
     */
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;

    /**
     * 定位权限
     */
    public static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    /**
     * 录音权限
     */
    public static final int PERMISSION_REQUEST_CODE_AUDIO = 2;

    /**
     * 照相机权限
     */
    public static final int PERMISSION_REQUEST_CODE_CAMERA = 3;

    public static final int PERMISSION_REQUEST_CODE_CAMERA_AND_AUDIO = 4;

    /**
     * 读取手机状态权限（for imei）
     */
    public static final int PERMISSION_REQUEST_CODE_READ_PHONE_STATE = 5;

    public static final int PERMISSION_REQUEST_CODE_INIT = 6;

    /**
     * 检查写权限
     *
     * @param activity
     */
    public static boolean checkWritePermission(Activity activity) {
        // SDK 小于23默认已经授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        Activity context = activity;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            return false;
        }
        return true;
    }

    /**
     * 检查定位权限
     */
    public static boolean checkLocationPermission(Activity activity) {
        // SDK 小于23默认已经授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE_LOCATION);
        return false;
    }

    /**
     * 照相机权限
     */
    public static boolean checkCameraPermission(Activity activity) {
        // SDK 小于23默认已经授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        boolean permissionCamera = ContextCompat.checkSelfPermission(Env.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        if (!permissionCamera) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_CAMERA);
            return false;
        }
        return true;
    }

    /**
     * 检查录音权限
     */
    public static boolean checkAudioPermission(Activity activity) {
        // SDK 小于23默认已经授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        boolean permissionAudio = ContextCompat.checkSelfPermission(Env.getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if (!permissionAudio) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE_AUDIO);
            return false;
        }

        return true;
    }

    /**
     * 检查直播需要的相机和麦克风权限
     */
    public static boolean checkRecordPermission(Activity activity) {
        // SDK 小于23默认已经授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        boolean permissionCamera = ContextCompat.checkSelfPermission(Env.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean permissionAudio = ContextCompat.checkSelfPermission(Env.getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if (!permissionCamera || !permissionAudio) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE_CAMERA_AND_AUDIO);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkReadPhoneStatePermission(Activity activity) {
        // SDK 小于23默认已经授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        boolean permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        if (!permission) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE_READ_PHONE_STATE);
            return false;
        }
        return true;
    }

    public static boolean checkInitPermission(Activity activity) {
        // SDK 小于23默认已经授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // 手机状态和写SDCARD的权限是必须的
        if (EasyPermissions.hasPermissions(activity, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(activity, "需要存储数据到设备！", PERMISSION_REQUEST_CODE_INIT, perms);
            return false;
        }
    }

}
