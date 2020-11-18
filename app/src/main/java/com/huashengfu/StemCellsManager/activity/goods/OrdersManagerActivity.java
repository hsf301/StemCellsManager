package com.huashengfu.StemCellsManager.activity.goods;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.FragmentAdapter;
import com.huashengfu.StemCellsManager.event.goods.ShowRefundDotEvent;
import com.huashengfu.StemCellsManager.event.goods.ShowToBeDeliveredDotEvent;
import com.huashengfu.StemCellsManager.fragment.goods.CompletedFragment;
import com.huashengfu.StemCellsManager.fragment.goods.RefundFragment;
import com.huashengfu.StemCellsManager.fragment.goods.ToBeConfirmedFragment;
import com.huashengfu.StemCellsManager.fragment.goods.ToBeDeliveredFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//订单管理
public class OrdersManagerActivity extends BaseActivity {

    private Unbinder unbinder;

    @BindView(R.id.rl_to_be_delivered)
    RelativeLayout rlToBeDelivered;
    @BindView(R.id.rl_to_be_confirmed)
    RelativeLayout rlToBeConfirmed;
    @BindView(R.id.rl_completed)
    RelativeLayout rlCompleted;
    @BindView(R.id.rl_refund)
    RelativeLayout rlRefund;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    private FragmentAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<RelativeLayout> menus = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_orders_manager);
        unbinder = ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        menus.add(rlToBeDelivered);
        menus.add(rlToBeConfirmed);
        menus.add(rlCompleted);
        menus.add(rlRefund);

        fragments.add(new ToBeDeliveredFragment());
        fragments.add(new ToBeConfirmedFragment());
        fragments.add(new CompletedFragment());
        fragments.add(new RefundFragment());

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
            TextView tvName = relativeLayout.findViewById(R.id.tv_name);
            View line = relativeLayout.findViewById(R.id.line);

            if(position == i){
                tvName.setTypeface(Typeface.DEFAULT_BOLD);
                line.setVisibility(View.VISIBLE);
            }else{
                tvName.setTypeface(Typeface.DEFAULT);
                line.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_to_be_confirmed, R.id.rl_to_be_delivered, R.id.rl_completed, R.id.rl_refund})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.rl_to_be_delivered:{
                vpContainer.setCurrentItem(0);
                break;
            }
            case R.id.rl_to_be_confirmed:{
                vpContainer.setCurrentItem(1);
                break;
            }
            case R.id.rl_completed:{
                vpContainer.setCurrentItem(2);
                break;
            }
            case R.id.rl_refund:{
                vpContainer.setCurrentItem(3);
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showToBeDeliveredDot(ShowToBeDeliveredDotEvent event){
        View dot = rlToBeDelivered.findViewById(R.id.dot);
        dot.setVisibility(event.isShow() ? View.VISIBLE : View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showRefundDot(ShowRefundDotEvent event){
        View dot = rlRefund.findViewById(R.id.dot);
        dot.setVisibility(event.isShow() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
