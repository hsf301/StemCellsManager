package com.huashengfu.StemCellsManager.fragment.interaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.ActivityConsultationAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ActivityConsultation;
import com.huashengfu.StemCellsManager.entity.interaction.ServiceQA;
import com.huashengfu.StemCellsManager.fragment.BaseFragment;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//活动咨询
public class ActivityConsultationFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;

    private int page = 1;
    private int pageSize = 10;
    private int pageCount = 1;

    private ActivityConsultationAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interaction_activity_consultation, null);

        unbinder = ButterKnife.bind(this, view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new ActivityConsultationAdapter();
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ActivityConsultation>() {
            @Override
            public void onItemClick(View view, ActivityConsultation activityConsultation) {

            }
        });

        loadMoreWrapper = new LoadMoreWrapper(adapter);
        rvList.setAdapter(loadMoreWrapper);
        rvList.setLayoutManager(layoutManager);

        rvList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if(page <= pageCount){
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                    mySwipeRefreshLayout.postDelayed(()->{
                        doQuery();
                    }, 1000);
                }else{
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
            }
        });

        mySwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorSwipeRefreshLayout1),
                getResources().getColor(R.color.colorSwipeRefreshLayout2),
                getResources().getColor(R.color.colorSwipeRefreshLayout3)
        );
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                doQuery();
            }
        });

        mySwipeRefreshLayout.post(()->{
            mySwipeRefreshLayout.setRefreshing(true);
            doQuery();
        });

        return view;
    }

    private void doQuery(){
        List<ActivityConsultation> activityConsultations = new ArrayList<>();
        for(int i=0; i<5; i++){
            activityConsultations.add(new ActivityConsultation());
        }

        adapter.addAll(activityConsultations);
        adapter.notifyDataSetChanged();

        mySwipeRefreshLayout.setRefreshing(false);
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
