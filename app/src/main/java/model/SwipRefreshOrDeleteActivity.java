package model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bone.com.manybugproject.R;
import bone.com.manybugproject.base.BaseActivity;
import bone.com.manybugproject.base.BasePagingFrameAdapter;
import bone.com.manybugproject.entity.ProjectItemEntity;
import bone.com.manybugproject.entity.swiprefreshentity.SwipeMenu;
import bone.com.manybugproject.entity.swiprefreshentity.SwipeMenuItem;
import bone.com.manybugproject.utils.DensityUtil;
import bone.com.manybugproject.utils.ToastUtils;
import bone.com.manybugproject.utils.ViewHolder;
import bone.com.manybugproject.view.RefreshLayout;
import bone.com.manybugproject.view.swiplistview.IsItemCanSwipInterface;
import bone.com.manybugproject.view.swiplistview.OnMenuItemClickListener;
import bone.com.manybugproject.view.swiplistview.SwipeMenuCreator;
import bone.com.manybugproject.view.swiplistview.SwipeMenuListView;
import butterknife.Bind;

/**
 * 功能：  系统下拉刷新+左滑删除 上拉更多
 * ＊创建者：赵然 on 16/6/13 15:53
 * ＊
 */
public class SwipRefreshOrDeleteActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,IsItemCanSwipInterface{

    @Bind(R.id.rl_swipactivity_refreshlay)
    RefreshLayout refreshLayout;
    @Bind(R.id.smlv_swipactivity_listview)
    SwipeMenuListView swipeMenuListView;

    private SwipViewAdapter adapter;

    @Override
    public void loadLayout() {

        setContentView(R.layout.activity_swiprefreshordelete);
    }

    @Override
    public void logic() {
        showDefaultTittle("上下拉带左滑删除");

        adapter = new SwipViewAdapter(getContext(),getData());
        swipeMenuListView.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFb,
                        0x41, 0x48)));
                // set item width
                deleteItem.setWidth(DensityUtil.dip2px(getContext(), 90));
                // set a icon
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(DensityUtil.sp2px(getContext(),8));
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        });

        swipeMenuListView.setAdapter(adapter);
        //设置条目是否可以左滑删除
        swipeMenuListView.setItemCanSwipInterface(this);
    }

    @Override
    public void loadListener() {
        swipeMenuListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ToastUtils.showMessage(getContext(),adapter.getData().get(position).getItemName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adapter.getData().remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void request() {

    }

    @Override
    public void clickRequest() {

    }

    @Override
    public void onRefresh() {

    }

    /**
     * 获取测试数据
     * @return
     */
    private List<ProjectItemEntity> getData(){
        List<ProjectItemEntity> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProjectItemEntity entity = new ProjectItemEntity();
            entity.setItemName("条目  "+ i);

            datas.add(entity);
            
        }
        return datas;
    }

    @Override
    public boolean isItemCanSwip(int position) {

        //TODO 添加判断逻辑 确认能否弹出删除按钮
        return position % 2 == 0 ? true : false;
    }

    /**
     * 适配器
     */
    class SwipViewAdapter extends BasePagingFrameAdapter<ProjectItemEntity>{

        public SwipViewAdapter(Context context, List<ProjectItemEntity> list) {
            super(context, list);
        }


        @Override
        protected View onViewCreate(int position, LayoutInflater inflater, ViewGroup parent) {
            return inflater.inflate(R.layout.item_mainacitivity,parent,false);
        }

        @Override
        protected void onViewAttach(int position, final ProjectItemEntity item, View convertView) {
            super.onViewAttach(position, item, convertView);

            ((TextView) ViewHolder.get(convertView, R.id.tv_mainactivityitem_name)).setText(item.getItemName());


        }
    }
}
