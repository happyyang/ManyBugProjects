package bone.com.manybugproject.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import bone.com.manybugproject.R;
import bone.com.manybugproject.interfaces.ActivityInterface;
import bone.com.manybugproject.net.HttpContext;
import bone.com.manybugproject.net.RequestListener;
import bone.com.manybugproject.utils.NetUtil;
import bone.com.manybugproject.utils.ProgressDialogUtils;
import bone.com.manybugproject.utils.ToastUtils;
import bone.com.manybugproject.view.CommonExceptionView;
import bone.com.manybugproject.view.CustomTittleView;
import butterknife.ButterKnife;

/**
 * Activity的基类
 *
 * @author 赵然
 *
 *
 */
public abstract class BaseActivity extends FragmentActivity implements ActivityInterface,RequestListener {

	private CustomTittleView tittle;

	private ViewGroup mGroupContent = null;// add activity's content
	protected Activity context;

	/**
	 * 异常view
	 */
	private CommonExceptionView exceptionView;
	/**
	 * 加载框
	 */
	private ProgressDialog mLoadingDialog;


	/**
	 * 请求错误信息的处理
	 */
	private Handler responseStatusExceptionHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//// TODO: 16/6/13   统一处理请求返回的错误码
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = this;
		setCustomerView(R.layout.activity_base);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 设置禁止横屏
		mGroupContent = (ViewGroup) findViewById(R.id.activity_content);

