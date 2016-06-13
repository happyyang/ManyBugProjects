package bone.com.manybugproject.view.swiplistview;


import bone.com.manybugproject.entity.swiprefreshentity.SwipeMenu;

/**
 * 左滑出现按钮的点击回调
 */
public interface OnMenuItemClickListener {
	boolean onMenuItemClick(int position, SwipeMenu menu, int index);
}
