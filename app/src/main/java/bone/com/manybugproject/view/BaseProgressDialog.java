package bone.com.manybugproject.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import bone.com.manybugproject.R;


/**
   * 类名：BaseProgressDialog
   * @author 赵然
   * 实现的主要功能:
   * 创建日期：2014-11-26
   * 修改者，修改日期，修改内容。
   */
public class BaseProgressDialog extends ProgressDialog{
	private static BaseProgressDialog dailog;
	private AnimationDrawable animationDrawable;
	private ImageView mLoadingImg;

	public BaseProgressDialog(Context context) {
		super(context, R.style.baseProgressDialog);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_baseprogress);
		mLoadingImg = (ImageView) findViewById(R.id.iv_loading);
		mLoadingImg.setImageResource(R.drawable.loading_progress);
		animationDrawable = (AnimationDrawable)mLoadingImg.getDrawable();
		animationDrawable.start();
	}

	@Override
	public void dismiss() {

        if (animationDrawable != null) {
            animationDrawable.stop();
        }
		dailog = null;
		super.dismiss();
	}

    @Override
    public void show() {
        super.show();
    }

	public static BaseProgressDialog getDailogInstance(Context context) {
        dailog = new BaseProgressDialog(context);
		dailog.setCancelable(false);
		return dailog;
	}

}
