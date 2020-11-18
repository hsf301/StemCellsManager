package com.huashengfu.StemCellsManager.activity.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.type.ChildrenTypeAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.type.ParentTypeAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.type.SelectTypeAdapter;
import com.huashengfu.StemCellsManager.entity.response.goods.Type;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GoodsTypeListActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.rv_select)
    RecyclerView rvSelect;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;

    private Unbinder unbinder;

    private SelectTypeAdapter selectTypeAdapter;
    private ParentTypeAdapter parentTypeAdapter;
    private ChildrenTypeAdapter searchTypeAdapter;

    private ArrayList<Type> types;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_type);
        unbinder = ButterKnife.bind(this);

        initSelect();
        initParent();

        searchTypeAdapter = new ChildrenTypeAdapter();
        searchTypeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Type>() {
            @Override
            public void onItemClick(View view, Type type) {
                goBack(type);
            }
        });

        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0){
                    rvList.setAdapter(parentTypeAdapter);
                    parentTypeAdapter.clear();
                    parentTypeAdapter.addAll(types);
                    parentTypeAdapter.notifyDataSetChanged();

                    rvSelect.setVisibility(View.GONE);
                    ivDelete.setVisibility(View.GONE);
                }else{
                    ivDelete.setVisibility(View.VISIBLE);
                    searchType.clear();
                    searchType(types, editable.toString());

                    if(!searchType.isEmpty()){
                        rvList.setAdapter(searchTypeAdapter);
                        searchTypeAdapter.setTypes(searchType);
                        searchTypeAdapter.notifyDataSetChanged();

                        selectTypeAdapter.clear();
                        selectTypeAdapter.notifyDataSetChanged();
                        rvSelect.setVisibility(View.GONE);
                    }
                }
            }
        });

        types = (ArrayList<Type>) getIntent().getSerializableExtra(Constants.Tag.data);
        parentTypeAdapter.addAll(types);
        parentTypeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Type>() {
            @Override
            public void onItemClick(View view, Type type) {
                if(type.getChildren().isEmpty()){
                    goBack(type);
                    return;
                }

                resetSelectType(type);

                parentTypeAdapter.clear();
                parentTypeAdapter.addAll(type.getChildren());
                parentTypeAdapter.notifyDataSetChanged();
            }
        });
        parentTypeAdapter.notifyDataSetChanged();
    }

    private void goBack(Type type){
        Intent data = new Intent();
        data.putExtra(Constants.Tag.data, type);
        setResult(RESULT_OK, data);
        finish();
    }

    private List<Type> searchType = new ArrayList<>();
    private void searchType(List<Type> types, String key){
        for(Type type : types){
            if(type.getChildren().isEmpty()){
                if(type.getName().contains(key)) {
                    searchType.add(type);
                }
            }else{
                searchType(type.getChildren(), key);
            }
        }
    }

    private void resetSelectType(Type type){
        tree.clear();
        findTree(types, type.getId());

        selectTypeAdapter.addAll(tree);
        selectTypeAdapter.notifyDataSetChanged();
        rvSelect.setVisibility(View.VISIBLE);
    }

    private List<Type> tree = new ArrayList<>();
    // 找出目标节点及所有父节点
    private void findTree(List<Type> list, int id){
        for(Type type : list){
            if(type.getId() == id) {
                // 保存到tree里
                tree.add(0, type);

                // 不是跟节点，继续查找上一级
                if(!type.isRoot())
                    findTree(types, type.getPid());
            }

            findTree(type.getChildren(), id);
        }
    }

    private void initSelect(){
        FlowLayoutManager layoutManager = new FlowLayoutManager(this, false);
        rvSelect.setLayoutManager(layoutManager);
        selectTypeAdapter = new SelectTypeAdapter();
        rvSelect.setAdapter(selectTypeAdapter);
        rvSelect.setVisibility(View.GONE);
    }

    private void initParent(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        parentTypeAdapter = new ParentTypeAdapter();
        rvList.setAdapter(parentTypeAdapter);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if(selectTypeAdapter.getItemCount() > 0){
            Type type = selectTypeAdapter.remove();
            parentTypeAdapter.clear();
            if(type == null){
                rvSelect.setVisibility(View.GONE);
                parentTypeAdapter.addAll(types);
                parentTypeAdapter.notifyDataSetChanged();
            }else{
                resetSelectType(type);
                parentTypeAdapter.addAll(type.getChildren());
                parentTypeAdapter.notifyDataSetChanged();
            }
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.iv_back, R.id.iv_delete})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.iv_delete:{
                etKey.setText("");
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
