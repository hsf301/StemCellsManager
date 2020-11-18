package com.huashengfu.StemCellsManager.activity.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.activity.service.PublishServiceActivity;
import com.huashengfu.StemCellsManager.adapter.FragmentAdapter;
import com.huashengfu.StemCellsManager.fragment.activity.ActivityFinishedFragment;
import com.huashengfu.StemCellsManager.fragment.activity.ActivityNotStartedFragment;
import com.huashengfu.StemCellsManager.fragment.activity.ActivityProgressFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 活动管理
public class ActivityManagementActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;
    @BindView(R.id.rl_not_started)
    RelativeLayout rlNotStarted;
    @BindView(R.id.rl_progress)
    RelativeLayout rlProgress;
    @BindView(R.id.rl_finished)
    RelativeLayout rlFinished;

    private FragmentAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<Pair<Integer, Integer>> menuResIds = new ArrayList<>();
    private List<RelativeLayout> menus = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_management);

        unbinder = ButterKnife.bind(this);

        menus.add(rlNotStarted);
        menus.add(rlProgress);
        menus.add(rlFinished);

        menuResIds.add(new Pair<>(R.mipmap.icon_activitystatus_0, R.mipmap.icon_activitystatus_0_select));
        menuResIds.add(new Pair<>(R.mipmap.icon_activitystatus_1, R.mipmap.icon_activitystatus_1_select));
        menuResIds.add(new Pair<>(R.mipmap.icon_activitystatus_2, R.mipmap.icon_activitystatus_2_select));

        fragments.add(new ActivityNotStartedFragment());
        fragments.add(new ActivityProgressFragment());
        fragments.add(new ActivityFinishedFragment());

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
                ivIcon.setImageResource(menuResIds.get(i).second);
                tvName.setTextAppearance(this, R.style.TextBlack14Sp);
                tvName.setTypeface(Typeface.DEFAULT_BOLD);
            }else{
                line.setVisibility(View.GONE);
                ivIcon.setImageResource(menuResIds.get(i).first);
                tvName.setTextAppearance(this, R.style.TextGray14Sp);
                tvName.setTypeface(Typeface.DEFAULT);
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_create, R.id.rl_not_started, R.id.rl_progress, R.id.rl_finished})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.rl_not_started:{
                vpContainer.setCurrentItem(0);
                break;
            }
            case R.id.rl_progress:{
                vpContainer.setCurrentItem(1);
                break;
            }
            case R.id.rl_finished:{
                vpContainer.setCurrentItem(2);
                break;
            }
            case R.id.btn_create:{
                Intent intent = new Intent(this, PublishActivityBannerActivity.class);
                startActivity(intent);
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
