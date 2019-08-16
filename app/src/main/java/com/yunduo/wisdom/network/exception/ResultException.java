package com.yunduo.wisdom.network.exception;

/**
 * Created by jerry on 2018/2/14.
 */

public class ResultException extends RuntimeException {
    private String code;
    private int errorCode;
    private String message;
    private String requestUri;
    public ResultException(int errCode, String msg) {
        super(msg);
        this.errorCode = errCode;
        this.message=msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage () {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }
}