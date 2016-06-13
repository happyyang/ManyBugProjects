package bone.com.manybugproject.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bone.com.manybugproject.R;

/**
 *
   * 类名：BaseShowToast
   * @author 赵然<br/>
   * 实现的主要功能:
   * 创建日期：2014-9-28
   * 修改者，修改日期，修改内容。
 */
public class BaseShowToast extends Toast {

    public BaseShowToast(Context context) {
        super(context);
    }

    /**
     * 创建位于屏幕下方的 toast
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static BaseShowToast makeTextAtBottom(Context context, CharSequence text, int duration) {
        BaseShowToast result = new BaseShowToast(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.view_mytoast, null);
        TextView tv = (TextView) v.findViewById(R.id.tips_msg);
        tv.setText(text);
        result.setView(v);
        result.setGravity(Gravity.BOTTOM, 0, 120);
        result.setDuration(duration);

        return result;
    }
    
    /**
     * 创建位于屏幕中间的 toast
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static BaseShowToast makeTextAtCenter(Context context, CharSequence text, int duration) {
    	BaseShowToast result = new BaseShowToast(context);
    	
    	LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View v = inflate.inflate(R.layout.view_mytoast, null);
    	TextView tv = (TextView) v.findViewById(R.id.tips_msg);
    	tv.setText(text);
    	result.setView(v);
    	result.setGravity(Gravity.CENTER, 0, 0);
    	result.setDuration(duration);
    	
    	return result;
    }
    
    public static BaseShowToast makeText(Context context, int resId, int duration) throws Resources.NotFoundException {
        return makeTextAtBottom(context, context.getResources().getText(resId), duration);
    }

    public void setIcon(int iconResId) {
        if (getView() == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        ImageView iv = (ImageView) getView().findViewById(R.id.tips_icon);
        if (iv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        iv.setImageResource(iconResId);
    }
    @Override
    public void setText(CharSequence s) {
        if (getView() == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) getView().findViewById(R.id.tips_msg);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }
    
    private static BaseShowToast tipsToast;
    
    public  static void showTips(Context context,int iconResId, String msg) {
		if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = BaseShowToast.makeTextAtBottom(context, msg,BaseShowToast.LENGTH_SHORT);
		}
		tipsToast.show();
		tipsToast.setIcon(iconResId);
		tipsToast.setText(msg);
	}
    
    public static void showTips(Context context, String msg) {
    	if (tipsToast != null) {
    		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
    			tipsToast.cancel();
    		}
    	} else {
//    		tipsToast = BaseShowToast.makeTextAtBottom(context, msg,BaseShowToast.LENGTH_SHORT);
    		tipsToast = BaseShowToast.makeTextAtCenter(context, msg,BaseShowToast.LENGTH_SHORT);
    	}
    	tipsToast.show();
    	tipsToast.setText(msg);
    }
    
    public static void showTips(Context context, String msg,int time) {
    	if (tipsToast != null) {
    		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
    			tipsToast.cancel();
    		}
    	} else {
    		tipsToast = BaseShowToast.makeTextAtCenter(context, msg,time);
    	}
    	tipsToast.show();
    	tipsToast.setText(msg);
    }
    
    public static void showTips(Context context, int msg) {
    	if (tipsToast != null) {
    		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
    			tipsToast.cancel();
    		}
    	} else {
    		tipsToast = BaseShowToast.makeText(context, msg,BaseShowToast.LENGTH_SHORT);
    	}
    	tipsToast.show();
    	tipsToast.setText(msg);
    }
}
