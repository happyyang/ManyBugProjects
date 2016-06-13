package bone.com.manybugproject.utils;


import android.content.Context;

import bone.com.manybugproject.view.BaseShowToast;


public class ToastUtils {

	/**
	 * 创建位于屏幕 下方的 toast
	 * @param context
	 *            上下文
	 * @param text
	 *            要展示的文本
	 */
	public static void showMessage(Context context, String text) {
		BaseShowToast.showTips(context, text);
	}
	
	/**
	 * 创建位于屏幕中间的 toast
	 * @param context
	 * @param text
	 * @param time
	 */
	public static void showMessage(Context context, String text,int time) {
		BaseShowToast.showTips(context, text,time);
	}

	public static void showMessage(Context context, int text) {
		BaseShowToast.showTips(context, text);
	}
}
