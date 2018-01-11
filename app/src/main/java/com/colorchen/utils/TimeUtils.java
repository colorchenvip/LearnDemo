package com.colorchen.utils;

import android.content.Context;
import android.content.res.Resources;

import com.colorchen.App;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by color on 16/8/18 17:40.
 */

public class TimeUtils {
    private static final TimeZone TIMEZONE_SH = TimeZone.getTimeZone("Asia/Shanghai");

    private static SimpleDateFormat sSimpleDateFormat;


    public synchronized static final SimpleDateFormat getSimpleDateFormat() {
        if (sSimpleDateFormat == null) {
            sSimpleDateFormat = new SimpleDateFormat();
            sSimpleDateFormat.setTimeZone(TIMEZONE_SH);
        }
        return sSimpleDateFormat;
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrentDate() {
        final SimpleDateFormat df = getSimpleDateFormat();
        df.applyPattern("yyyy-MM-dd HH:mm");
        return df.format(new Date());
    }

    public static String getFormattedCurrentDate() {
        final SimpleDateFormat df = getSimpleDateFormat();
        df.applyPattern("yyyy_MM_dd_HH_mm");
        return df.format(new Date());
    }

    public static String getCurrentDateNoYear() {
        final SimpleDateFormat df = getSimpleDateFormat();
        df.applyPattern("MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String getExactlyTime(int time) {
        final SimpleDateFormat df = getSimpleDateFormat();
        df.applyPattern("yyyy-MM-dd HH:mm");
        return df.format(new Date(1000l * time));
    }

    public static String getExacttlyTime(long time, String format) {
        final SimpleDateFormat df = getSimpleDateFormat();
        df.applyPattern(format);
        int offset = TimeZone.getDefault().getRawOffset();
        return df.format(new Date(time - offset));
    }

    public static String getExactlyTimeNoYear(int time) {
        final SimpleDateFormat df = getSimpleDateFormat();
        df.applyPattern("M月d日 HH:mm");
        return df.format(new Date(1000l * time));
    }

    public static boolean isWithinSomeDays(long recordTime, int someDays) {
        boolean isWithinSomeDays;
        Date now = new Date();
        long currentTime = now.getTime();
        if (recordTime != -1) {
            if ((currentTime - recordTime) < (long) someDays * 24 * 60 * 60 * 1000) {
                isWithinSomeDays = true;
            } else {
                isWithinSomeDays = false;
            }
        } else {
            isWithinSomeDays = false;
        }
        return isWithinSomeDays;
    }

    /**
     * 判断当前时间是否是当天（从0点到24点）
     *
     * @return
     */
    public static boolean isInSameDay(long time) {
        boolean isInSameDay = false;
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        if (time > todayStart.getTime().getTime() && time <= todayEnd.getTime().getTime()) {
            isInSameDay = true;
        }
        return isInSameDay;
    }

    private static final String BaseTime_DATESTRING = "2012-01-01 ";

    /**
     * 当前时间段 格式HH:mm
     *
     * @param timeStringWithoutDate
     * @return
     */
    public static long getTimeMillisOnBasedTime(String timeStringWithoutDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置格式
        String dateString = BaseTime_DATESTRING + timeStringWithoutDate;// 设定具有指定格式的日期字符串
        Date dataTime = null;
        try {
            dataTime = format.parse(dateString);
            return dataTime.getTime();// 获取当前时区下日期时间对应的时间戳
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 返回某时间的无日期字符串  格式HH:mm
     *
     * @param time
     * @return
     */
    public static String getTimeStringWithoutDate(long time) {
        final SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置格式
        String timeString = df.format(new Date(time));
        return timeString;// 获取当前时区下日期时间对应的时间戳
    }

    /**
     * 返回某时间经basetime换算后的时间
     *
     * @param time
     * @return long
     */
    public static long getTimeMillisOnBasedTime(long time) {
        final SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置格式
        Date date = new Date(time);
        String timeString = df.format(date);
        return getTimeMillisOnBasedTime(timeString);// 获取当前时区下日期时间对应的时间戳
    }

    public static String[] getMicroGalleryTime(long time) {
        String[] result = new String[3];
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
        Date date = new Date(time * 1000L);

        String timeString = df.format(date);
        String[] times = timeString.split("-");

        if (times != null && times.length == result.length) {
            for (int i = 0, length = result.length; i < length; i++) {
                result[i] = times[i];
            }
        } else {
            for (int i = 0, length = result.length; i < length; i++) {
                result[i] = "";
            }
        }

        return result;
    }

    //参数time时间单位为毫秒
    public static String getVoteExpireTime(Context context, long time) {
        SimpleDateFormat df = getSimpleDateFormat();

        Date msgDate = new Date(time);
        Date now = new Date();

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.set(now.getYear(), now.getMonth(), now.getDate());
        Calendar msgCalendar = Calendar.getInstance();
        msgCalendar.set(msgDate.getYear(), msgDate.getMonth(), msgDate.getDate());

        final Resources resources = context.getResources();
        final String year = "年";
        final String month = "月";
        final String day = "日";

        if (msgDate.getYear() == now.getYear()) {
            if (msgCalendar.get(Calendar.DATE) == nowCalendar.get(Calendar.DATE)) {
                final String today = "日";
                df.applyPattern(today + "HH:mm");//今天HH:mm
            } else {
                msgCalendar.add(Calendar.DATE, -1);
                if (msgCalendar.get(Calendar.DATE) == nowCalendar.get(Calendar.DATE)) {
                    final String tomorrow = "明天";
                    df.applyPattern(tomorrow + "HH:mm");//明天HH:mm
                } else {
                    df.applyPattern("M" + month + "d" + day + "HH:mm");//M月d日 HH:mm
                }
            }
        } else {
            msgCalendar.add(Calendar.DATE, -1);
            if (msgCalendar.get(Calendar.DATE) == nowCalendar.get(Calendar.DATE)) {
                final String tomorrow = "明天";
                df.applyPattern(tomorrow + "HH:mm");////明天HH:mm
            } else {
                df.applyPattern("yyyy" + year + "M" + month + "d" + day + " HH:mm");//yyyy年M月d日 HH:mm
            }
        }

        return df.format(msgDate);
    }

    /**
     * 以秒为单位获取当前时间
     *
     * @return
     */
    public static int getCurrentTimeInSecond() {
        Calendar c = Calendar.getInstance();
        long ms = c.getTimeInMillis();
        return (int) (ms / 1000);
    }

    /**
     * 以小时单位获取当前时间
     *
     * @return
     */
    public static int getCurrentTimeInHour() {
        Calendar c = Calendar.getInstance();
        long ms = c.getTimeInMillis() / 60 / 60;
        return (int) (ms / 1000);
    }

    //  秒 为单位
    public static String packTime(long postTime) {
        long time = (System.currentTimeMillis() / 1000) - postTime;
        if (time <= 60) {
            return "1分钟前";
        }
        if (time < 3600) {
            return (time / 60) + "分钟前";
        }
        if (time > 86400) {
            return (time / 86400) + "天前";
        }
        return (time / 3600) + "小时前";
    }

    /**
     * 私聊相关时间显示
     *
     * @param msgTime
     * @return
     */
    public static String getChatTimeDesc(long msgTime, boolean year) {
        Context context = App.context.getApplicationContext();
        Date msgDate = new Date(msgTime * 1000l);
        Date now = new Date();
        final SimpleDateFormat format = getSimpleDateFormat();
        long curTime = now.getTime();
        msgTime = msgTime * 1000;
        long diffTime = (curTime - msgTime) / 1000;
        String strTime;
        // 1小时<=发布时间<6小时 ---> xx小时前
        if (diffTime < 60 * 60 * 24) {
            format.applyPattern("HH:mm");
            strTime = format.format(msgDate);
            if (now.getDate() != msgDate.getDate()) {
                strTime = "昨天" + " " + strTime;
            }
            // 6小时<=发布时间<72小时 --> （昨天/前天）HH：MM
        } else if (diffTime < 60 * 60 * 72) {
            format.applyPattern("HH:mm");
            strTime = format.format(msgDate);
            if (now.getDate() == msgDate.getDate()) {
                // 就是今天，直接返回 HH:mm

            } else {
                Calendar nowCalendar = Calendar.getInstance();
                nowCalendar.set(now.getYear(), now.getMonth(), now.getDate());
                Calendar msgCalendar = Calendar.getInstance();
                msgCalendar.set(msgDate.getYear(), msgDate.getMonth(), msgDate.getDate());
                msgCalendar.add(Calendar.DATE, 1);
                if (msgCalendar.get(Calendar.DATE) == nowCalendar.get(Calendar.DATE)) {
                    strTime = "昨天" + " " + strTime;
                } else {
                    msgCalendar.add(Calendar.DATE, 1);
                    if (msgCalendar.get(Calendar.DATE) == nowCalendar.get(Calendar.DATE)) {
                        strTime = "前天" + " " + strTime;
                    } else {
                        format.applyPattern("M月d日");
                        strTime = format.format(msgDate);
                    }
                }
            }

        } else {
            if (msgDate.getYear() == now.getYear()) {
                if (year) {
                    format.applyPattern("M月d日 HH:mm");
                } else {
                    format.applyPattern("M月d日");
                }
                strTime = format.format(msgDate);
            } else {
                if (year) {
                    format.applyPattern("yyyy年M月d日 HH:mm");
                } else {
                    format.applyPattern("M月d日");
                }
                strTime = format.format(msgDate);
            }
        }

        return strTime;
    }

    public static long getCurrentTimeInMillis() {
        Calendar c = Calendar.getInstance();
        long ms = c.getTimeInMillis();
        return ms;
    }

    /**
     * 时间显示
     * 显示的格式yyyy年M月d日 HH:mm
     *
     * @param msgTime
     * @return
     * @th
     */
    public static String getDateTimeDesc(long msgTime, boolean year) {
        Date msgDate = new Date(msgTime * 1000l);
        final SimpleDateFormat format = getSimpleDateFormat();
        if (year) {
            format.applyPattern("yyyy年M月d日 HH:mm");
        } else {
            format.applyPattern("M月d日");
        }
        String strTime = format.format(msgDate);
        return strTime;
    }


    /**
     * 视频播放器 使用的时间
     *
     * @param timeMs
     * @return
     */
    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