		init();

	}


	@Override
	public void setContentView(View view) {
		mGroupContent.addView(view);
		ButterKnife.bind(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		View view = LayoutInflater.from(this).inflate(layoutResID, mGroupContent, false);
		setContentView(view);
	}
	public View getmContentView(){
		return mGroupContent;
	}
	public void remove(View view) {
		mGroupContent.removeView(view);
	}

	protected void setCustomerView(int res) {
		super.setContentView(res);
		initTitleBarAndExceptionView();
	}
	private void initTitleBarAndExceptionView() {
		mGroupContent = (ViewGroup)findViewById(R.id.activity_content);
		tittle = (CustomTittleView) findViewById(R.id.title_bar);
		tittle.setViewBackgroundColor(getResources().getColor(R.color.color_tittle_bg));
		exceptionView = (CommonExceptionView)findViewById(R.id.cev_base_exception);
		exceptionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });
		tittle.setViewBackgroundResource(R.drawable.oneline_downbkg_white);

	}


	private void init() {
		loadLayout();
		logic();
		loadListener();
		request();
	}
	@Override
	protected void onStart() {
		super.onStart();
	}

	protected String getAppVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0);
			return packInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	protected void replaceFragment(Fragment fragment, String tag,int replaceLayoutId) {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		Fragment oldFragment = fragmentManager.findFragmentByTag(tag);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		// fragmentTransaction.setCustomAnimations(R.anim.anim_enter,R.anim.anim_exit);
		if (null == oldFragment)
			oldFragment = fragment;
		fragmentTransaction.replace(replaceLayoutId, oldFragment, tag);
		fragmentTransaction.addToBackStack(oldFragment.getClass().toString());
		fragmentTransaction.commit();
	}

	public void replaceNewFragment(Fragment fragment, String tag,int replaceLayoutId) {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		// fragmentTransaction.setCustomAnimations(R.anim.anim_enter,R.anim.anim_exit);
		fragmentTransaction.replace(replaceLayoutId, fragment, tag);
		fragmentTransaction.commit();
	}

	/**
	 * 设置 tittle 中间文字
	 * @param title
	 */
	public void setCenterTitleText(String title) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setCenterText(title);
	}


	/**
	 * 设置tittle中间文字
	 * @param titleID   文字对应的ID
	 */
	public void setCenterTitleText(int titleID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setCenterText(getResources().getString(titleID));
	}
	/**
	 * 设置左侧的文字
	 * @param content
	 */
	public void setLeftTittleText(String content) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setLeftText(content);
	}
	/**
	 * 设置左侧的文字
	 * @param contentID   文字对应的ID
	 */

	public void setLeftTittleText(int contentID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setLeftText(getResources().getString(contentID));
	}
	/**
	 * 左侧的图片
	 * @param drawableID 图片对应的ID
	 */
	public void setLeftDrawable(int drawableID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setTextViewDrawableResource(0,drawableID,0); // 设置左图标
	}


	/**
	 * 设置右侧的文本
	 * @param content   文本内容
	 */
	public void setRightTitleText(String content) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setRightText(content);
	}
	/**
	 * 设置右侧文本
	 * @param contentID 文本对应的ID
	 */
	public void setRightTitleText(int contentID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setRightText(getResources().getString(contentID));
	}
	/**
	 * 设置右侧文本的图片 默认图片在左侧
	 * @param drawableID 图片对应的ID
	 */
	public void setRightDrawable(int drawableID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setTextViewDrawableResource(2,drawableID,0); // 设置左图标
	}
	/**
	 * 设置右侧文本 及 对应图标(图标默认在左侧)
	 * @param title 文本内容
	 * @param drawableID  文本对应的ID
	 */

	public void setRightTextAndDrawable(String title, int drawableID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setRightText(title);
		tittle.setTextViewDrawableResource(2,drawableID,0); // 设置左图标

	}

	/**
	 * 获取tittle中间部分的textview
	 * @return
	 */
	public TextView getCenterTittleView(){
		return tittle.getCenterView();
	}
	/**
	 * 获取tittle中左侧的部分的textview
	 * @return
	 */
	public TextView getLeftTittleView(){
		return tittle.getLeftView();
	}
	/**
	 * 获取右侧的view
	 * @return
	 */
	public TextView getRightTittleView(){
		return tittle.getRightView();
	}

	/**
	 * 设置右标题字体颜色
	 * @return
	 */
	public void setRightTittleColor(int color){
		tittle.getRightView().setTextColor(color);
	}
    /**
     * 设置背景色
     * @param colorId
     */
    public void setBackgroundColor(int colorId){
        tittle.setViewBackgroundColor(colorId);
	}
    /**
     * 设置右标题字体颜色
     * @return
     */
    public void setCenterTitleColor(int color){
        tittle.getCenterView().setTextColor(color);
    }

	protected boolean isRoot() {
		return isTaskRoot() || (getParent() != null && getParent().isTaskRoot());
	}


	/**
	 * 自定义图片及提示异常页面
	 * @param icon
	 *            异常图片
	 * @param hint
	 *            文字提示
	 * @param  listener  点击布局的监听事件
	 */
	public void showCommonExceptionLay( int icon, String hint,OnClickListener listener) {
		exceptionView.setVisibility(View.VISIBLE);
		exceptionView.getExceptionIcon().setBackgroundResource(icon);
		exceptionView.getExceptionTextView().setText(hint);
		exceptionView.setOnClickListener(listener);
	}
	/**
	 * 自定义的 异常view
	 * @param view
	 */
	public void showCustomExceptionView(View view){
		exceptionView.setVisibility(View.VISIBLE);
		exceptionView.setCustomException(view);
	}
	/**
	 * 异常布局中得无网界面 一样故 统一封装  点击直接调用request方法
	 * @param isClickWithRequestMethed  点击无网布局是否调用 request防范  true: 调用  false 自己继续写点击事件
	 */
	public CommonExceptionView showCommonNoNet(boolean isClickWithRequestMethed){
		exceptionView.setVisibility(View.VISIBLE);
		exceptionView.getExceptionIcon().setBackgroundResource(R.drawable.icon_nonet);
		exceptionView.getExceptionTextView().setText(getString(R.string.string_netconnect_nonet));
		if (isClickWithRequestMethed) {
			exceptionView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					request();
				}
			});
		}
		return exceptionView;
	}
	/**
	 * 无数据布局
	 * @param tips  无数据布局的提示语
	 */
	public CommonExceptionView showCommonNoData (String tips){
		exceptionView.setVisibility(View.VISIBLE);
		exceptionView.getExceptionIcon().setVisibility(View.GONE);
		exceptionView.getExctptionButton().setVisibility(View.GONE);
		exceptionView.getExceptionTextView().setText(tips);
		return exceptionView;
	}

	/**
	 * 隐藏异常布局
	 */
	public void hiddenCommonException(){

		exceptionView.setVisibility(View.GONE);
	}

	/**
	 * 显示加载框
	 * @param isCancleable
	 */
	public void showLoadingDialog(boolean isCancleable) {
		mLoadingDialog = ProgressDialogUtils.getProgressDialog(this, isCancleable);
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
	public void showCommonTittle(){
		tittle.showCommonTittle();
	}
	public void showCustomTittle(){
		tittle.showCustomTittle();
	}

	public void setCustomTittle(View view){
        showTitlebar();
		tittle.setCustomTittleView(view);
	}
	/**
	 * 隐藏tittle
	 */
	public void hiddenTitlebar(){
		if (tittle != null) {
			tittle.setVisibility(View.GONE);
		}
	}

    /**
     * 显示标题栏
     */
	public void showTitlebar(){
		if (tittle != null) {
			tittle.setVisibility(View.VISIBLE);
		}
	}

	public View getTitlebar(){
			return tittle;
	}

	/**
	 *
	 * @return
	 */
	public CustomTittleView setTittl(){
		return tittle;
	}
	/**
	 * 方便获取上下文对象方法,免写类名.this
	 * @return
	 */
	public Activity getContext(){
		return context;
	}

	protected void findListeners(OnClickListener listener, int... ids) {
		if (ids == null || listener == null)
			return;
		for (int id : ids) {
			findViewById(id).setOnClickListener(listener);
		}
	}

	/**
	 * 设置左侧tittle文本的点击事件
	 * @param listener
	 */
	public void setLeftClickListener(OnClickListener listener){
		if (listener != null){
			tittle.getLeftView().setOnClickListener(listener);
		}
	}

	/**
	 * 设置左侧tittle文本的点击事件
	 * @param listener
	 */
	public void setRightClickListener(OnClickListener listener){
		if (listener != null){
			tittle.getRightView().setOnClickListener(listener);
		}
	}

	/**
	 * 隐藏键盘
	 */
	public void HiddenKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(getWindow().getDecorView()
                    .getWindowToken(), 0);
		}
	}
	/**
	 * 判断软键盘显隐
	 */
	public boolean ShowKeyBoard(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm != null) {
			return imm.showSoftInput(v,InputMethodManager.SHOW_FORCED);
		}
		return false;
	}

    /**
     * 显示软键盘
     * @param v
     */
	public void showKeyBoard(EditText v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(v, 0);
        }
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}

	/**
	 * 显示默认的普通tittle   左侧返回按钮 点击返回  中间展示文字
	 * @param centreTittleText
     */
	protected  void showDefaultTittle(String centreTittleText){
//// TODO: 16/5/11
		setCenterTitleText(centreTittleText);
		setLeftDrawable(R.drawable.icon_back_black);
		setLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onHttpRequestBegin(String url) {

	}

	@Override
	public void onHttpRequestSuccess(String url, HttpContext httpContext) {

	}

	@Override
	public void onHttpRequestFailed(String url, HttpContext httpContext, Throwable e) {

	}

	@Override
	public void onHttpRequestComplete(String url, HttpContext httpContext) {

	}

	@Override
	public void onHttpRequestCancel(String url, HttpContext httpContext) {

	}

	@Override
	public void onHttpResponseCodeException(String url, HttpContext httpContext ,int status) {
//// TODO: 16/5/29
		if (801 == status){
			responseStatusExceptionHandler.sendEmptyMessage(0);
		}
	}

	/**
	 * 检查网络  无网络则提示
	 * @return
     */
	public  boolean checkNet(){
		if (NetUtil.checkNetWork(getContext())){
			return true;
		}else{

			ToastUtils.showMessage(getContext(),R.string.string_netconnect_nonet);
			return false;
		}
	}


	/**
	 * 判断某个界面是否在前台
	 *
	 * @param context
	 * @param className
	 *            某个界面名称
	 */
	public boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

}
