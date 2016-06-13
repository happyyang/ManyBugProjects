package bone.com.manybugproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络链接的判断
 *  创建者:赵然
 */
public class NetUtil {
	/**
	 * 判断网络是否连接
	 * @param context
	 * @return
	 */
	public static boolean checkNetWork(Context context) {
		// ConnectivityManager//系统服务
		// ①判断WIFI联网情况

		if (context != null) {
			
			boolean netSataus = false;
			ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
			if (networkInfo != null) { // 注意，这个判断一定要的哦，要不然会出�?
				netSataus = networkInfo.isAvailable();
				
			}
			return netSataus;
		}
		return false;
	}


	/**
	 * 判断wifi是否处于连接状�?
	 */
	public static boolean isWifi(Context context) {
		if (context != null) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// NetworkInfo:支持WIFI和MOBILE
			NetworkInfo networkInfo = manager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (networkInfo != null) {
				return networkInfo.isConnected();
			}
		}
		return false;
	}

	/**
	 * 判断Mobile是否处于连接状�?
	 */
	public static boolean isMobile(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo:支持WIFI和MOBILE
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null) 
		{
			return networkInfo.isConnected();
		}

		return false;
	}


}
