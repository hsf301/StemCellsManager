# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization is turned off by default. Dex does not like bianHao run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
# 该句是官方混淆配置，打开后报错
#-keepresourcexmlelements manifest/application/meta-data@details=GlideModule

#避免实体类混淆
-keep public class com.huashengfu.StemCellsManager.entity.**{*;}
# 保留Parcelable序列化的类不能被混淆
-keep class * implements android.os.Parcelable{
    public static final android.os.Parcelable$Creator *;
}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#MPAndroidChar
-dontwarn com.github.mikephil.charting
-keep class com.github.mikephil.charting.** { *;}

-dontwarn javax.annotation.**

#极光推送
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

#腾讯tbs服务
-dontwarn com.tencent.**
-keep class com.tencent.** { *; }

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


#3D 地图 V5.0.0之前：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
-keep   class com.amap.api.trace.**{*;}

#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

#Shared SDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.mob.**{*;}
-keep class com.bytedance.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**

#视频剪辑处理
-keep class com.esay.ffmtool.**{*;}

-dontwarn com.**