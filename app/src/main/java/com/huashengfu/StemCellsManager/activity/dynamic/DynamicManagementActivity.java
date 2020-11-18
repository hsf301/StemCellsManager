package com.huashengfu.StemCellsManager.activity.dynamic;

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
import com.huashengfu.StemCellsManager.adapter.FragmentAdapter;
import com.huashengfu.StemCellsManager.fragment.dynamic.AllDynamicFragment;
import com.huashengfu.StemCellsManager.fragment.dynamic.ArticleDynamicFragment;
import com.huashengfu.StemCellsManager.fragment.dynamic.VideoDynamicFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 动态管理
public class DynamicManagementActivity extends BaseActivity {

    @BindView(R.id.rl_all)
    RelativeLayout rlAll;
    @BindView(R.id.rl_file)
    RelativeLayout rlFile;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    private FragmentAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<RelativeLayout> menus = new ArrayList<>();
    private List<Pair<Integer, Integer>> menuResIds = new ArrayList<>();

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_management);

        unbinder = ButterKnife.bind(this);

        menus.add(rlAll);
        menus.add(rlFile);
        menus.add(rlVideo);

        menuResIds.add(new Pair<>(R.mipmap.icon_menu_all, R.mipmap.icon_menu_all_select));
        menuResIds.add(new Pair<>(R.mipmap.icon_menu_file, R.mipmap.icon_menu_file_select));
        menuResIds.add(new Pair<>(R.mipmap.icon_menu_video, R.mipmap.icon_menu_video_select));

        fragments.add(new AllDynamicFragment());
        fragments.add(new ArticleDynamicFragment());
        fragments.add(new VideoDynamicFragment());

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

    @OnClick({R.id.iv_back, R.id.btn_create, R.id.ll_all, R.id.ll_file, R.id.ll_video})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.btn_create:{
                Intent intent = new Intent(this, PublishDynamicActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_all:{
                vpContainer.setCurrentItem(0);
                break;
            }
            case R.id.ll_file:{
                vpContainer.setCurrentItem(1);
                break;
            }
            case R.id.ll_video:{
                vpContainer.setCurrentItem(2);
                break;
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
