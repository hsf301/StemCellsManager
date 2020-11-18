package com.huashengfu.StemCellsManager.activity.goods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.flag.FlagAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.address.AddressAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.address.CityAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.param.ParametersDetailAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Goods;
import com.huashengfu.StemCellsManager.entity.goods.Specifications;
import com.huashengfu.StemCellsManager.entity.response.goods.CategoryParam;
import com.huashengfu.StemCellsManager.entity.response.goods.City;
import com.huashengfu.StemCellsManager.entity.response.goods.GoodsInfo;
import com.huashengfu.StemCellsManager.entity.response.goods.Init;
import com.huashengfu.StemCellsManager.entity.response.goods.Parameters;
import com.huashengfu.StemCellsManager.entity.response.goods.Province;
import com.huashengfu.StemCellsManager.entity.response.goods.Sku;
import com.huashengfu.StemCellsManager.entity.response.goods.Type;
import com.huashengfu.StemCellsManager.entity.response.goods.Upload;
import com.huashengfu.StemCellsManager.event.goods.UpdateGoodsInfoEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.convert.JsonConvert;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okrx2.adapter.ObservableResponse;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// 编辑商品信息
public class ModifyGoodsInformactionActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @BindView(R.id.et_brand)
    EditText etBrand;
    @BindView(R.id.tv_type)
    TextView tvType;

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_price_old)
    EditText etPriceOld;
    @BindView(R.id.et_price_now)
    EditText etPriceNow;
    @BindView(R.id.et_flag)
    EditText etFlag;

    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.ll_amount)
    LinearLayout llAmount;

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_content_count)
    TextView tvContentCount;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.rg_free_shipping)
    RadioGroup rgFreeShipping;
    @BindView(R.id.rg_7days)
    RadioGroup rgSevenDays;

    @BindView(R.id.tv_name_count)
    TextView tvNameCount;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.iv_down)
    ImageView ivDown;
    @BindView(R.id.tv_param_tips)
    TextView tvParamTips;
    @BindView(R.id.ll_parameters_content)
    LinearLayout llParametersContent;
    @BindView(R.id.ll_parameters)
    LinearLayout llParameters;
    @BindView(R.id.rv_parameters)
    RecyclerView rvParameters;

    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.ll_specifications_value)
    LinearLayout llSpecificationsValue;

    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.tv_content2)
    TextView tvContent2;
    @BindView(R.id.tv_content3)
    TextView tvContent3;

    @BindView(R.id.ll_add_flag)
    LinearLayout llAddFlag;

    // 商品规格
    private ArrayList<Specifications> specifications = new ArrayList<>();

    private Unbinder unbinder;
    private FlagAdapter flagAdapter;
    private ParametersDetailAdapter parametersDetailAdapter;

    private Init init;

    private CityPopupwindow cityPopupwindow;

    /*
     商品类别
     */
    private Type currentType;

    private Goods goods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_modify_informaction);

        unbinder = ButterKnife.bind(this);

        goods = (Goods) getIntent().getSerializableExtra(Constants.Tag.data);

        cityPopupwindow = new CityPopupwindow();
        cityPopupwindow.init();

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > Constants.titleMaxLength){
                    etName.setText(editable.toString().substring(0, Constants.titleMaxLength));
                    etName.setSelection(etName.length());
                }

                tvNameCount.setText(etName.length() + "/" + Constants.titleMaxLength);
            }
        });

        etFlag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > Constants.flagMaxLength){
                    etFlag.setText(editable.toString().substring(0, Constants.flagMaxLength));
                    etFlag.setSelection(etFlag.length());
                }
            }
        });
        tvNameCount.setText(etName.length() + "/" + Constants.titleMaxLength);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > Constants.goodsContentMaxLength){
                    etContent.setText(editable.toString().substring(0, Constants.goodsContentMaxLength));
                    etContent.setSelection(etContent.length());
                }
                tvContentCount.setText(etContent.length() + "/" + Constants.goodsContentMaxLength);
            }
        });
        tvContentCount.setText(etContent.length() + "/" + Constants.goodsContentMaxLength);

        initFlag();

        initParameters();

        rgFreeShipping.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_no:{
                        llAmount.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.rb_yes:{
                        llAmount.setVisibility(View.GONE);
                        break;
                    }
                }
            }
        });

        rvList.postDelayed(()->{
            doInit(goods.getGoodsId(), 2);
        }, 200);
    }

    private void initFlag(){
        flagAdapter = new FlagAdapter();
        flagAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener<String>() {
            @Override
            public boolean onItemLongClick(View view, String flag) {
                AlertDialog dialog = new AlertDialog.Builder(ModifyGoodsInformactionActivity.this)
                        .setMessage(R.string.dialog_message_delete_service_flag)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int position = flagAdapter.remove(flag);
                                flagAdapter.notifyItemRemoved(position);
                                flagAdapter.notifyDataSetChanged();

                                llAddFlag.setEnabled(true);
                            }
                        })
                        .setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                return false;
            }
        });

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager(this, false);
        rvList.setLayoutManager(flowLayoutManager);
        rvList.setAdapter(flagAdapter);
        rvList.setNestedScrollingEnabled(false);
    }

    private void initParameters(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvParameters.setLayoutManager(layoutManager);

        parametersDetailAdapter = new ParametersDetailAdapter();
        rvParameters.setAdapter(parametersDetailAdapter);
    }

    private void requestParameters(){
        if(currentType == null){
            showMessage(R.string.error_goods_type_empty);
            return;
        }

        ArrayList<Parameters> parameters = new ArrayList<>();
        parameters.addAll(parametersDetailAdapter.getParameters());

        Intent intent = new Intent(this, GoodsParametersActivity.class);
        intent.putExtra(Constants.Tag.data, currentType.getId());
        intent.putExtra(Constants.Tag.list, parameters);
        startActivityForResult(intent, Constants.Request.SelectGoodsParameters);
    }

    @OnClick({R.id.iv_back, R.id.ll_add_flag, R.id.btn_commit,
            R.id.ll_address, R.id.ll_specifications, R.id.ll_param_hidden, R.id.ll_parameters_content})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.ll_parameters_content:{
                // 打开产参数选择页面
                requestParameters();
                break;
            }
            case R.id.ll_param_hidden:{
                llParametersContent.setVisibility(llParametersContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ivDown.setImageResource(llParametersContent.getVisibility() == View.VISIBLE ? R.mipmap.icon_arrow_up_gray : R.mipmap.icon_arrow_down_gray);
                break;
            }
            case R.id.ll_specifications:{
                Log.i(Constants.Log.Log, "specifications --> " + specifications);
                Intent intent = new Intent(this, CommoditySpecificationsActivity.class);
                intent.putExtra(Constants.Tag.data, specifications);
                startActivityForResult(intent, Constants.Request.SelectGoodsCommoditySpecifications);
                break;
            }
            case R.id.ll_add_flag:{
                if(etFlag.length() == 0 || etFlag.length() > Constants.flagMaxLength){
                    showMessage(R.string.error_service_flag);
                    break;
                }

                if(flagAdapter.getItemCount() >= Constants.flagMax){
                    showMessage(R.string.error_service_flag);
                    break;
                }

                flagAdapter.add(etFlag.getText().toString());
                flagAdapter.notifyDataSetChanged();
                etFlag.setText("");

                if(flagAdapter.getItemCount() >= Constants.flagMax){
                    llAddFlag.setEnabled(false);
                }

                break;
            }
            case R.id.ll_address:{
                cityPopupwindow.show();
                break;
            }
            case R.id.btn_commit:{
                // 分步处理，此处调用接口，提交本页面编辑的数据
                // 先上传商品规格数据
                // 再调用保存接口
                //类别
                if(currentType == null){
                    showMessage(tvType.getHint());
                    break;
                }

                //品牌
                if(etBrand.getText().length() == 0){
                    showMessage(etBrand.getHint());
                    etBrand.requestFocus();
                    break;
                }

                //名称
                if(etName.getText().length() == 0){
                    showMessage(etName.getHint());
                    etName.requestFocus();
                    break;
                }

                //简介
                if(etContent.getText().length() == 0){
                    showMessage(etContent.getHint());
                    etContent.requestFocus();
                    break;
                }

                //原价
                if(etPriceOld.getText().length() == 0){
                    showMessage(etPriceOld.getHint());

                    etPriceOld.requestFocus();
                    break;
                }

                //电话
                if(etPhone.getText().length() == 0){
                    showMessage(etPhone.getHint());
                    etPhone.requestFocus();
                    break;
                }

                if(etPhone.getText().length() != 11){
                    showMessage(R.string.error_phone_length);
                    etPhone.requestFocus();
                    break;
                }

                //地址
                if(tvAddress.getText().length() == 0){
                    showMessage(tvAddress.getHint());
                    break;
                }

                //运费
                if(rgFreeShipping.getCheckedRadioButtonId() == R.id.rb_no){
                    if(etAmount.getText().length() == 0){
                        showMessage(etAmount.getHint());
                        etAmount.requestFocus();
                        break;
                    }
                }

                showDialog(false);


                try {
                    JSONObject obj = buildParam();
                    String tmp = obj.toString().replaceAll("\\\\", "");
                    StringUtils.print(tmp);

                    OkGo.<JSONObject>put(HttpHelper.Url.Goods.modifyInfo)
                            .tag(this)
                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                            .upJson(tmp)
                            .execute(new JsonCallback<JSONObject>() {
                                @Override
                                public void onSuccess(Response<JSONObject> response) {
                                    super.onSuccess(response);
                                    try {
                                        if(ResponseUtils.ok(response.body())){
                                            goNext();
                                        }else{
                                            showMessage(ResponseUtils.getMsg(response.body()));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    dismissDialog();
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private JSONObject buildParam() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(HttpHelper.Params.goodsId, goods.getGoodsId());

        obj.put(HttpHelper.Params.brand, etBrand.getText().toString());
        //分类
        obj.put(HttpHelper.Params.categoryId, currentType.getId());
        obj.put(HttpHelper.Params.deliverAddress, tvAddress.getText().toString());

        //标签
        String[] labels = new String[flagAdapter.getFlags().size()];
        flagAdapter.getFlags().toArray(labels);
        obj.put(HttpHelper.Params.labels, new JSONArray(labels));

        //运费
        if(rgFreeShipping.getCheckedRadioButtonId() == R.id.rb_yes){
            obj.put(HttpHelper.Params.logistics, 0);
        }else{
            obj.put(HttpHelper.Params.logistics, etAmount.getText().toString());
        }

        //最低价
        obj.put(HttpHelper.Params.minPrice, etPriceNow.getText().toString());
        obj.put(HttpHelper.Params.originalPrice, etPriceOld.getText().toString());

        obj.put(HttpHelper.Params.name, etName.getText().toString());
        obj.put(HttpHelper.Params.phone, etPhone.getText().toString());
        obj.put(HttpHelper.Params.content, etContent.getText().toString());

        // 7天无理由退货
        if(rgSevenDays.getCheckedRadioButtonId() == R.id.rb_yes){
            obj.put(HttpHelper.Params.serviceRemarks, "支持七天无理由退换货");
        }else{
            obj.put(HttpHelper.Params.serviceRemarks, "不支持七天无理由退换货");
        }

        return obj;
    }

    private void searchType(List<Type> types, int id){
        for(Type type : types){
            if(type.getChildren().isEmpty()){
                if(type.getId() == id){
                    currentType = type;
                }
            }else{
                searchType(type.getChildren(), id);
            }
        }
    }

    private void goNext(){
        Goods tmp = new Goods();
        tmp.setGoodsId(goods.getGoodsId());
        tmp.setName(etName.getText().toString());
        tmp.getLabels().addAll(flagAdapter.getFlags());
        tmp.setMinPrice(Double.parseDouble(etPriceOld.getText().toString()));
        tmp.setCategoryId(currentType.getId());

        showMessage(R.string.success_goods_informaction_modify);
        EventBus.getDefault().post(new UpdateGoodsInfoEvent().setGoods(tmp));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void doInit(int id, int dNo){
        GetRequest<JSONObject> getRequest = OkGo.<JSONObject>get(HttpHelper.Url.Goods.init)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.dNo, dNo);

        if(id > 0){
            getRequest.params(HttpHelper.Params.goodsId, id);
        }

        getRequest.execute(new DialogCallback<JSONObject>(this, false) {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(Response<JSONObject> response) {
                super.onSuccess(response);
                try {
                    if(ResponseUtils.ok(response.body())){
                        StringUtils.print(response.body().toString());
                        init = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                new TypeToken<Init>(){}.getType());

                        Glide.with(getApplicationContext())
                                .load(init.getDescription().getIcon())
                                .into(ivIcon);

                        Glide.with(getApplicationContext())
                                .load(init.getDescription().getPic())
                                .into(ivPic);

                        tvTitle.setText(init.getDescription().getTitle());
                        tvContent1.setText(init.getDescription().getContent1());
                        tvContent2.setText(init.getDescription().getContent2());
                        tvContent3.setText(init.getDescription().getContent3());

                        if(init.getAddress() != null){
                            cityPopupwindow.setProvinces(init.getAddress());
                        }

                        if(init.getTypeList() != null){
                            for(Type type : init.getTypeList()){
                            }
                        }

                        if(init.getCategoryParamMap() != null){
                            for(String key : init.getCategoryParamMap().keySet()){
                                List<CategoryParam> param = init.getCategoryParamMap().get(key);
                                for(CategoryParam categoryParam : param){
                                    Log.i(Constants.Log.Log, categoryParam.getName() + " -> " + categoryParam.getId());
                                }
                            }
                        }

                        if(init.getGoodsInfo() != null){
                            GoodsInfo info = init.getGoodsInfo();

                            searchType(init.getTypeList(), info.getCategoryId());
                            if(currentType != null)
                                tvType.setText(currentType.getName());

                            etBrand.setText(info.getBrand());
                            etName.setText(info.getName());
                            etContent.setText(info.getContent());
                            etPhone.setText(info.getPhone());
                            etPriceOld.setText(String.valueOf(info.getOriginalPrice()));
                            etPriceNow.setText(String.valueOf(info.getMinPrice()));
                            tvAddress.setText(info.getDeliverAddress());
                            if(info.getLogistics() == 0){
                                rgFreeShipping.findViewById(R.id.rb_yes).performClick();
                            }else{
                                etAmount.setText(String.valueOf(info.getLogistics()));
                            }

                            if(info.getSkus().size() > 0){
                                Sku first = info.getSkus().get(0);
                                double price = first.getSkuPrice();
                                int stock = first.getSkuSum();

                                for(Sku sku : info.getSkus()){
                                    Specifications tmp = new Specifications();
                                    tmp.setSpecifications(sku.getSkuName());
                                    tmp.setNumber(sku.getSkuSum());
                                    tmp.setPrice(sku.getSkuPrice());
                                    tmp.setImage(sku.getSkuPic());
                                    specifications.add(tmp);

                                    if(price < sku.getSkuPrice()){
                                        price = sku.getSkuPrice();
                                        stock = sku.getSkuSum();
                                    }
                                }

                                tvPrice.setText(String.valueOf(price));
                                tvStock.setText(String.valueOf(stock));
                            }

                            flagAdapter.clear();
                            flagAdapter.addAll(info.getLabels());
                            flagAdapter.notifyDataSetChanged();

                            if(flagAdapter.getItemCount() >= Constants.flagMax){
                                llAddFlag.setEnabled(false);
                            }

                            parametersDetailAdapter.clear();
                            parametersDetailAdapter.addAll(info.getParams());
                            parametersDetailAdapter.notifyDataSetChanged();

                            if(parametersDetailAdapter.getItemCount() > 0){
                                llParameters.setVisibility(View.VISIBLE);
                                tvParamTips.setVisibility(View.GONE);
                            }

                            if(info.getServiceRemarks().contains("不支持")){
                                rgSevenDays.findViewById(R.id.rb_no).performClick();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.Request.SelectGoodsParameters){
                Object object = data.getSerializableExtra(Constants.Tag.data);
                if(object instanceof ArrayList){
                    ArrayList<Parameters> parameters = (ArrayList<Parameters>) object;
                    parametersDetailAdapter.clear();
                    parametersDetailAdapter.addAll(parameters);
                    parametersDetailAdapter.notifyDataSetChanged();

                    llParameters.setVisibility(View.VISIBLE);
                    tvParamTips.setVisibility(View.GONE);
                }
            }else if(requestCode == Constants.Request.SelectGoodsCommoditySpecifications){
                if(data != null){
                    ArrayList<Specifications> list = (ArrayList<Specifications>) data.getSerializableExtra(Constants.Tag.data);
                    if(!list.isEmpty()){
                        Specifications tmp = list.get(0);
                        // 总库存
                        int stock = 0;
                        for(Specifications specifications : list){
                            stock += specifications.getNumber();

                            if(tmp.getPrice() > specifications.getPrice()){
                                tmp = specifications;
                            }
                        }

                        specifications.clear();
                        specifications.addAll(list);

                        llSpecificationsValue.setVisibility(View.VISIBLE);
                        tvPrice.setText(String.valueOf(tmp.getPrice()));
                        tvStock.setText(String.valueOf(stock));

                        etPriceNow.setText(String.valueOf(tmp.getPrice()));

                        double priceOld = 0.0d;
                        if(etPriceOld.getText().length() > 0){
                            priceOld = Double.parseDouble(etPriceOld.getText().toString());

                            if(priceOld < tmp.getPrice()){
                                showMessage(R.string.error_price_low);
                            }
                        }
                    }
                }
            }
        }
    }

    private class CityPopupwindow{
        private PopupWindow popupWindow;
        private RecyclerView rvList;
        private AddressAdapter addressAdapter;
        private CityAdapter cityAdapter = new CityAdapter();
        private EditText etKey;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_goods_city, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.getBackground().setAlpha(170);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(ModifyGoodsInformactionActivity.this, 1f);
                }
            });

            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            cityAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<City>() {
                @Override
                public void onItemClick(View view, City city) {
                    setAddress(city);
                }
            });

            rvList = view.findViewById(R.id.rv_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            rvList.setLayoutManager(layoutManager);
            rvList.setNestedScrollingEnabled(false);

            etKey = view.findViewById(R.id.et_key);
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
                        if(addressAdapter != null){
                            rvList.setAdapter(addressAdapter);
                            addressAdapter.notifyDataSetChanged();
                        }else{
                            setProvinces(init.getAddress());
                        }
                    }else{
                        // 本地搜索
                        cityAdapter.clear();

                        for(Province province : init.getAddress()){
                            for(City city : province.getCityList()){
                                if(city.getCityName().contains(editable.toString())){
                                    cityAdapter.add(city);
                                }
                            }
                        }

                        rvList.setAdapter(cityAdapter);
                        cityAdapter.notifyDataSetChanged();
                    }
                }
            });

        }

        public void setProvinces(List<Province> provinces){
            try {
                InputStream is = getAssets().open("hot_city.json");
                List<City> cities = new Gson().fromJson(new InputStreamReader(is), new TypeToken<List<City>>(){}.getType());

                addressAdapter = new AddressAdapter(provinces, cities);
                addressAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<City>() {
                    @Override
                    public void onItemClick(View view, City city) {
                        setAddress(city);
                    }
                });

                rvList.setAdapter(addressAdapter);
                addressAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setAddress(City city){
            for(Province province : init.getAddress()){
                if(province.getProvinceCode().startsWith(city.getCityCode().substring(0, 2))){
                    tvAddress.setText(province.getProvinceName() + " " + city.getCityName());
                    dismiss();
                    return;
                }
            }
        }

        public boolean isShowing(){
            return popupWindow.isShowing();
        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(){
            etKey.setText("");
            rvList.scrollTo(0, 0);
            ViewUtils.background(ModifyGoodsInformactionActivity.this, 0.8f);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }

    }
}
