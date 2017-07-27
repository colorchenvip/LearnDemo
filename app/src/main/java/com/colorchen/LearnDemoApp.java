package com.colorchen;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
/**
 * application
 * @author ChenQ
 * @time 2017/7/19 17:00
 * @email：colorchenvip@163.com
 */
public class LearnDemoApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
//        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
