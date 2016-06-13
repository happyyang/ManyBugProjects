package bone.com.manybugproject;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import bone.com.manybugproject.base.BaseActivity;
import bone.com.manybugproject.entity.ProjectItemEntity;
import bone.com.manybugproject.utils.IntentUtils;
import bone.com.manybugproject.view.customrecycleviewadapter.BaseQuickAdapter;
import butterknife.Bind;
import model.SwipRefreshOrDeleteActivity;

/**
 *
 */
public class MainActivity extends BaseActivity  {

    @Bind(R.id.rv_mainactivity_mainview)
    RecyclerView recyclerView;
    private MainActivityAdapter adapter;
    @Override
    public void loadLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void logic() {
        setCenterTitleText("首页");
        adapter = new MainActivityAdapter(getContext(),getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
          recyclerView.setHasFixedSize(true);
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                IntentUtils.startActivity(getContext(),adapter.getData().get(position).getClazz());
            }
        });

    }



    @Override
    public void loadListener() {


    }

    @Override
    public void request() {

    }

    @Override
    public void clickRequest() {

    }

    /**
     * 获取界面展示的填充数据
     * @return
     */
    private List<ProjectItemEntity> getData() {
        List<ProjectItemEntity> datas = new ArrayList<>();

        ProjectItemEntity swapRefreshdeleteViewEntity = new ProjectItemEntity();
        swapRefreshdeleteViewEntity.setItemName("系统的下拉刷新嵌套 左滑删除listview");
        swapRefreshdeleteViewEntity.setClazz(SwipRefreshOrDeleteActivity.class);
        datas.add(swapRefreshdeleteViewEntity);


        return datas;
    }
    
}
