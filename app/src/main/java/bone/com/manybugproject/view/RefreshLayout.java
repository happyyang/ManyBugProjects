package bone.com.manybugproject.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 功能： 只有向下拉才会触发下拉刷新  其他情况不处理---防止内部view 左右滑动冲突
 * ＊创建者：赵然 on 16/6/12 15:31
 * ＊
 */
public class RefreshLayout extends SwipeRefreshLayout {

    // 是否存在左右滑动事件
    private boolean mDragger;
    // 记录手指按下的位置
    private float mStartY, mStartX;
    // 出发事件的最短距离
    private int mTouchSlop;
    public RefreshLayout(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                mStartY = ev.getY();
                mStartX = ev.getX();
                //初始化左右滑动事件为false
                mDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //如果左右滑动事件为true  直接返回false 不拦截事件
                if (mDragger) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                //获取X,Y滑动距离的绝对值
                float distanceX = Math.abs(endX - mStartX);
                float distanceY = Math.abs(endY - mStartY);

                // 如果X轴位移大于Y轴距离，那么将事件交给其他控件
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    mDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //初始化左右滑动事件为false
                mDragger = false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
