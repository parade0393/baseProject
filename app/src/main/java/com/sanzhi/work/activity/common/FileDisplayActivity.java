package com.sanzhi.work.activity.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.sanzhi.work.BuildConfig;
import com.sanzhi.work.R;
import com.sanzhi.work.base.BaseActivity;
import com.sanzhi.work.util.ToastUtils;
import com.sanzhi.work.util.file.FileUtil;
import com.sanzhi.work.util.file.WpsModel;
import com.sanzhi.work.util.log.TLog;
import com.sanzhi.work.util.permission.EasyPermissionsUtil;
import com.sanzhi.work.view.file.SuperFileView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class FileDisplayActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tv_right_text)
    TextView btnRight;
    private String TAG = "FileDisplayActivity";
    SuperFileView mSuperFileView;

    String filePath;
    //    LinearLayout iv_back;
    TextView tv_title;
    String path;
    private ProgressDialog pd;

    private int FileLength;
    private int DownedFileLength = 0;

    private HttpURLConnection connection;
    private FileOutputStream outputStream;
    private File file;
    private boolean isCached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);
        ButterKnife.bind(this);
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平加载进度条

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    public void init() {

        isCached = true;
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDisplayActivity.this.finish();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("文件预览");
        btnRight.setText("下载");
        btnRight.setVisibility(View.VISIBLE);
        mSuperFileView = (SuperFileView) findViewById(R.id.mSuperFileView);
        mSuperFileView.setOnGetFilePathListener(new SuperFileView.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView mSuperFileView2) {
                getFilePathAndShowFile(mSuperFileView2);
            }
        });

        Intent intent = this.getIntent();
        path = (String) intent.getSerializableExtra("path");

        if (!TextUtils.isEmpty(path)) {
            TLog.d(TAG, "文件path:" + path);
            setFilePath(path);
        }
        mSuperFileView.show();
    }

    private void getFilePathAndShowFile(SuperFileView mSuperFileView2) {


        if (getFilePath().contains("http")) {//网络地址要先下载

            downLoadFromNet(getFilePath(), mSuperFileView2);

        } else {
            mSuperFileView2.displayFile(new File(getFilePath()));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        TLog.d("FileDisplayActivity-->onDestroy");
        if (mSuperFileView != null) {
            mSuperFileView.onStopDisplay();
        }
    }


    public static void show(Context context, String url) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", url);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    private String getFilePath() {
        return filePath;
    }

    private void downLoadFromNet(final String url, final SuperFileView mSuperFileView2) {
        //1.网络下载、存储路径、
        File cacheFile = getDownloadFile(url);
        if (cacheFile.exists()) {
            if (cacheFile.length() <= 0) {
                TLog.d(TAG, "删除空文件！！");
                cacheFile.delete();
            }
        }
        Thread thread = new Thread() {
            public void run() {
                try {
                    DownFile(url, mSuperFileView2);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        };
        thread.start();
    }

    private SuperFileView curFileView;
    private File fileCache;

    private void DownFile(final String urlString, final SuperFileView mSuperFileView2) {
        InputStream inputStream = null;
        curFileView = mSuperFileView2;
        try {
            String fileName = getFileName(urlString);
            URL url = new URL((urlString.substring(0, urlString.length() - fileName.length()) + URLEncoder.encode(fileName, "utf-8")).replaceAll("\\+", "%20"));
            connection = (HttpURLConnection) url.openConnection();
            if (connection.getReadTimeout() == 5) {
                Log.i("---------->", "当前网络有问题");
                return;
            }
            connection.connect();
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
            } else {
                Message message3 = new Message();
                message3.what = 3;
                handlerCache.sendMessage(message3);
            }

        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
         * 文件的保存路径和和文件名其中Nobody.mp3是在手机SD卡上要保存的路径，如果不存在则新建
         */
        File filet = getDownloadDir();

        if (!filet.exists()) {
            filet.mkdirs();
            TLog.d(TAG, "创建缓存目录： " + filet.toString());
        }

        fileCache = getDownloadFile(urlString);
        if (!fileCache.exists()) {
            try {
                fileCache.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*
             * 向SD卡中写入文件,用Handle传递线程
             */
            Message message = new Message();
            int len = 0;
            try {

                outputStream = new FileOutputStream(fileCache);
                byte[] buffer = new byte[1024 * 4];
                FileLength = connection.getContentLength();
                message.what = 0;
                handlerCache.sendMessage(message);
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                    DownedFileLength += len;
                    Log.i("缓存-------->", DownedFileLength + "");
                    Message message1 = new Message();
                    message1.what = 1;
                    handlerCache.sendMessage(message1);
                }
                outputStream.flush();
                Message message2 = new Message();
                message2.what = 2;
                handlerCache.sendMessage(message2);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Message message2 = new Message();
            message2.what = 2;
            handlerCache.sendMessage(message2);
        }
    }


    /***
     * 获取缓存目录
     *
     * @param
     * @return
     */
    private File getCacheDir(String url) {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/");
    }

    private File getDownloadDir() {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunduooa_down/");

    }

    /***
     * 绝对路径获取缓存文件
     *
     * @param url
     * @return
     */
    private File getCacheFile(String url) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/"
                + getFileName(url));
        TLog.d(TAG, "缓存文件 = " + cacheFile.toString());
        return cacheFile;
    }

    private File getDownloadFile(String url) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunduooa_down/"
                + FileUtil.getFileName(url));
        TLog.d(TAG, "缓存文件 = " + cacheFile.toString());
        return cacheFile;
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
//        String fileName = Md5Tool.hashKey(url) + "." + getFileType(url);
        String fileName = FileUtil.getFileName(url);
        return fileName;
    }

    /***
     * 获取文件类
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

    @OnClick(R.id.tv_right_text)
    public void onViewClicked() {
        downloadFile3(path);

    }

    private void downloadFile3(final String url) {
        DownedFileLength = 0;
        // TODO Auto-generated method stub
        Thread thread = new Thread() {
            public void run() {
                try {
                    DownFile(url);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        };
        thread.start();
    }

    private void DownFile(String urlString) {

        /*
         * 连接到服务器
         */
        InputStream inputStream = null;
        try {
            String fileName = getFileName(urlString);
            URL url = new URL((urlString.substring(0, urlString.length() - fileName.length()) + URLEncoder.encode(fileName, "utf-8")).replaceAll("\\+", "%20"));
            connection = (HttpURLConnection) url.openConnection();
            if (connection.getReadTimeout() == 5) {
                Log.i("---------->", "当前网络有问题");
                return;
            }
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
            } else {
                Message message3 = new Message();
                message3.what = 3;
                handler.sendMessage(message3);
            }
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
         * 文件的保存路径和和文件名其中Nobody.mp3是在手机SD卡上要保存的路径，如果不存在则新建
         */
        File filet = getDownloadDir();

        if (!filet.exists()) {
            filet.mkdirs();
            TLog.d(TAG, "创建下载目录： " + filet.toString());
        }
        file = getDownloadFile(urlString);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*
             * 向SD卡中写入文件,用Handle传递线程
             */
            Message message = new Message();
            int len = 0;
            try {
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 4];
                FileLength = connection.getContentLength();
                message.what = 0;
                handler.sendMessage(message);
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                    DownedFileLength += len;
                    Log.i("下载-------->", DownedFileLength + "");
                    Message message1 = new Message();
                    message1.what = 1;
                    handler.sendMessage(message1);
                }
                outputStream.flush();
                Message message2 = new Message();
                message2.what = 2;
                handler.sendMessage(message2);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Message message2 = new Message();
            message2.what = 2;
            handler.sendMessage(message2);
        }
    }

    private Handler handlerCache = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        pd.setMessage("缓存中...");
                        pd.setMax((FileLength / 1024));
                        break;
                    case 1:
                        pd.setProgress((DownedFileLength / 1024));
                        pd.setProgressNumberFormat("%1d Kb /%2d Kb");
                        pd.show();
                        break;
                    case 2:
                        pd.dismiss();
                        String fileExtension=FileUtil.getFileExtension(fileCache);
                        if (curFileView.isSupportExt(fileExtension)){
                            if (!curFileView.displayFile(fileCache)) {
                                showOpenThird();
                            }
                        }else
                        {
                            FileUtil.openFileByPath(FileDisplayActivity.this,fileCache);
//                            finish();
//                            ToastUtils.showToast(FileDisplayActivity.this,"文件下载完成，当前文件不支持预览",Toast.LENGTH_LONG);
                        }

                        break;
                    case 3:
                        finish();
                        ToastUtils.showToast(FileDisplayActivity.this, "附件异常！请联系管理员",Toast.LENGTH_LONG);
                        break;
                    default:
                        break;
                }
            }
        }

    };

    //直接打开wps
    private void showOpenThird() {
        isInstall();
        if (isInstall) {
            openFile(FileDisplayActivity.this, fileCache);
        } else {
            // 从市场上下载
            Uri uri = Uri.parse("market://details?id=cn.wps.moffice_eng");
            // 直接从指定网址下载
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        pd.setMessage("正在下载...");
                        pd.setMax(FileLength / 1024);
                        break;
                    case 1:
                        pd.setProgress(DownedFileLength / 1024);
                        pd.setProgressNumberFormat("%1d Kb /%2d Kb");
                        pd.show();
                        break;
                    case 2:
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_LONG).show();
                        isInstall();
                        AlertDialog.Builder builder = new AlertDialog.Builder(FileDisplayActivity.this);
                        builder.setTitle("下载完成");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("打开", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isInstall) {
                                    openFile(FileDisplayActivity.this, file);
//                                    FileUtil.openFileByPath(FileDisplayActivity.this,file);
                                } else {
                                    // 从市场上下载
                                    Uri uri = Uri.parse("market://details?id=cn.wps.moffice_eng");
                                    // 直接从指定网址下载
                                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(it);
                                }

                            }
                        });
                        builder.show();
                        break;
                    case 3:
                        finish();
                        ToastUtils.showToast(FileDisplayActivity.this, "附件异常！请联系管理员",Toast.LENGTH_LONG);
                        break;
                    default:
                        break;
                }
            }
        }

    };

    boolean openFile(Context context, File file) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 打开模式
        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 关闭时是否发送广播
        bundle.putString(WpsModel.THIRD_PACKAGE, getPackageName()); // 第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// 清除打开记录
        // bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);
        if (file == null || !file.exists()) {
            System.out.println("文件为空或者不存在");
            return false;
        }
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, mimetype);
        } else {
            intent.setDataAndType(Uri.fromFile(file), mimetype);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtras(bundle);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            System.out.println("打开wps异常：" + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    boolean isInstall = false;

    // 判断是否安装wps
    public void isInstall() {

        List<PackageInfo> list = getPackageManager().getInstalledPackages(
                PackageManager.GET_PERMISSIONS);

        StringBuilder stringBuilder = new StringBuilder();


        for (PackageInfo packageInfo : list) {
            stringBuilder.append("package name:" + packageInfo.packageName
                    + "\n");
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            stringBuilder.append("应用名称:"
                    + applicationInfo.loadLabel(getPackageManager()) + "\n");
            if (packageInfo.permissions != null) {

                for (PermissionInfo p : packageInfo.permissions) {
                    stringBuilder.append("权限包括:" + p.name + "\n");
                }
            }
            stringBuilder.append("\n");
            if ("cn.wps.moffice_eng".equals(packageInfo.packageName)) {
                isInstall = true;
            }
        }
        System.out.println(isInstall);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 请求权限
        if (EasyPermissionsUtil.checkInitPermission(this)) {
            doTaskAfterPermission();
        }
    }

    @AfterPermissionGranted(EasyPermissionsUtil.PERMISSION_REQUEST_CODE_INIT)
    private void doTaskAfterPermission() {
        if (!isCached)
            init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i(TAG, "获取成功的权限" + perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i(TAG, "获取失败的权限" + perms + requestCode);
        if (requestCode == EasyPermissionsUtil.PERMISSION_REQUEST_CODE_INIT) {
            // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
            // This will display a dialog directing them to enable the permission in app settings.
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//                PermissionUtil.showPermissionDetail(this, "应用必须权限", true);
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(localIntent);
                ToastUtils.showToast(this, "必要的权限被禁止，请到应用管理授予权限！");
            } else {
                EasyPermissionsUtil.checkInitPermission(this);
            }
        }
    }
}
