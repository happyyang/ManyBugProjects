package bone.com.manybugproject.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import bone.com.manybugproject.R;


/**
 * 公用的titttle  
 * 注意：整体背景色设置：方法太多这里提供了获取整体view的方法  自食其力吧
 * @author 赵然
 *
 */
public class CustomTittleView extends LinearLayout {
	/**
	 * 左侧 中间 右侧 的textviews
	 */
	private TextView mLeftTextView,mCenterTextView,mRightTextView;
	/**
	 * 整体view
	 */
	private View view;
	private Drawable leftDrawable  = null;
	private Drawable rightDrawable = null;
	private String leftTextStr,centerTextStr,rightTextStr;
	/**
	 * 自定义的tittle 对应的view
	 */
	private ViewGroup mCustomTittle;
	/**
	 * 标记当前是否是自定义的tittle
	 */
	private boolean isCustomTittle =  false;
	private  final int LEFT = 0;
	private  final int CENTER = 1;
	private  final int RIGHT = 2;

	public CustomTittleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = View.inflate(context, R.layout.base_title, this);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CustomTittleView, 0, 0);
		leftDrawable = a.getDrawable(R.styleable.CustomTittleView_leftDrawableLeft);
		rightDrawable = a.getDrawable(R.styleable.CustomTittleView_rightDrawableRight);

		leftTextStr = a.getString(R.styleable.CustomTittleView_leftTextText);

		centerTextStr = a.getString(R.styleable.CustomTittleView_centerTextText);

		rightTextStr = a.getString(R.styleable.CustomTittleView_rightTextText);

		a.recycle();

		init(view);
	}
	/**
	 * 获取整体view 在此可以设置背景
	 * @return
	 */
	public View getTittleView(){
		return view;
	}
	/**
	 * 页面初始化
	 * @param view
	 */
	private void init(View view){

		mLeftTextView = (TextView) view.findViewById(R.id.base_left);
		mCenterTextView = (TextView) view.findViewById(R.id.base_title);
		mRightTextView = (TextView) view.findViewById(R.id.base_right);
		mCustomTittle = (ViewGroup) view.findViewById(R.id.fl_basetittle_content);
		//设置左边图标
		if (leftDrawable != null) {
			setTextViewDrawable(LEFT,leftDrawable,0);

		}
		//设置右边图标
		if (rightDrawable != null) {
			setTextViewDrawable(RIGHT,rightDrawable,0);
		}
		//设置左边的文字
		if (!TextUtils.isEmpty(leftTextStr)) {
			setLeftText(leftTextStr);
		}
		//设置右边的文字
		if (!TextUtils.isEmpty(rightTextStr)) {
			setRightText(rightTextStr);
		}
		//设置中间文字
		if (!TextUtils.isEmpty(centerTextStr)) {
			setCenterText(centerTextStr);
		}

	}
	public void setCustomTittleView(View view){
        showCustomTittle();
		if (view !=  null) {
			if (view.getParent() != null) {
				((ViewGroup)view.getParent()).removeAllViews();
			}
		}
		mCustomTittle.addView(view);
	}
	/**
	 * 显示自定义tittle
	 */
	public void showCustomTittle(){
		mCustomTittle.setVisibility(View.VISIBLE);
	}
	/**
	 * 显示公用tittle
	 */
	public void showCommonTittle(){
		mCustomTittle.setVisibility(View.GONE);
	}
	/**
	 * 设置左侧tittle的文本
	 * @param leftText
	 */
	public void setLeftText(String leftText){
		setViewVisiable(LEFT);
		mLeftTextView.setText(leftText);

	}
	/**
	 * 设置 公共tittle 显示
	 */
	private void setViewVisiable(int type){

		if (!isCustomTittle) {
			switch (type) {
				case LEFT:
					mLeftTextView.setVisibility(View.VISIBLE);
					break;
				case CENTER:
					mCenterTextView.setVisibility(View.VISIBLE);
					break;
				case RIGHT:
					mRightTextView.setVisibility(View.VISIBLE);
					break;

				default:
					break;
			}
		}
	}
	/**
	 * 设置显示的权重
	 * @param left
	 * @param center
	 * @param right
	 */
	public void setWeight(int left,int center,int right){
		setWeightParams(mLeftTextView, left);
		setWeightParams(mCenterTextView, center);
		setWeightParams(mRightTextView, right);
	}
	/**
	 * 设置view对应的权重
	 * @param view
	 * @param weight
	 */
	private void setWeightParams(View view,int weight){
		LayoutParams params = (LayoutParams) view.getLayoutParams();
		params.weight = weight;
		view.setLayoutParams(params);

	}
	/**
	 * 设置中间文本
	 * @param centerText
	 */
	public void setCenterText(String centerText){
//		setViewVisiable(CENTER);
		this.setVisibility(View.VISIBLE);
		mCenterTextView.setVisibility(View.VISIBLE);
		mCenterTextView.setText(centerText);
	}

	/**
	 * 设置右侧文本
	 * @param rightText
	 */
	public void setRightText(String rightText){
		setViewVisiable(RIGHT);
		mRightTextView.setText(rightText);
	}



	/**
	 * 设置右侧文本的左侧的图片
	 * @param viewType 修改视图的位置  0:左边 1:中间  2:右边
	 * @param drawable 图片
	 * @param drawableLocation 0: left 1:right
	 *            图片位置
	 */
	public void setTextViewDrawable(int viewType,Drawable drawable,int drawableLocation){

		setViewVisiable(viewType);
		switch (viewType){
			case  LEFT:
				setTextViewDrawableWithLocation(mLeftTextView,drawable,drawableLocation);
				break;
			case  CENTER:
				setTextViewDrawableWithLocation(mCenterTextView,drawable,drawableLocation);
				break;
			case  RIGHT:
				setTextViewDrawableWithLocation(mRightTextView,drawable,drawableLocation);
				break;
			default:
				break;
		}

	}
	/**
	 * 设置右侧文本的左侧的图片
	 * @param viewType 修改视图的位置  0:左边 1:中间  2:右边
	 * @param drawableId 图片ID
	 * @param drawableLocation 0: left 1:right
	 *            图片位置
	 */
	public void setTextViewDrawableResource(int viewType,int drawableId,int drawableLocation){
		Drawable drawable = getViewDrawable(drawableId);
		setViewVisiable(viewType);
		switch (viewType){
			case  LEFT:
				setTextViewDrawableWithLocation(mLeftTextView,drawable,drawableLocation);
				break;
			case  CENTER:
				setTextViewDrawableWithLocation(mCenterTextView,drawable,drawableLocation);
				break;
			case  RIGHT:
				setTextViewDrawableWithLocation(mRightTextView,drawable,drawableLocation);
				break;
			default:
				break;
		}

	}

	/**
	 * 根据图片位置 设置图片
	 * @param view
	 * @param drawable
	 * @param drawableLocation
     */
	private void setTextViewDrawableWithLocation(TextView view,Drawable drawable,int drawableLocation){
		switch (drawableLocation){
			case  0:
				view.setCompoundDrawables(drawable, null, null, null); // 设置左图标
				break;
			case 1:
				view.setCompoundDrawables(null, null, drawable ,null); // 设置右图标
				break;
			default:
				break;

		}

	}

	/**
	 * 设置左侧文本的颜色
	 * @param colorId  eg: R.color.blue
	 */
	public void setLeftTextColorId(int colorId){
		setViewVisiable(LEFT);
		mLeftTextView.setTextColor(getResources().getColor(colorId));
	}

	/**
	 * 设置右侧文本的颜色
	 * @param colorString  eg:"#ffffff"
	 */
	public void setLeftTextColorString(String colorString){
		setViewVisiable(LEFT);
		mLeftTextView.setTextColor(Color.parseColor(colorString));
	}
	/**
	 * 设置中间文本的颜色
	 * @param colorId  eg: R.color.blue
	 */
	public void setCenterTextColorId(int colorId) {
		setViewVisiable(CENTER);
		mCenterTextView.setTextColor(getResources().getColor(colorId));
	}
	/**
	 * 设置中间文本的颜色
	 * @param colorString  eg:"#ffffff"
	 */
	public void setCenterTextColorString(String colorString){
		setViewVisiable(CENTER);
		mCenterTextView.setTextColor(Color.parseColor(colorString));
	}
	/**
	 * 设置右侧文本的颜色
	 * @param colorId  eg: R.color.blue
	 */
	public void setRightTextColorId(int colorId){
		setViewVisiable(RIGHT);
		mRightTextView.setTextColor(getResources().getColor(colorId));
	}
	/**
	 * 设右侧文本的颜色
	 * @param colorString  eg:"#ffffff"
	 */
	public void setRightTextColorString(String colorString){
		setViewVisiable(RIGHT);
		mRightTextView.setTextColor(Color.parseColor(colorString));
	}

	/**
	 * 设置TestView时需要先获取drawable 这里根据ID构建Drawable
	 *
	 * @param drawableID
	 * @return
	 */
	private Drawable getViewDrawable(int drawableID) {
		Drawable drawable;
		Resources res = getResources();
		drawable = res.getDrawable(drawableID);

//		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		return getAvailableDrawable(drawable);
	}

	/**
	 * 设置drawable大小
	 * @param drawable
	 * @return
	 */
	private Drawable getAvailableDrawable(Drawable drawable){
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		return drawable;
	}
	/**
	 * 设置左侧view的点击事件
	 * @param listener
	 */
	public void setLeftClickListener(OnClickListener listener){
		setViewVisiable(LEFT);
		mLeftTextView.setOnClickListener(listener);
	}
	/**
	 * 设置右侧view的点击事件
	 * @param listener
	 */
	public void setRightClickListener(OnClickListener listener){
		setViewVisiable(RIGHT);
		mRightTextView.setOnClickListener( listener);
	}
	/**
	 * 获取左侧的view
	 * @return
	 */
	public TextView getLeftView() {
		return mLeftTextView;
	}
	/**
	 * 获取右侧的view
	 * @return
	 */
	public TextView getRightView(){
		return mRightTextView;
	}
	public TextView getCenterView(){
		return mCenterTextView;
	}

	public void setViewBackgroundColor(int colorID){
		findViewById(R.id.rl_title_background).setBackgroundColor(colorID);

	}
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setViewBackground(Drawable backgroundDrable){
		findViewById(R.id.rl_title_background).setBackground(backgroundDrable);;
	}
	public void setViewBackgroundResource(int resid){
		findViewById(R.id.rl_title_background).setBackgroundResource(resid);
	}

}
