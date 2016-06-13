package bone.com.manybugproject.interfaces;

import android.webkit.JavascriptInterface;

/**
 * 给js 调用的类
 * @author 赵然
 *
 */
public abstract class JavaScriptinterface {

	@JavascriptInterface
	public abstract void notifyOnAndroid(String str);
	@JavascriptInterface
	public abstract String getSQNum();

}
