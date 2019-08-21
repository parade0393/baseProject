package com.yunduo.wisdom.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.yunduo.wisdom.R;
import com.yunduo.wisdom.util.font.FontCache;

/**
 * Created by parade岁月 on 2019/8/21 12:22
 * 必填TextView，比如文字前有*，前缀字体可定制，颜色可定制
 */
public class RequiredTextView extends AppCompatTextView {

    private String prefix = "";
    private int prefixColor = Color.RED;
    private String content;
    private String typeFace;

    public RequiredTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public RequiredTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RequiredTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RequiredTextView);
        prefix = array.getString(R.styleable.RequiredTextView_prefix);
        prefixColor = array.getInteger(R.styleable.RequiredTextView_prefix_color, Color.RED);
        content = array.getString(R.styleable.RequiredTextView_android_text);
        typeFace = array.getString(R.styleable.RequiredTextView_type_face);
        if (!TextUtils.isEmpty(typeFace)){
            setTypeface(FontCache.getTypeface(typeFace,context));
        }
        if (TextUtils.isEmpty(prefix)){
            prefix = "*";
        }
        if (TextUtils.isEmpty(content)){
            content = "";
        }

        setTextContent(content);
        array.recycle();
    }

    private void setTextContent(String content) {
        Spannable span = new SpannableString(prefix +" "+ content);
        span.setSpan(new ForegroundColorSpan(prefixColor), 0, prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(span);
    }


}
