package bone.com.manybugproject;

import android.content.Context;

import java.util.List;

import bone.com.manybugproject.entity.ProjectItemEntity;
import bone.com.manybugproject.view.customrecycleviewadapter.BaseQuickAdapter;
import bone.com.manybugproject.view.customrecycleviewadapter.BaseViewHolder;

/**
 * 功能：首页recycleView的适配器
 * ＊创建者：赵然 on 16/6/13 15:42
 * ＊
 */
public class MainActivityAdapter extends BaseQuickAdapter<ProjectItemEntity> {
    public MainActivityAdapter(Context context, List<ProjectItemEntity> data) {
        super(context,R.layout.item_mainacitivity, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectItemEntity item) {

        helper.setText(R.id.tv_mainactivityitem_name,item.getItemName());
    }
}
