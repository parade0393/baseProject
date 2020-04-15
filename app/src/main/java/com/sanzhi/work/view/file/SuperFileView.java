package com.sanzhi.work.view.file;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.TbsReaderView;
import com.sanzhi.work.util.log.TLog;

import java.io.File;

/**
 * Created by 12457 on 2017/8/29.
 */

public class SuperFileView extends FrameLayout implements TbsReaderView.ReaderCallback {

    private static String TAG = "SuperFileView";
    private TbsReaderView mTbsReaderView;
    private int saveTime = -1;
    private Context context;

    public SuperFileView(Context context) {
        this(context, null, 0);
    }

    public SuperFileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperFileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTbsReaderView = new TbsReaderView(context, this);
        this.addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
        this.context = context;
    }


    private OnGetFilePathListener mOnGetFilePathListener;


    public void setOnGetFilePathListener(OnGetFilePathListener mOnGetFilePathListener) {
        this.mOnGetFilePathListener = mOnGetFilePathListener;
    }


    private TbsReaderView getTbsReaderView(Context context) {
        return new TbsReaderView(context, this);
    }

    public boolean  isSupportExt(String fileExtension){
        if (this.mTbsReaderView == null)
            this.mTbsReaderView = getTbsReaderView(context);
        boolean var3 = mTbsReaderView.isSupportExt(context, fileExtension);
        if (!var3) {
            Log.e("TbsReaderView", "not supported by:" + fileExtension);
            return false;
        }
        return  var3;
    }
    public boolean displayFile(File mFile) {
        boolean bool=false;
        if (mFile != null && !TextUtils.isEmpty(mFile.toString())) {
            //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
            String bsReaderTemp = "/storage/emulated/0/TbsReaderTemp";
            File bsReaderTempFile =new File(bsReaderTemp);

            if (!bsReaderTempFile.exists()) {
                TLog.d("准备创建/storage/emulated/0/TbsReaderTemp！！");
                boolean mkdir = bsReaderTempFile.mkdir();
                if(!mkdir){
                    TLog.e("创建/storage/emulated/0/TbsReaderTemp失败！！！！！");
                }
            }

            //加载文件
            Bundle localBundle = new Bundle();
            TLog.d(mFile.toString());
            localBundle.putString("filePath", mFile.toString());

            localBundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");

            if (this.mTbsReaderView == null)
                this.mTbsReaderView = getTbsReaderView(context);
            bool = this.mTbsReaderView.preOpen(getFileType(mFile.toString()), false);
            TLog.d(bool+"");
            if (bool) {
                this.mTbsReaderView.openFile(localBundle);
            }

        } else {
            TLog.e("文件路径无效！");
        }
        return bool;
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            TLog.d(TAG, "paramString---->null");
            return str;
        }
        TLog.d(TAG, "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            TLog.d(TAG, "i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        TLog.d(TAG, "paramString.substring(i + 1)------>" + str);
        return str;
    }

    public void show() {
        if(mOnGetFilePathListener!=null){
            mOnGetFilePathListener.onGetFilePath(this);
        }
    }

    /***
     * 将获取File路径的工作，“外包”出去
     */
    public interface OnGetFilePathListener {
        void onGetFilePath(SuperFileView mSuperFileView2);
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
        TLog.e("****************************************************" + integer);
    }

    public void onStopDisplay() {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }
}
