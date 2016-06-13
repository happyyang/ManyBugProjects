package bone.com.manybugproject.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;


import java.util.List;

/**
 * 适配器，包含分页以及不分页
 *
 * @author wangshoubo
 * @date 2015年10月25日
 *
 */
public abstract class BaseFrameAdapter<T> extends BaseAdapter implements
		OnScrollListener {
	public Context mContext;

	public LayoutInflater inflater;

	protected List<T> mData;

	public boolean hasNextPage = false;

	public int page = 0;
	private FooterRefreshListener mRefreshListener;

	private ScrollingCallbackListener mScrollingCallbackListener;
    private boolean isLoadComplete = false;


    public BaseFrameAdapter() {
		resetPageNumber();
	}
	/**
	 * 在adapter的getView中，首次创建view，初始化并返回创建的布局
	 *
	 * @param position
	 *
	 * @param inflater
	 * @param parent
	 * @return
	 */
	protected abstract View onViewCreate(int position, LayoutInflater inflater, ViewGroup parent);

	/**
	 * 在apdater的getView中，每次滑动listView会循环执行本方法
	 *
	 * @param position
	 * @param item
	 * @param convertView
	 */
	protected abstract void onViewAttach(int position, T item, View convertView);

    /**
     * 构造方法
     * @param context
     * @param data
     */
	public BaseFrameAdapter(Context context, List<T> data) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.mData = data;
		resetPageNumber();
	}

	public void setData(List<T> data) {
		this.mData = data;
	}

	public List<T> getData() {
		return mData;
	}

	public void resetPageNumber() {
		page = 0;
	}

	public int getPage() {
		return page;
	}

	/**
	 * @discretion: 删除指定位置数据
	 * @author: MaoYaNan
	 * @date: 2014-10-21 下午6:38:09
	 * @param position
	 */
	public void removeItem(int position) {
		if (mData != null) {
			mData.remove(position);
			notifyDataSetChanged();
		}
	}

	/**
	 * @discretion: 删除所有数据
	 * @author: WangShouBo
	 * @date: 2015-6-24
	 *
	 */
	public void removeAll() {
		if (mData != null) {
			mData.clear();
			notifyDataSetChanged();
		}
	}

	/**
	 * @discretion: 删除所有数据不刷新
	 * @author: WangShouBo
	 * @date: 2015-6-24
	 *
	 */
	public void removeAllNoNotify() {
		if (mData != null) {
			mData.clear();
		}
	}

	/**
	 * 重新填充Adapter的数据
	 *
	 * @param list
	 * @author LiuYuHang
	 * @date 2014年10月14日
	 */
	public void resetData(List<T> list) {
		if (this.mData != null)
			mData.clear();
		setData(list);
		resetPageNumber();

		notifyDataSetChanged();
	}

	/**
	 * 加载下一页数据，之后Page + 1 (已做判空处理)
	 *
	 * @author LiuYuHang
	 * @date 2014年9月25日
	 *
	 * @param list
	 */
	public void loadNextPage(List<T> list) {
		if (list == null || list.size() == 0)
			return;
		page++;
		mData.addAll(list);
		notifyDataSetChanged();
	}

	/**
	 * 加载更多（page为未加1，前端做判断）
	 *
	 * @author wangshoubo
	 * @param list
	 */
	public void loadMore(List<T> list) {
		if (list == null || list.size() == 0)
			return;
		mData.addAll(list);
		notifyDataSetChanged();
	}


	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = onViewCreate(position, inflater, parent);
		}
		onViewAttach(position, getItem(position), convertView);
		return convertView;
	}
	@Override
	public int getCount() {
		int count = mData == null ? 0 : mData.size();
		// if (dataDelegate != null)
		// dataDelegate.onDataChanaged(getCount());//会造成死循环
		return count;
	}

	public T getItem(int position) {
		return mData == null ? null : mData.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public Context getContext() {
		return mContext;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		 switch(scrollState){
	       case SCROLL_STATE_FLING:
	      	 //用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动
	       break;

	       case SCROLL_STATE_IDLE:
	      	// 视图已经停止滑动
	    	   if (view.getLastVisiblePosition() == view.getCount() - 1 && mRefreshListener != null
                       && isLoadComplete) {
		 	            mRefreshListener.onFooterAutoRefresh();
                   isLoadComplete = false;
		        }
	       break;

	       case SCROLL_STATE_TOUCH_SCROLL:
	      	// 手指没有离开屏幕，视图正在滑动
	    	   if (mScrollingCallbackListener != null) {
	    		   mScrollingCallbackListener.onScrolling();
			  }
	       break;
	       }
	}

    /**
     * 防止滑到底部没有加载完成时又加载多次。
     */
    public void setLoadComplete() {
       isLoadComplete = true;
    }
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	public interface FooterRefreshListener {
		void onFooterAutoRefresh();
	}

	public void setFooterRefreshListener(FooterRefreshListener mRefreshListener) {
		this.mRefreshListener = mRefreshListener;
	}
	public interface ScrollingCallbackListener {
		void onScrolling();
	}

	public void setScrollingCallbackListener(ScrollingCallbackListener mScrollingCallbackListener) {
		this.mScrollingCallbackListener = mScrollingCallbackListener;
	}

}
