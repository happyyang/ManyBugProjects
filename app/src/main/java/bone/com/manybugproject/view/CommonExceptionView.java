package bone.com.manybugproject.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bone.com.manybugproject.R;

/**
 * 异常view 分：无网络 图文按钮   其他异常提示：文字
 *
 * @author 赵然
 * 
 */
public class CommonExceptionView extends LinearLayout {
	/**
	 * 异常文本
	 */
	private TextView text;
	/**
	 * 异常图片
	 */
	private ImageView icon;
	/**
	 * 异常 按钮
	 */
	private TextView button;
	/**
	 * 自定义iview
	 */
	private FrameLayout customView;

	public CommonExceptionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context);
	}
	
	public CommonExceptionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CommonExceptionView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 界面初始化
	 */
	private void init(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.view_exception, this);
		icon = (ImageView) view.findViewById(R.id.iv_expectionicon_icon);
		text = (TextView) view.findViewById(R.id.tv_expectiontext_text);
		button = (TextView) view.findViewById(R.id.tv_expectiontext_button);
		customView = (FrameLayout) view.findViewById(R.id.fl_view_exception);
	}
	/**
	 * 获取异常文本
	 * @return
	 */
	public TextView getExceptionTextView(){
		return text;
	}
	/**
	 * 获取异常的图片
	 * @return
	 */
	public ImageView getExceptionIcon(){
		return icon;
	}
	/**
	 * 获取异常按钮
	 */
	public TextView getExctptionButton(){
		return button;
	}

	/**
	 * 设置自定义的异常view
	 * @param view
	 */
	public void setCustomException(View view){
		
		customView.addView(view);
	}

}
