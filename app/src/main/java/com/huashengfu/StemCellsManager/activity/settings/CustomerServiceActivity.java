package com.huashengfu.StemCellsManager.activity.settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.FragmentAdapter;
import com.huashengfu.StemCellsManager.fragment.settings.customer.service.ActivityFragment;
import com.huashengfu.StemCellsManager.fragment.settings.customer.service.GoodsFragment;
import com.huashengfu.StemCellsManager.fragment.settings.customer.service.ServiceFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//客服设置
public class CustomerServiceActivity extends BaseActivity {

    @BindView(R.id.rl_goods)
    RelativeLayout rlGoods;
    @BindView(R.id.rl_service)
    RelativeLayout rlService;
    @BindView(R.id.rl_activity)
    RelativeLayout rlActivity;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    private List<Fragment> fragments = new ArrayList<>();
    private FragmentAdapter adapter;
    private List<RelativeLayout> menus = new ArrayList<>();
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_customer_service);

        unbinder = ButterKnife.bind(this);

        menus.add(rlGoods);
        menus.add(rlService);
        menus.add(rlActivity);

        fragments.add(new GoodsFragment());
        fragments.add(new ServiceFragment());
        fragments.add(new ActivityFragment());

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
    }

    private void resetMenu(int position){
        for(int i=0; i<menus.size(); i++){
            RelativeLayout relativeLayout = menus.get(i);
            View line = relativeLayout.findViewById(R.id.line);
            ImageView ivIcon = relativeLayout.findViewById(R.id.iv_icon);
            TextView tvName = relativeLayout.findViewById(R.id.tv_name);

            if(i == position){
                line.setVisibility(View.VISIBLE);
                tvName.setTextAppearance(this, R.style.TextBlack);
                tvName.setTypeface(Typeface.DEFAULT_BOLD);
            }else{
                line.setVisibility(View.GONE);
                tvName.setTextAppearance(this, R.style.TextGray);
                tvName.setTypeface(Typeface.DEFAULT);
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_goods, R.id.rl_activity, R.id.rl_service})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.rl_goods:{
                vpContainer.setCurrentItem(0);
                break;
            }
            case R.id.rl_service:{
                vpContainer.setCurrentItem(1);
                break;
            }
            case R.id.rl_activity:{
                vpContainer.setCurrentItem(2);
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
