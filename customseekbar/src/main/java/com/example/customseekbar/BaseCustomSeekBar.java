package com.example.customseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.customseekbar.CustomSeekBarUtils.dp2px;
import static com.example.customseekbar.CustomSeekBarUtils.sp2px;

/**
 * 基类
 * @author ChenQ
 * @time 2017/6/27 19:08
 * @email：colorchenvip@163.com
 */
public abstract class BaseCustomSeekBar extends View {
    @IntDef({NONE, TextStyle.BOTTOM_TEXT, TextStyle.TOP_TEXT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextStyle {
        int BOTTOM_TEXT = 0, TOP_TEXT = 1;
    }
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationStyle {
        int SINGLE_STYLE = 0, DOUBLE_STYLE = 1;
    }

    public int mTextStyle;//文字显示模式
    public int mOrientationStyle;//滑动方向

    public float mMin;//seekbar最小值
    public float mMax;//seekbar最大值
    public float mHeight;//默认条目高度
    public float mThumbRadius;//中间拖动条半径
    public int mCompleteColor;//已完成部分颜色
    public int mInCompleteColor;//未完成部分颜色
    public int mThumbColor;//拖动条颜色
    public int mTextColor;//字颜色
    public int mCount;//间隔数量
    public float mTextSpace;//字与center具体
    public float mTextSize;//字大小
    public float mProgressWidth;//进度条的宽度
    public boolean isDownMove;//点击下去是否移动

    public boolean isThumb;//拖动条是否被点击
    public Rect mRectText;
    public float progress;//进度条实际50 对应 画出来的 0.以此推 0->-100,100->100;
    static final int NONE = -1;
    public ProgressChangeListener progressChangeListener;
    public BaseCustomSeekBar(Context context) {
        this(context, null);
    }

    public BaseCustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewDate(context, attrs, defStyleAttr);
        setPaintDefault();
    }
     public void setOnProgressChangeListener(ProgressChangeListener progressChangeListener){
         this.progressChangeListener=progressChangeListener;
     }
    /**
     * 设置画布默认属性
     */
    public abstract void setPaintDefault();



    /***
     * 初始化View信息
     */
    private void initViewDate(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSeekBar, defStyleAttr, 0);
        mMin = a.getFloat(R.styleable.CustomSeekBar_custom_min, 0.0f);
        mMax = a.getFloat(R.styleable.CustomSeekBar_custom_max, 200.0f);
        progress = a.getFloat(R.styleable.CustomSeekBar_custom_progress, 50.0f);
        mHeight = a.getDimension(R.styleable.CustomSeekBar_custom_height, dp2px(20));
        mThumbRadius = a.getDimension(R.styleable.CustomSeekBar_custom_thumb_radius, dp2px(11));
        mCount = a.getInteger(R.styleable.CustomSeekBar_custom_count, 5);
        mCompleteColor = a.getColor(R.styleable.CustomSeekBar_custom_complete_color, ContextCompat.getColor(context, R.color.customCompleteColor));
        mInCompleteColor = a.getColor(R.styleable.CustomSeekBar_custom_incomplete_color, ContextCompat.getColor(context, R.color.customInCompleteColor));
        mThumbColor = a.getColor(R.styleable.CustomSeekBar_custom_thumb_color, ContextCompat.getColor(context, R.color.customThumbColor));
        mTextColor = a.getColor(R.styleable.CustomSeekBar_custom_text_color, ContextCompat.getColor(context, R.color.customTextColor));
        isDownMove = a.getBoolean(R.styleable.CustomSeekBar_custom_down_move, false);
        mTextSize = a.getDimension(R.styleable.CustomSeekBar_custom_text_size, sp2px(140));
        mProgressWidth = a.getDimension(R.styleable.CustomSeekBar_custom_progress_width, dp2px(10));
        mTextSpace = a.getDimension(R.styleable.CustomSeekBar_custom_text_space, dp2px(50));
        mRectText = new Rect();
        int style = a.getInteger(R.styleable.CustomSeekBar_custom_text_style, 0);
        if (style == 1) {
            mTextStyle = BaseCustomSeekBar.TextStyle.TOP_TEXT;
        } else {
            mTextStyle = BaseCustomSeekBar.TextStyle.BOTTOM_TEXT;
        }
        int style2 = a.getInteger(R.styleable.CustomSeekBar_custom_orientation_style, 1);
        if (style2 == 0) {
            mOrientationStyle = OrientationStyle.SINGLE_STYLE;
        } else {
            mOrientationStyle = OrientationStyle.DOUBLE_STYLE;
        }

    }
    void config(CustomConfigBuilder builder) {
        mMin = builder.mMin;
        mMax = builder.mMax;
        mHeight = builder.mHeight;
        mThumbRadius = builder.mThumbRadius;
        mCount = builder.mCount;
        mCompleteColor = builder.mCompleteColor;
        mInCompleteColor = builder.mInCompleteColor;
        mThumbColor = builder.mThumbColor;
        mTextColor = builder.mTextColor;
        isDownMove = builder.isDownMove;
        mTextSize = builder.mTextSize;
        mTextSpace = builder.mTextSpace;
        mTextStyle = builder.mTextStyle;
        progress=builder.progress;
        mOrientationStyle=builder.mOrientationStyle;
        requestLayout();
    }

    public CustomConfigBuilder getConfigBuilder() {
        CustomConfigBuilder customConfigBuilder = new CustomConfigBuilder(this);
        customConfigBuilder.mMin = mMin;
        customConfigBuilder.mMax = mMax;
        customConfigBuilder.mHeight = mHeight;
        customConfigBuilder.mThumbRadius = mThumbRadius;
        customConfigBuilder.mCount = mCount;
        customConfigBuilder.mCompleteColor = mCompleteColor;
        customConfigBuilder.mInCompleteColor = mInCompleteColor;
        customConfigBuilder.mThumbColor = mThumbColor;
        customConfigBuilder.mTextColor = mTextColor;
        customConfigBuilder.mTextSize = mTextSize;
        customConfigBuilder.mTextSpace = mTextSpace;
        customConfigBuilder.mTextStyle = mTextStyle;
        customConfigBuilder.progress=progress;
        customConfigBuilder.mOrientationStyle=mOrientationStyle;
        return customConfigBuilder;
    }
    public interface  ProgressChangeListener{
        void progressChanging(float progress);
        void progressChangeDown(float progress);
    }
}
