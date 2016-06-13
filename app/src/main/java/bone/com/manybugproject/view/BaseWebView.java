package bone.com.manybugproject.view;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import bone.com.manybugproject.utils.NetUtil;
import bone.com.manybugproject.utils.ProgressDialogUtils;
import bone.com.manybugproject.utils.ToastUtils;


public class BaseWebView extends WebView {

	private boolean mIsErrorPage;
	private CommonExceptionView mErrorView;
	private Context context;
	private boolean showLoading;
	private boolean firstShow = true;
	private goBackListener backLisener;
	private boolean isBackEnable = false;

	/**
	 * 加载框
	 */
	private ProgressDialog mLoadingDialog;

	public BaseWebView(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);
		this.context = ctx;

	}

	public BaseWebView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		this.context = ctx;
	}

	public BaseWebView(Context ctx) {
		super(ctx);
		this.context = ctx;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isBackEnable) {

			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (keyCode == KeyEvent.KEYCODE_BACK && this.canGoBack()) {
					goBack(); // 后退
					if (backLisener != null) {
						backLisener.goBackClick();
					}
					return true; // 已处理
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 显示自定义错误提示页面，用一个View覆盖在WebView
	 */
	@SuppressWarnings("deprecation")
	protected void showErrorPage() {
		initErrorPage();
		LinearLayout webParentView = (LinearLayout) getParent();

			while (webParentView.getChildCount() > 1 ) {
				webParentView.removeViewAt(0);
			}
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			webParentView.addView(mErrorView, 0, lp);
			mIsErrorPage = true;
	}

	protected void hideErrorPage() {
		LinearLayout webParentView = (LinearLayout) getParent();
		mIsErrorPage = false;
		while (webParentView.getChildCount() > 1) {
			webParentView.removeViewAt(0);
		}
	}

	/**
	 * 当刷新的时候，如果是有哭脸，先隐藏哭脸
	 */
	@Override
	public void reload() {
		super.reload();
		if(mIsErrorPage)
		{
			hideErrorPage();
		}
	}

	protected void initErrorPage() {
		if (mErrorView == null) {
			mErrorView = new CommonExceptionView(context);
			mErrorView.setVisibility(View.VISIBLE);
			mErrorView.getExctptionButton().setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					reload();
				}
			});
			mErrorView.setOnClickListener(null);
		}
	}

	public class BaseWebViewClient extends WebViewClient {
		/**
		 * 当出现错误页面，用自定义的哭脸页面覆盖上去
		 */
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			showErrorPage();
			view.loadUrl("javascript:document.body.innerHTML=\"" + "" + "\""); //可以将谷歌的默认出错给替换掉，不是百分之百有效
			ToastUtils.showMessage(context,"网络连接超时,请检查网络");
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

	}

	public class BaseWebChromeClient extends WebChromeClient {
		@SuppressLint("NewApi")
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if (isShowLoading()) {   //可以显示加载状态框
				if (newProgress == 100)
				{

					hiddenLoadingDialog();
				} else
				{

					if ( firstShow)
					{
						firstShow = false;
//						dialog = AppUtils.getProgressDialog("正在加载，请稍候...",context,true);
						if (NetUtil.checkNetWork(context))
						{
							showLoadingDialog(true);
						}
					}
				}
			}
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			ToastUtils.showMessage(context, message);
			result.confirm();
			return true;
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			ToastUtils.showMessage(context, message);
			result.confirm();
			return true;
		}

		@Override
		public void onCloseWindow(WebView window) {
			super.onCloseWindow(window);
		}
	}
	/**
	 * 显示加载框
	 * @param isCancleable
	 */
	public void showLoadingDialog(boolean isCancleable) {
		if (mLoadingDialog == null){

			mLoadingDialog = ProgressDialogUtils.getProgressDialog(getContext(), isCancleable);
		}
		mLoadingDialog.show();
	}

	/**
	 * 隐藏加载框
	 */
	public void hiddenLoadingDialog() {
		if (mLoadingDialog != null) {
				mLoadingDialog.dismiss();
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {

	    if (event.getAction() == MotionEvent.ACTION_DOWN){
	        int temp_ScrollY = getScrollY();
	        scrollTo(getScrollX(), getScrollY() + 1);
	        scrollTo(getScrollX(), temp_ScrollY);
	    }

	    return super.onTouchEvent(event);
	}

	public boolean isShowLoading() {
		return showLoading;
	}

	public void setShowLoading(boolean showLoading) {
		this.showLoading = showLoading;
	}
	/**
	 * 设置实体返回键是否可用
	 * @param isBackEnable
	 */
	public void setIsBackEnable(boolean isBackEnable){
		this.isBackEnable= isBackEnable;
	}
	/**
	 * 回退回调
	 */
	public static abstract interface goBackListener {
		//回退时回调
		public abstract void goBackClick();

	}
	public void setGoBackListener(goBackListener backLisener){
		this.backLisener = backLisener;
	}
	public boolean getIsErrorPage(){
		return mIsErrorPage;
	}
}
