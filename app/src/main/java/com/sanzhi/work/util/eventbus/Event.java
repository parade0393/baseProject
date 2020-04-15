package com.sanzhi.work.util.eventbus;

/**
 * Created by parade岁月 on 2019/8/21 9:28
 */
public class Event<T> {
    private int code;
    private T data;
    private boolean flag;

    public Event(int code) {
        this.code = code;
    }

    public Event(int code, boolean flag) {
        this.code = code;
        this.flag = flag;
    }

    public Event(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
