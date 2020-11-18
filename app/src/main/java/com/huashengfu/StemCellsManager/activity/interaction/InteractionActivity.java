package com.huashengfu.StemCellsManager.activity.interaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.FragmentAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.InteractionTypeAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.InteractionType;
import com.huashengfu.StemCellsManager.fragment.interaction.ActivityRegistrationFragment;
import com.huashengfu.StemCellsManager.fragment.interaction.CommodityConsultationFragment;
import com.huashengfu.StemCellsManager.fragment.interaction.DynamicCommentFragment;
import com.huashengfu.StemCellsManager.fragment.interaction.ServiceConsultationFragment;
import com.huashengfu.StemCellsManager.fragment.interaction.ServiceQAFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//我的互动
public class InteractionActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    private InteractionTypeAdapter interactionTypeAdapter;
    private FragmentAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction);

        unbinder = ButterKnife.bind(this);
        initInteractionTypeAdapter();

        fragments.add(new ServiceConsultationFragment());
        fragments.add(new ServiceQAFragment());
//        fragments.add(new CommodityConsultationFragment());
        fragments.add(new DynamicCommentFragment());
        fragments.add(new ActivityRegistrationFragment());
//        fragments.add(new ActivityConsultationFragment());

        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vpContainer.setAdapter(adapter);
        vpContainer.setCurrentItem(0);
        vpContainer.setOffscreenPageLimit(fragments.size());
        vpContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetMenu(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int select = getIntent().getIntExtra(Constants.Tag.data, Constants.Type.Interaction.fwzx);
        switch (select){
            case Constants.Type.Interaction.fwzx:{
                break;
            }
            case Constants.Type.Interaction.fwwd:{
                break;
            }
//            case Constants.Type.Interaction.spzx:{
//                break;
//            }
            case Constants.Type.Interaction.dtpl:{
                break;
            }
            case Constants.Type.Interaction.hdbm:{
                break;
            }
//            case Constants.Type.Interaction.hdzx:{
//                break;
//            }
        }

        vpContainer.setCurrentItem(select - 1);
    }

    private void resetMenu(int position){
        interactionTypeAdapter.setSelect(position);
        interactionTypeAdapter.notifyDataSetChanged();
        rvList.scrollToPosition(position);
    }

    private void initInteractionTypeAdapter(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvList.setLayoutManager(layoutManager);

        interactionTypeAdapter = new InteractionTypeAdapter();
        interactionTypeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<InteractionType>() {
            @Override
            public void onItemClick(View view, InteractionType type) {
                vpContainer.setCurrentItem(interactionTypeAdapter.getItemPosition(type));
            }
        });
        rvList.setAdapter(interactionTypeAdapter);

        String[] menus = getResources().getStringArray(R.array.interaction_type);
        int[] ids = new int[]{
                Constants.Type.Interaction.fwzx,
                Constants.Type.Interaction.fwwd,
//                Constants.Type.Interaction.spzx,
                Constants.Type.Interaction.dtpl,
                Constants.Type.Interaction.hdbm
//                Constants.Type.Interaction.hdzx
        };
        int[] resIds = new int[]{
                R.mipmap.icon_fwzx,
                R.mipmap.icon_fwwd,
//                R.mipmap.icon_spzx,
                R.mipmap.icon_dtpl,
                R.mipmap.icon_hdbm
//                R.mipmap.icon_hdzx
        };
        int[] resSelectIds = new int[]{
                R.mipmap.icon_fwzx_select,
                R.mipmap.icon_fwwd_select,
//                R.mipmap.icon_spzx_select,
                R.mipmap.icon_dtpl_select,
                R.mipmap.icon_hdbm_select
//                R.mipmap.icon_hdzx_select
        };

        List<InteractionType> types = new ArrayList<>();
        for(int i=0; i<menus.length; i++){
            InteractionType type = new InteractionType();
            type.setName(menus[i]);
            type.setId(ids[i]);
            type.setResId(resIds[i]);
            type.setResSelectId(resSelectIds[i]);
            types.add(type);
        }

        interactionTypeAdapter.addAll(types);
        interactionTypeAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.iv_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
