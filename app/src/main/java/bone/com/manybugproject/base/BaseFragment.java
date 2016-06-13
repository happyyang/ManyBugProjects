package bone.com.manybugproject.base;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * base fragment
 *
 * @author 赵然
 *
 */
public abstract class BaseFragment extends Fragment implements ActivityInterface,RequestListener {

	private CustomTittleView tittle;

	private View mContentView;

	private ViewGroup mGroupContent = null;

	private Intent mIntent = new Intent();

	/**
	 * 是否缓存页面
	 */
	private boolean isCache = true ;
	protected boolean isVisible;
	/**
	 * 加载框
	 */
	ProgressDialog progressDialog;

	/**
	 * 异常view
	 */
	private CommonExceptionView exceptionView;
	private View contentView;

	private boolean isViaOnCreate;
	protected Activity context;


	/**
	 * 请求错误信息的处理
	 */
	private  Handler responseStatusExceptionHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//// TODO: 16/6/13   请求异常的统一处理 
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContentView = inflateLayout(R.layout.activity_base, null, false);
		hiddenTittle();
		context = getActivity();
		isViaOnCreate = true;
		initTitleBarView();
		initView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 设置禁止横屏
		if (isCache) {
			isViaOnCreate = false;
			getActivity().getSupportFragmentManager().saveFragmentInstanceState(
					this);
			ViewGroup p = (ViewGroup) mContentView.getParent();
			if (p != null)
				p.removeAllViewsInLayout();
		}else{
			if (isViaOnCreate)
			{
				isViaOnCreate = false;
			}else {
				mContentView = inflateLayout(R.layout.activity_base, null, false);

				initTitleBarView();
				initView();
			}
		}
		return mContentView;

	}
	/**
	 * 装载一个布局文件，获得其视图
	 *
	 * @param layoutId
	 *            布局文件
	 * @param root
	 * @param attachToRoot
	 * @return
	 */
	protected View inflateLayout(int layoutId, ViewGroup root,
								 boolean attachToRoot) {
		View view = LayoutInflater.from(getActivity()).inflate(layoutId, root,
				attachToRoot);
		return view;
	}
	protected void setContentView(int resid) {
		contentView = View.inflate(mContentView.getContext(), resid, mGroupContent);
		contentView.setClickable(true);
		ButterKnife.bind(this, contentView);
	}

	private void initView() {
		loadLayout();
		logic();
		loadListener();
		request();
	}
	/**
	 * 在这里实现Fragment数据的缓加载.
	 * @param isVisibleToUser
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
		}
	}

	protected void onVisible(){
		lazyLoad();
	}

	protected abstract void lazyLoad();
	protected void initTitleBarView() {
		mGroupContent = (ViewGroup) mContentView.findViewById(R.id.activity_content);
		tittle = (CustomTittleView) findViewById(R.id.title_bar);
		exceptionView = (CommonExceptionView) findViewById(R.id.cev_base_exception);
		tittle.setViewBackgroundResource(R.drawable.oneline_downbkg_white);
	}
	public View getmContentView(){
		return contentView;
	}

    /**
     * 获取异常view
     * @return
     */
	public CommonExceptionView getExceptionView(){
		return exceptionView;
	}
	/**
	 * 针对当前fragment中的view  批量设置监听器
	 * @param listener
	 * @param ids
	 * @author 赵然
	 * @date 2016年02月16
	 */
	protected void findListeners( OnClickListener listener, int... ids) {
		if (ids == null || listener == null)
			return;
		for (int id : ids) {
			findViewById(id).setOnClickListener(listener);
		}
	}
	/**
	 * 针对指定的view  批量设置内部控件的监听  比如 弹框中的布局是自定义的 部分按钮的点击效果
	 * @param listener
	 * @param ids
	 * @author 赵然
	 * @date 2016年02月16
	 */
	protected void findListeners4View( View view,OnClickListener listener, int... ids) {
		if (ids == null || listener == null)
			return;
		for (int id : ids) {
			view.findViewById(id).setOnClickListener(listener);
		}
	}

	/**
	 * 功能描述：无网点击回调
	 *
	 * @author 赵然
	 *            :
	 */
	public void noNetCallBack() {
		request();
	}

	protected View findViewById(int id) {
		return mContentView.findViewById(id);
	}

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
		mIntent.replaceExtras(args);
	}


	protected void runOnUiThread(Runnable runnable) {
		new Handler(Looper.getMainLooper()).post(runnable);
	}

	protected Intent getIntent() {
		return mIntent;
	}

	public void repalceFramgnet(int id, Fragment fragment) {
		FragmentTransaction mTransaction = getFragmentManager().beginTransaction();

		mTransaction.replace(id, fragment);
		mTransaction.commit();
	}

	protected void replaceFragment(Fragment fragment, String tag, int id) {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment oldFragment = fragmentManager.findFragmentByTag(tag);
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (oldFragment == null)
			oldFragment = fragment;
		fragmentTransaction.replace(id, oldFragment, tag);
		fragmentTransaction.commit();
	}

	/**
	 * Fragment 中嵌套fragment时    父fragment中替换view使用这个
	 * @param fragment  innerFragment
	 * @param tag     标记
	 * @param id   替换的布局
	 */
	protected void replaceInnerFragment(Fragment fragment, String tag, int id) {
		FragmentManager fragmentManager = getChildFragmentManager();
		Fragment oldFragment = fragmentManager.findFragmentByTag(tag);
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		// fragmentTransaction.setCustomAnimations(R.anim.anim_enter,R.anim.anim_exit);
		if (oldFragment == null)
			oldFragment = fragment;
		fragmentTransaction.replace(id, oldFragment, tag);
		// fragmentTransaction.addToBackStack(oldFragment.getClass().toString());
		fragmentTransaction.commit();
	}


	/**
	 * 设置 tittle 中间文字
	 *
	 * @param title
	 */
	public void setTitleText(String title) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setCenterText(title);
	}

	/**
	 * 设置tittle中间文字
	 *
	 * @param titleID
	 *            文字对应的ID
	 */
	public void setTitleText(int titleID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setCenterText(getResources().getString(titleID));
	}

	/**
	 * 设置左侧的文字
	 *
	 * @param content
	 */
	public void setLeftText(String content) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setLeftText(content);
	}

	/**
	 * 设置左侧的文字
	 *
	 * @param contentID
	 *            文字对应的ID
	 */

	public void setLeftText(int contentID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setLeftText(getResources().getString(contentID));
	}

	/**
	 * 左侧的图片
	 *
	 * @param drawableID
	 *            图片对应的ID
	 */
	public void setLeftDrawable(int drawableID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setTextViewDrawableResource(0,drawableID,0); // 设置左图标

	}

	/**
	 * 设置右侧的文本
	 *
	 * @param content
	 *            文本内容
	 */
	public void setRightText(String content) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setRightText(content);
	}

	/**
	 * 设置右侧文本
	 *
	 * @param contentID
	 *            文本对应的ID
	 */
	public void setRightText(int contentID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setRightText(getResources().getString(contentID));
	}

	/**
	 * 设置右侧文本的图片
	 *
	 * @param drawableID
	 *            图片对应的ID
	 */
	public void setRightTittleDrawable(int drawableID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setTextViewDrawableResource(2,drawableID,0); // 设置左图标
	}

	/**
	 * 设置右侧文本 及 对应图标
	 *
	 * @param title
	 *            文本内容
	 * @param drawableID
	 *            文本对应的ID
	 */

	public void setRightTextAndDrawable(String title, int drawableID) {
		tittle.setVisibility(View.VISIBLE);
		tittle.setRightText(title);
		tittle.setTextViewDrawableResource(2,drawableID,0); // 设置左图标

	}

	/**
	 * 获取tittle中左侧的部分的textview
	 *
	 * @return
	 */
	public TextView getLeftTittleView() {
		return tittle.getLeftView();
	}

	/**
	 * 获取右侧的view
	 *
	 * @return
	 */
	public TextView getRightTittleView() {
		return tittle.getRightView();
	}
	public CustomTittleView getTittleView(){
		tittle.setViewBackgroundColor(Color.BLACK);
		return tittle;
	}
	public TextView getCenterTittleView(){
		return tittle.getCenterView();
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
	 * 隐藏异常布局
	 */
	public void hiddenCommonException(){

		exceptionView.setVisibility(View.GONE);
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
		if (isAdded() && getActivity() != null)
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
	 * 显示加载框
	 * @param isCancleable
	 */
	public void showLoadingDialog(boolean isCancleable) {
		progressDialog = ProgressDialogUtils.getProgressDialog(getActivity(), isCancleable);
		progressDialog.show();
	}

	/**
	 * 隐藏加载框
	 */
	public void hiddenLoadingDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
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
	public void onHttpResponseCodeException(String url, HttpContext httpContext,int status) {
		if (801 == status){
			responseStatusExceptionHandler.sendEmptyMessage(0);
		}
	}

	/**
	 * 异常布局的类型
	 *
	 * @author zijing
	 *
	 */
	public enum ExceptionLayType {
		NO_DATA, NO_NET;
	}

	public void showCommonTittle() {
		tittle.showCommonTittle();
	}

	public void showCustomTittle() {
		tittle.showCustomTittle();
	}

	public void setCustomTittle(View view) {

		tittle.setCustomTittleView(view);
	}
	/**
	 * 隐藏tittle
	 */
	public void hiddenTittle() {
		if (tittle != null) {
			tittle.setVisibility(View.GONE);
		}
	}
	/**
	 * 设置fragment 是否缓存页面
	 * @param isCache  默认true
	 */
	public void setIsCache(boolean isCache){
		this.isCache = isCache;
	}
	public boolean getisCache(){
		return isCache;
	}

	/**
	 * 显示默认的普通tittle   左侧返回按钮 点击返回  中间展示文字
	 * @param centreTittleText
	 */
	protected  void showDefaultTittle(String centreTittleText){
//// TODO: 16/5/11
		setTitleText(centreTittleText);
		setLeftDrawable(R.drawable.icon_back_black);
		getLeftTittleView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
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
}
