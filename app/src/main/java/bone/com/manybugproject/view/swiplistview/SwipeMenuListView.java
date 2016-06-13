package bone.com.manybugproject.view.swiplistview;


import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

import bone.com.manybugproject.entity.swiprefreshentity.SwipeMenu;


/**
 * 
 * @author 赵然
 * @date 2014-8-18
 * 
 */
public class SwipeMenuListView extends ListView {

	private static final int TOUCH_STATE_NONE = 0;
	private static final int TOUCH_STATE_X = 1;
	private static final int TOUCH_STATE_Y = 2;

	private int MAX_Y = 5;
	private int MAX_X = 3;
	private float mDownX;
	private float mDownY;
	private int mTouchState;
	private int mTouchPosition;
	private SwipeMenuLayout mTouchView;
	private OnSwipeListener mOnSwipeListener;

	private SwipeMenuCreator mMenuCreator;
	private OnMenuItemClickListener mOnMenuItemClickListener;
	private Interpolator mCloseInterpolator;
	private Interpolator mOpenInterpolator;
	
	private ItemTouchListener mItemTouchListener;
	/**
	 * 判断当前 条目是否可滑出的回调
	 */
	private IsItemCanSwipInterface itemSwipListener;

	public SwipeMenuListView(Context context) {
		super(context);
		init();
	}

	public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SwipeMenuListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		MAX_X = dp2px(MAX_X);
		MAX_Y = dp2px(MAX_Y);
		mTouchState = TOUCH_STATE_NONE;
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
			@Override
			public void createMenu(SwipeMenu menu) {
				if (mMenuCreator != null) {
					mMenuCreator.create(menu);
				}
			}

			@Override
			public void onItemClick(SwipeMenuView view, SwipeMenu menu,int index) {
				boolean flag = false;
				if (mOnMenuItemClickListener != null) {
					flag = mOnMenuItemClickListener.onMenuItemClick(
							view.getPosition(), menu, index);
				}
				if (mTouchView != null && !flag) {
					mTouchView.smoothCloseMenu();
				}
			}
		});
	}

	public void setCloseInterpolator(Interpolator interpolator) {
		mCloseInterpolator = interpolator;
	}

	public void setOpenInterpolator(Interpolator interpolator) {
		mOpenInterpolator = interpolator;
	}

	public Interpolator getOpenInterpolator() {
		return mOpenInterpolator;
	}

	public Interpolator getCloseInterpolator() {
		return mCloseInterpolator;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
			return super.onTouchEvent(ev);
		int action = MotionEventCompat.getActionMasked(ev);
		action = ev.getAction();
		//初始化监听
		if (mItemTouchListener != null) {
			mItemTouchListener.onItemTouchListener(mTouchPosition);
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			int oldPos = mTouchPosition;
			mDownX = ev.getX();
			mDownY = ev.getY();
			mTouchState = TOUCH_STATE_NONE;

			mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

			if (mTouchPosition == oldPos && mTouchView != null&& mTouchView.isOpen()) {
				mTouchState = TOUCH_STATE_X;
				mTouchView.onSwipe(ev);
				return true;
			}

			View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

			if (mTouchView != null && mTouchView.isOpen()) {
				mTouchView.smoothCloseMenu();
				mTouchView = null;
				// return super.onTouchEvent(ev);
				// try to cancel the touch event
				MotionEvent cancelEvent = MotionEvent.obtain(ev);  
				cancelEvent.setAction(MotionEvent.ACTION_CANCEL);    
				onTouchEvent(cancelEvent);
				return true;
			}
			if (view instanceof SwipeMenuLayout) {
				mTouchView = (SwipeMenuLayout) view;
			}
			if (mTouchView != null) {
				mTouchView.onSwipe(ev);
			}
//			Log.d("data", "--------------down---------------"+mTouchPosition);
			break;
		case MotionEvent.ACTION_MOVE:
			float dy = Math.abs((ev.getY() - mDownY));
			float dx = Math.abs((ev.getX() - mDownX));
			if (mTouchState == TOUCH_STATE_X) {
				if (mTouchView != null) {
					//判断当前点击item是否可以滑动
//					if(mTouchPosition != noSwipPosition){
//						mTouchView.onSwipe(ev);
//					}
					if (itemSwipListener != null) {
						if (itemSwipListener.isItemCanSwip(mTouchPosition)) {
							mTouchView.onSwipe(ev);
						}
					}else{
						mTouchView.onSwipe(ev);
					}
					
				}
				getSelector().setState(new int[] { 0 });
				ev.setAction(MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(ev);
				return true;
			} else if (mTouchState == TOUCH_STATE_NONE) {
				if (Math.abs(dy) > MAX_Y) {
					mTouchState = TOUCH_STATE_Y;
				} else if (dx > MAX_X) {
					mTouchState = TOUCH_STATE_X;
					if (mOnSwipeListener != null) {
						mOnSwipeListener.onSwipeStart(mTouchPosition);
					}
				}
			}
//			Log.d("data", "--------------move---------------"+mTouchPosition);
			break;
		case MotionEvent.ACTION_UP:

			if (mTouchState == TOUCH_STATE_X) {
				if (mTouchView != null) {
					//判断当前点击item是否可以滑动
//					if(mTouchPosition != noSwipPosition){
//						mTouchView.onSwipe(ev);
//					}
					if (itemSwipListener != null) {
						if (itemSwipListener.isItemCanSwip(mTouchPosition)) {
							mTouchView.onSwipe(ev);
						}
					}else{
						mTouchView.onSwipe(ev);
					}
					
					if (!mTouchView.isOpen()) {
						mTouchPosition = -1;
						mTouchView = null;
					}
				}
				if (mOnSwipeListener != null) {
					mOnSwipeListener.onSwipeEnd(mTouchPosition);
				}
				ev.setAction(MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(ev);
				return true;
			}
//			Log.d("data", "--------------up---------------"+mTouchPosition);
			break;
		}
		return super.onTouchEvent(ev);
	}

	public void smoothOpenMenu(int position) {
		if (position >= getFirstVisiblePosition()
				&& position <= getLastVisiblePosition()) {
			View view = getChildAt(position - getFirstVisiblePosition());
			if (view instanceof SwipeMenuLayout) {
				mTouchPosition = position;
				if (mTouchView != null && mTouchView.isOpen()) {
					mTouchView.smoothCloseMenu();
				}
				mTouchView = (SwipeMenuLayout) view;
				mTouchView.smoothOpenMenu();
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	public void setMenuCreator(SwipeMenuCreator menuCreator) {
		this.mMenuCreator = menuCreator;
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.mOnMenuItemClickListener = onMenuItemClickListener;
	}

	public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
		this.mOnSwipeListener = onSwipeListener;
	}


	public static interface OnSwipeListener {
		void onSwipeStart(int position);

		void onSwipeEnd(int position);
		
	}
	
	public void setOnItemTouchListener(ItemTouchListener mOnItemTouchListener) {
		this.mItemTouchListener = mOnItemTouchListener;
	}
	
	public interface ItemTouchListener{
		void onItemTouchListener(int position);
	}
	/**
	 * 设置 条目滑动与否的回调
	 * @param itemSwipListener
	 */
	public void setItemCanSwipInterface(IsItemCanSwipInterface itemSwipListener){
		this.itemSwipListener = itemSwipListener;
	}
}
