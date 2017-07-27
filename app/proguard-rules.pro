# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/color/soft_app/android/adt/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-ignorewarnings                     # 忽略警告，避免打包时某些警告出现
-optimizationpasses 5               # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontskipnonpubliclibraryclasses    # 是否混淆第三方jar
-dontpreverify                      # 混淆时是否做预校验
-verbose                            # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法
-keepclasseswithmembernames class * {       # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {            # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {            # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { #保持类成员
   public void *(android.view.View);
}
-keepclassmembers enum * {                  # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {    # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}

# 泛型与反射
-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes *Annotation*

-dontwarn android.support.v4.**     #缺省proguard 会检查每一个引用是否正确，但是第三方库里面往往有些不会用到的类，没有正确引用。如果不配置的话，系统就会报错。
-dontwarn android.support.**
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.widget

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.v7.app.Fragment
-keep public class * extends android.support.v7.widget

-dontwarn android.os.**
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class android.os.**{*;}
-keep public class * extends android.app.Fragment
-keep public class * extends com.sqlcrypt.database
-keep public class * extends com.sqlcrypt.database.sqlite
-keep public class * extends com.treecore.**
-keep public class * extends de.greenrobot.dao.**

#四大组件ProGuard配置代码
 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference
 -keep public class com.android.vending.licensing.ILicensingService
 -keep public class * extends android.os.IInterface

-keep class com.alibaba.sdk.android.** {*;}
-keep class com.squareup.retrofit2.** {*;}
-keep class com.squareup.leakcanary.** {*;}
-keep class retrofit.client.** {*;}
-keep class com.squareup.leakcanary.android.noop.** {*;}
-keep class com.jakewharton.** {*;}
-keep class io.reactivex.** {*;}
-keep class rx.** {*;}
-keep class com.jakewharton.rxbinding.** {*;}
-keep class com.facebook.** {*;}
-keep class com.google.code.gson.** {*;}
-keep class com.orhanobut.** {*;}
-dontwarn org.greenrobot.**
-keep class org.greenrobot.** {*;}
-keep class com.github.flavienlaurent.datetimepicker.** {*;}
-keep class cn.trinea.android.common.** {*;}
-keep class com.getui.** {*;}
-keep class com.tencent.bugly.** {*;}
-keep class com.taobao.android.** {*;}
-keep class com.unnamed.b.atv.** {*;}
-keep class com.aspsine.multithreaddownload.** {*;}
-keep class com.zhy.autolayout.** {*;}
-keep class android.support.graphics.drawable.** {*;}
-keep class com.sdk.** {*;}
-keep class org.hamcrest.** {*;}
# 百度混淆配置
-dontwarn com.baidu.**
-keep class com.baidu.**{*;}
#个推的混淆
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }
#听云的混淆配置
# ProGuard configurations for NetworkBench Lens
-keep class com.networkbench.** { *; }
-dontwarn com.networkbench.**
-keepattributes Exceptions, Signature, InnerClasses
# End NetworkBench Lens

#阿里的热修复 start--
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class anet.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
#阿里修复--end
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.alipay.euler.andfix.**{
    *;
}
-keep class com.taobao.hotfix.aidl.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-keep class com.taobao.hotfix.HotFixManager{
    public *;
}
#不混淆butterknife
-keep class com.butterknife.** {*;}
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-dontwarn butterknife.**
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#针对greenDao数据库的配置
-keep class de.greenrobot.dao.** {*;}
#保持greenDao的方法不被混淆 用来保持生成的表名不被混淆
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
 public static java.lang.String TABLENAME;
}
 -keep class **$Properties

 # OkHttp
 -dontwarn com.squareup.okhttp.**
 -keep class com.squareup.okhttp.** {*;}
 -keep class okhttp3.** {*;}
 -keep class okhttp3.internal** {*;}
 -keep class okio.** {*;}
 -keep interface com.squareup.okhttp.** {*;}
 -dontwarn okio.**

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#针对高德地图的混淆配置
-dontwarn com.amap.api**
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#不混淆R文件
-keep class **.R$* { *; }
 #针对新能源的项目配置相关配置代码如下：
-keep class com.yutong.xny.activity.** {*;}
-keep class com.yutong.xny.adapter.** {*;}
-keep class com.yutong.xny.application.** {*;}
-keep class com.yutong.xny.bean.** {*;}
-keep class com.yutong.xny.components.** {*;}
-keep class com.yutong.xny.database.** {*;}
-keep class com.yutong.xny.fragment.** {*;}
-keep class com.yutong.xny.Interface.** {*;}
-keep class com.yutong.xny.net.** {*;}
-keep class com.yutong.xny.receiver.** {*;}
-keep class com.yutong.xny.service.** {*;}
-keep class com.yutong.xny.utils.** {*;}
-keep class com.yutong.xny.view.** {*;}
-keep class org.xclcharts.** {*;}

#百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**