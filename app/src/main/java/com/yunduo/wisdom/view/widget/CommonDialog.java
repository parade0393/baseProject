package com.yunduo.wisdom.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/***
 *author: parade岁月
 *date: 2020/1/8 17:20
 *description：基础dialog
 */
public class CommonDialog {

    private Builder builder;

    public CommonDialog(Builder builder) {
        this.builder = builder;
    }

    public void show(){
        builder.show();
    }

    public void dismiss(){
        builder.dismiss();
    }

    public boolean isShowing(){
        return builder.isShowing();
    }

    public View findViewById(int resId){
        return builder.findViewById(resId);
    }

    public static class Builder extends Dialog implements View.OnClickListener {

        private List<Integer> viewIds;//需要设置内容的TextView
        private List<CharSequence> viewValues;//设置的内容
        private Integer gravity;
        private OnAllItemClickListener listener;
        private int[] listenItem;//点击事件监听的控件集合
        private int offsetY;
        private int offsetX;

        public Builder(Context context,@LayoutRes int resId) {
            super(context);
            setContentView(resId);
            initData();

        }

        private void initData() {
            viewIds = new ArrayList<>();
            viewValues = new ArrayList<>();
            listenItem = new int[]{};
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Window window = getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                gravity = gravity == null ? Gravity.CENTER : gravity;
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;//dialog的宽占满全屏，必须配合setBackgroundDrawableResource设置透明使用，否则不会宽度全屏
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;//dialog的高
                params.gravity = gravity;
                params.x = offsetX;
                params.y = offsetY;
                window.setAttributes(params);
            }

            for (int i = 0; i < listenItem.length; i++) {
                findViewById(listenItem[i]).setOnClickListener(this);
            }

            //循环设置文本
            for (int i = 0; i < viewIds.size(); i++) {
                TextView view = findViewById(viewIds.get(i));
                view.setText(viewValues.get(i));
            }
        }

        public Builder setGravity(Integer gravity){
            this.gravity = gravity;
            return this;
        }

        public Builder setoffsetY(int offset){
            this.offsetY = offset;
            return this;
        }
        public Builder setoffsetX(int offset){
            this.offsetX = offset;
            return this;
        }

        public Builder setCanceledOnOut(boolean cancel){
            setCanceledOnTouchOutside(cancel);
            return this;
        }

        public Builder setListenItem(int[] listenItem){
            this.listenItem = listenItem;
            return this;
        }

        public Builder setListener(OnAllItemClickListener listener){
            this.listener = listener;
            return this;
        }

        /**
         * 给TextView设置文本内容 调用show后再设置
         * @param viewId TextView的ID
         * @param value 需要设置的值
         */
        public Builder setText(@IdRes int viewId, CharSequence value){
            viewIds.add(viewId);
            viewValues.add(value);
            return this;
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.handleClick(this,v);
            }
        }

        public interface OnAllItemClickListener{
            void handleClick(Builder builder, View view);
        }

        public CommonDialog build(){
            return new CommonDialog(this);
        }
    }
}
