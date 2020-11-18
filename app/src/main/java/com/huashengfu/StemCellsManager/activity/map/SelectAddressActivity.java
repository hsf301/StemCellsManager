package com.huashengfu.StemCellsManager.activity.map;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.map.AddressAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 地址选择
public class SelectAddressActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView mapView;

//    @BindView(R.id.progressBar)
//    ProgressBar progressBar;
//    @BindView(R.id.tv_address)
//    TextView tvAddress;

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;

    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;

    private Unbinder unbinder;
    private AMap aMap;
    private GeocodeSearch geocodeSearch;

    private AddressAdapter addressAdapter;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private Marker marker;

    private PoiSearch poiSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        unbinder = ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0)
                    ivDelete.setVisibility(View.VISIBLE);
                else
                    ivDelete.setVisibility(View.INVISIBLE);

                doQuery();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        addressAdapter = new AddressAdapter();
        rvList.setAdapter(addressAdapter);

        mySwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorSwipeRefreshLayout1),
                getResources().getColor(R.color.colorSwipeRefreshLayout2),
                getResources().getColor(R.color.colorSwipeRefreshLayout3)
        );


        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doQuery();
            }
        });


        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if(i == 1000){
                    RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();

                    Log.i(Constants.Log.Log, address.toString());

                    for(PoiItem poiItem : address.getPois()){
                        Log.i(Constants.Log.Log, poiItem.toString());
                    }

                    GeocodeQuery query = new GeocodeQuery(address.getFormatAddress(),address.getCityCode());
                    geocodeSearch.getFromLocationNameAsyn(query);

//                    tvAddress.setText(address.getFormatAddress());
//                    tvAddress.setTag(address);
//                    progressBar.setVisibility(View.GONE);

//                    mySwipeRefreshLayout.setRefreshing(false);
                }else{
//                    tvAddress.setText(R.string.str_search_address_click_map);
//                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                mySwipeRefreshLayout.setRefreshing(false);

//                addressAdapter.clear();
//                addressAdapter.addAll(geocodeResult.getGeocodeAddressList());
//                addressAdapter.notifyDataSetChanged();
            }
        });

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

//        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                LatLng latLng = marker.getPosition();
//                addMarker(latLng);
//
//                return false;
//            }
//        });

//        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                addMarker(latLng);
//            }
//        });

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                aMap.clear();
                addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                addMarker(cameraPosition.target);
            }
        });
    }

    private void addMarker(LatLng latLng){
        if(marker != null)
            marker.remove();

        marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker)));


//        tvAddress.setText(R.string.str_search_address);
//        progressBar.setVisibility(View.VISIBLE);

        mySwipeRefreshLayout.post(()->{
            mySwipeRefreshLayout.setRefreshing(true);

//            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200,GeocodeSearch.AMAP);
//            geocodeSearch.getFromLocationAsyn(query);


            doQuery();

        });
    }

    private void doQuery(){
        addressAdapter.clear();
        addressAdapter.notifyDataSetChanged();

        String key  = etKey.getText().toString();
        PoiSearch.Query query = new PoiSearch.Query(key, null, null);

        PoiSearch poiSearch = new PoiSearch(getApplicationContext(), query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude), 10000));
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                Log.i(Constants.Log.Log, "onPoiSearched -> " + poiResult.toString());
                for(PoiItem item : poiResult.getPois()){
                    Log.i(Constants.Log.Log, "item --> " + item.toString());
                }

                addressAdapter.clear();
                addressAdapter.addAll(poiResult.getPois());
                addressAdapter.notifyDataSetChanged();

                mySwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    @OnClick({R.id.iv_back, R.id.tv_ok})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.tv_ok:{
//                Object obj = tvAddress.getTag();
//                if(obj != null){
//                    Intent data = new Intent();
//                    RegeocodeAddress address = (RegeocodeAddress)obj;
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(Constants.Tag.data, address);
//                    data.putExtras(bundle);
//                    setResult(RESULT_OK, data);
//                    finish();
//                }else{
//                    finish();
//                }
                PoiItem poiItem = addressAdapter.getSelect();
                if(poiItem != null){
                    Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.Tag.data, poiItem);
                    data.putExtras(bundle);
                    setResult(RESULT_OK, data);
                }
                finish();
                break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
