package com.example.customseekbar;

import android.content.res.Resources;
import android.util.TypedValue;

import java.math.BigDecimal;

/**
 * 工具类
 * @author ChenQ
 * @time 2017/6/27 19:09
 * @email：colorchenvip@163.com
 */
public class CustomSeekBarUtils {
    static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    static int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    static String float2String(float value) {
        return new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }

   public static int float2Int(float value) {
        return new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    static float formatFloat(float value) {

        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        // 0 保留整数位 ；BigDecimal.ROUND_HALF_UP 四舍五入模式
        return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
