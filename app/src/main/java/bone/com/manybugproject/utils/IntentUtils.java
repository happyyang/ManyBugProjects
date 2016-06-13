package bone.com.manybugproject.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * activity 之间跳转的工具类
 */
public class IntentUtils {
	/**
	* Activity 页面跳转
	 * 
	 * @param fromActivity
	 *            从哪个页面跳得
	 * @param toActivity
	 *            到哪个页面去得
	 */
	public static void startActivity(Activity fromActivity, Class toActivity) {

		Intent intent = new Intent(fromActivity, toActivity);
		startActivity(fromActivity, toActivity, intent);
	}

	/**
	 * Activity 页面跳转
	 * 
	 * @param fromActivity
	 *            从哪个页面跳得
	 * @param toActivity
	 *            到哪个页面去得
	 * @param bundle
	 *            携带的数据
	 */
	public static void startActivity(Activity fromActivity, Class toActivity, Bundle bundle) {
		Intent intent = new Intent(fromActivity, toActivity);

		if (bundle != null) {

			intent.putExtras(bundle);
		}
		startActivity(fromActivity, toActivity, intent);
	}
	/**
	 * Activity 页面跳转
	 * 
	 * @param fromActivity
	 *            从哪个页面跳得
	 * @param toActivity
	 *            到哪个页面去得
	 * @param flag
	 *            Activity 跳转标示
	 */
	public static void startActivity(Activity fromActivity, Class toActivity, int flag) {
		Intent intent = new Intent(fromActivity, toActivity);
		intent.setFlags(flag);
		startActivity(fromActivity, toActivity, intent);
	}
	/**
	 * Activity 页面跳转
	 * @param fromActivity
	 *  	从哪个页面跳得
	 * @param toActivity
	 * 			  到哪个页面去得
	 * @param bundle  页面需要传递的数据
	 * @param flag  跳转标示
	 */
	
	public static void startActivity(Activity fromActivity, Class toActivity, Bundle bundle, int flag) {
		Intent intent = new Intent(fromActivity, toActivity);

		intent.setFlags(flag);
		if (bundle != null) {

			intent.putExtras(bundle);
		}
		startActivity(fromActivity, toActivity, intent);
	}
	public  static void startActivity(Activity fromActivity, Class toActivity, Intent intent) {
		fromActivity.startActivity(intent);
	}


}
