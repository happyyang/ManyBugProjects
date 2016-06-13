package bone.com.manybugproject.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;



/**
 * 类名：AppUtils
 *
 * @author 赵然<br/>
 *         实现的主要功能: 创建日期：2014-11-24 修改者，修改日期，修改内容。
 */
public class AppUtils {

    private static final String TAG = AppUtils.class.getSimpleName();

    /**
     * SDK 版本号
     *
     * @return
     */
    public static String getSdkVersion() {
        String sdk = Build.VERSION.SDK;
        if (TextUtils.isEmpty(sdk)) {
            sdk = "sdk";
        }
        return sdk;
    }

    /**
     * android 版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        String system = Build.VERSION.RELEASE;
        if (TextUtils.isEmpty(system)) {
            system = "os_info";
        }
        return system;
    }

    /**
     * 手机版本号
     *
     * @return
     */
    public static String getModelVersion() {
        String model = Build.MODEL;
        if (TextUtils.isEmpty(model)) {
            model = "model";
        }
        return model;
    }

    /**
     * App版本号
     *
     * @return
     */
    public static String getAppVersion(Context context) {
        String version = "";
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            version = "version";
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取app的versionCode
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();

            PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            String versionName = pinfo.versionName;
            int versionCode = pinfo.versionCode;

            return versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取屏幕宽高信息
     *
     * @param activity
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreenDisplay(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕尺寸
     *
     * @return
     */
    public static String getScreen(Activity activity) {
        DisplayMetrics dm = getScreenDisplay(activity);
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        String screen = dm.widthPixels + "x" + dm.heightPixels;
        if (TextUtils.isEmpty(screen)) {
            screen = "screen";
        }
        return screen;
    }

    /**
     * 手机运营商
     *
     * @return
     */
    public static String getOp(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String providersName = null;
        String IMSI = tm.getSubscriberId();
        try {
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                providersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                providersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                providersName = "中国电信";
            } else {
                providersName = "op";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(providersName)) {
            providersName = "op";
        }
        return providersName;
    }

    /**
     * 获取手机号(一般拿不到)
     *
     * @param context
     * @return
     */
    public static String getNativePhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber = null;
        NativePhoneNumber = tm.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * 获取产品名
     *
     * @return
     */
    public static String getProduct() {

        return "huijiayou_android";
    }

    /**
     * 获取网络状态
     *
     * @return
     */
    public static String getNetType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return "net_type";
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {

            return "wifi";

        } else {
            return "cellular";
        }
    }

    /**
     * 魅族的SmartBar
     *
     * @return
     */
    public static boolean hasSmartBar() {
        try {
            // 新型号可用反射调用Build.hasSmartBar()
            Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        // 反射不到Build.hasSmartBar()，则用Build.DEVICE判断
        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }

        return false;
    }

    public static boolean isFlym3() {
        if (Build.DEVICE.equals("mx2") || Build.DEVICE.equals("mx3")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }
        return false;
    }

    public static boolean isMi3OrMi4OS() {
        return "cancro".equals(Build.DEVICE);
    }

    public static void setConfigValue(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("setting_config", Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).commit();
    }

    ;

    public static String getConfigValue(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("setting_config", Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    ;

    /**
     * 屏幕是否是亮的
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    /**
     * 程序是否在后台运行
     */
    public static boolean isBackgroudApp(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (null == activityManager) {
            return false;
        }

        List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(20);
        Iterator<RunningTaskInfo> iterator = runningTasks.iterator();
        RunningTaskInfo runningTaskInfo;
        while (iterator.hasNext()) {
            runningTaskInfo = (RunningTaskInfo) iterator.next();
            if (packageName.equals(runningTaskInfo.topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 该应用是否正在运行
     */
    public static boolean isTopApp(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取手机ip地址
     *
     * @return
     */
    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    // 判断是否安装某个客户端
    public static boolean AHAappInstalledOrNot(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static boolean isFirstRun(Context context) {
        boolean isFirstRun = false;
        String isFirstRunStr = context.getSharedPreferences("configure", Context.MODE_PRIVATE).getString("apkVersion", "");
        if (getAppVersion(context).equals(isFirstRunStr)) {
            isFirstRun = false;
        } else {
            isFirstRun = true;
        }
        context.getSharedPreferences("configure", Context.MODE_PRIVATE).edit().putString("apkVersion", getAppVersion(context)).commit();
        return isFirstRun;
    }

    /**
     * 获取当前context的进程
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }


    /**
     * 显示键盘
     * @param context
     */
    public static void showKeyBoard(Activity context){

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView()
                    .getWindowToken(), 0);
        }
    }
    /**
     * 操作系统
     *
     * @return
     */
    public static String getOs() {
        return "android";
    }


    public static String getChannel(Context context) {
        return ChannelUtil.getChannel(context);
    }
}
