package bone.com.manybugproject.utils;

import android.app.ProgressDialog;
import android.content.Context;

import bone.com.manybugproject.view.BaseProgressDialog;


/**
 * 功能：
 * ＊创建者：赵然 on 16/5/9 15:00
 * ＊
 */
public class ProgressDialogUtils {
    public static ProgressDialog getProgressDialog(Context context, Boolean canceLable) {
        BaseProgressDialog dialog = BaseProgressDialog.getDailogInstance(context);
        dialog.setCancelable(canceLable);
        return dialog;
    }

//	public static void CloseProgressDialog(BaseProgressDialog dialog) {
//		dialog.setDismiss(dialog);
//	}

    public static void CloseProgressDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();

        }
    }
}
