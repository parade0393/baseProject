package com.sanzhi.work.network;


import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sanzhi.work.R;
import com.sanzhi.work.view.widget.CommonDialog;
import com.sanzhi.work.network.exception.ResultException;
import com.sanzhi.work.util.ToastUtils;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by parade岁月 on 2019/8/16 22:45
 */
public abstract class BaseObserver<T> implements Observer<T> {

    protected Disposable disposable;
    private Context mContext;
    private CommonDialog loadingDialog;
    private boolean showDialog;

    public BaseObserver(Context context, boolean showDialog) {
        this(context, showDialog, "加载中...");
    }

    protected BaseObserver(Context context, boolean showDialog, String msg) {
        this.mContext = context;
        loadingDialog = new CommonDialog.Builder(mContext, R.layout.dialog_request_loading)
                .setCanceledOnOut(false).build();
        TextView tv = (TextView) loadingDialog.findViewById(R.id.tv_loading_tip);
        tv.setText(msg + "...");
        this.showDialog = showDialog;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onError(Throwable e) {
        Log.w("Subscriber onError", e);
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ResultException errorResponse = null;

            try {
                String string = httpException.response().errorBody().string();
                if (validate(string)){
                    errorResponse = new Gson().fromJson(string, ResultException.class);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            switch (errorResponse.getErrorCode()) {
                case 401:
//                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    break;
                case 400:
                    Toast.makeText(mContext, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mContext, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
            // We had non-2XX http error

        } else if (e instanceof EOFException){

        }else if (e instanceof SocketTimeoutException){
            ToastUtils.warning(mContext,"连接超时，稍后重试");
        }else if (e instanceof IOException) {
            // A network or conversion error happened
            Toast.makeText(mContext, mContext.getString(R.string.cannot_connected_server), Toast.LENGTH_SHORT).show();
        } else if (e instanceof ResultException) {
            ResultException exception = (ResultException) e;
            switch (exception.getErrorCode()) {
                case 401:
//                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    break;
                case 400:
                default:
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    break;

            }

        }


        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public void onComplete() {
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    public static boolean validate(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        return true;
    }
}
