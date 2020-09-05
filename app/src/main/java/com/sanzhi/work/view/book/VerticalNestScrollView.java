package com.sanzhi.work.view.book;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * @author : parade
 * date : 2020/8/9
 * description :
 */
public class VerticalNestScrollView extends NestedScrollView {
    public VerticalNestScrollView(@NonNull Context context) {
        super(context);
    }

    public VerticalNestScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //上一次的触摸点的坐标
    private int mLastX, mLastY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//请求父容器不拦截
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;//x轴的偏移量
                int deltaY = y - mLastY;//y轴的偏移量
                if (Math.abs(deltaX) > Math.abs(deltaY)){
                    //x轴的偏移量大于y轴的偏移量，则为水平滑动，此时父容器可以拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                //其它情况：即竖直方向滑动，父容器依然不拦截
                break;
            default:
                break;
        }

        //重置上一次的触摸点坐标
        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }
}
