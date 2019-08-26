package com.yunduo.wisdom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunduo.wisdom.R;
import com.yunduo.wisdom.util.font.FontCache;

/**
 * Created by parade岁月 on 2019/8/17 20:59
 */
public class MenuItemLayout extends FrameLayout {

    private Context mContext;
    private View mView;
    private TextView titleTv, hintTv;
    private ImageView iconImg,redHintImg;
    private String titleText,hintText,jumpUrl,onclickId;
    private int iconImageId;
    public static final int NO_LINE = 0;
    public static final int DIVIDE_LINE = 1;
    public static final int DIVIDE_AREA = 2;
    private int divideLineStyle = NO_LINE;
    private boolean isShowRedHintImg;
    private boolean isShowBottomLine;
    private View divider_line_bottom;

    public boolean isShowRedHintImg() {
        return isShowRedHintImg;
    }

    public void setShowRedHintImg(boolean showRedHintImg) {
        isShowRedHintImg = showRedHintImg;
        redHintImg.setVisibility(showRedHintImg ? VISIBLE : GONE);
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public int getIconImageId() {
        return iconImageId;
    }

    private void setIconImageId(int iconImageId) {
        if (iconImageId != 10000){
            this.iconImageId = iconImageId;
            iconImg.setImageResource(iconImageId);
        }
    }

    public String getHintText() {
        return hintText;
    }

    private void setHintText(String hintText) {
        if (!TextUtils.isEmpty(hintText)){
            this.hintText = hintText;
            hintTv.setText(hintText);
        }
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        if (!TextUtils.isEmpty(titleText)){
            this.titleText = titleText;
            titleTv.setText(titleText);
        }
    }

    public String getOnclickId() {
        return onclickId;
    }

    public void setOnclickId(String onclickId) {
        this.onclickId = onclickId;
    }

    public MenuItemLayout(Context context) {
        this(context,null);
    }

    public MenuItemLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MenuItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.menu_item_layout, this, true);
        titleTv = (TextView) findViewById(R.id.menu_item_text);
        hintTv = (TextView) findViewById(R.id.menu_item_text_hint);
        iconImg = (ImageView) findViewById(R.id.menu_item_icon_img);
        redHintImg = (ImageView) findViewById(R.id.menu_item_red_hint);
        divider_line_bottom =  findViewById(R.id.divider_line_bottom);

        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MenuItemLayout);
        setTitleText(a.getString(R.styleable.MenuItemLayout_title_text));
        setTitleTextSize(a.getDimension(R.styleable.MenuItemLayout_title_text_size,15));
        setTitleTextColor(a.getInteger(R.styleable.MenuItemLayout_title_text_color, Color.parseColor("#9c9c9c")));
        setTitleTextTypeFace(a.getString(R.styleable.MenuItemLayout_title_text_type));
        setHintText(a.getString(R.styleable.MenuItemLayout_hint_text));
        setHintTextSize(a.getDimension(R.styleable.MenuItemLayout_hint_text_size,13));
        setHintTextColor(a.getInteger(R.styleable.MenuItemLayout_hint_text_color, Color.parseColor("#9c9c9c")));
        setHintTextType(a.getString(R.styleable.MenuItemLayout_hint_text_type));
        setIconImageId(a.getResourceId(R.styleable.MenuItemLayout_icon_reference,10000));
        divideLineStyle = a.getInt(R.styleable.MenuItemLayout_divider_line_style, NO_LINE);
        isShowBottomLine = a.getBoolean(R.styleable.MenuItemLayout_bottom_line_show, false);
        setBottomLineVisible(isShowBottomLine);
        setBottomLineColor(a.getInteger(R.styleable.MenuItemLayout_bottom_line_color,Color.parseColor("#9c9c9c")));
        setDivideLine(divideLineStyle);
        a.recycle();
    }

    private void setHintTextType(String typeFace) {
        if (!TextUtils.isEmpty(typeFace)){
            hintTv.setTypeface(FontCache.getTypeface(typeFace,mContext));
        }
    }

    private void setTitleTextTypeFace(String typeFace) {
        if (!TextUtils.isEmpty(typeFace)){
            titleTv.setTypeface(FontCache.getTypeface(typeFace,mContext));
        }
    }

    private void setBottomLineColor(int color) {
        if (isShowBottomLine){
            divider_line_bottom.setBackgroundColor(color);
        }
    }

    private void setBottomLineVisible(boolean isShowBottomLine) {
       divider_line_bottom.setVisibility(isShowBottomLine ? VISIBLE : GONE);
    }

    private void setHintTextColor(int color) {
        hintTv.setTextColor(color);
    }

    private void setTitleTextColor(int color) {
        titleTv.setTextColor(color);
    }

    private void setHintTextSize(float size) {
        if ((int)size == 13){
            hintTv.setTextSize(size);
        }else {
            hintTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        }
    }

    private void setTitleTextSize(float size) {
        if ((int)size == 15){
            titleTv.setTextSize(size);
        }else {
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        }
    }

    private void setDivideLine(int bottomLineStyle) {
        View lineView = findViewById(R.id.divider_line_view);
        View areaView = findViewById(R.id.divider_area_view);
        lineView.setVisibility(GONE);
        areaView.setVisibility(GONE);
        if (bottomLineStyle == DIVIDE_LINE){
            lineView.setVisibility(VISIBLE);
        }else if (bottomLineStyle == DIVIDE_AREA){
            areaView.setVisibility(VISIBLE);
        }
    }

    public TextView getTitleTv() {
        return titleTv;
    }

    public TextView getHintTv() {
        return hintTv;
    }
}
