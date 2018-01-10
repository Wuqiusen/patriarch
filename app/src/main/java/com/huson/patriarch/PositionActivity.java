package com.huson.patriarch;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.mylibrary.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PositionActivity extends AppCompatActivity implements LocationSource, AMap.OnMapLoadedListener, AMap.OnMapClickListener, AMapLocationListener {
    @Bind(R.id.map_position)
    MapView mapPosition;

    private AMap aMap;
    private UiSettings mUiSetting;
    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption mLocationOption;
    private List<Marker> mMarkers = new ArrayList<>();
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        ButterKnife.bind(this);
        mapPosition.onCreate(savedInstanceState);
    }

    private void initMap() {
        aMap = mapPosition.getMap();

        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        if (aMap != null) {
            mUiSetting = aMap.getUiSettings();
            aMap.setTrafficEnabled(false);
            aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.setOnMapLoadedListener(this);
            aMap.setOnMapClickListener(this);
            mUiSetting.setAllGesturesEnabled(true);

        }

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (locationClient == null) {
            //初始化定位
            locationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            locationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            locationClient.setLocationOption(mLocationOption);
            locationClient.startLocation();//启动定位
        }
    }
    @Override
    public void deactivate() {
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }


    @Override
    public void onMapLoaded() {
        LatLng marker1 = new LatLng(22.543096, 114.057865);    //设置中心点和缩放比例
        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        }
        DebugLog.i("地图加载完成");
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        DebugLog.w("lc" + amapLocation.getLatitude() + "," + amapLocation.getLongitude());
        if (amapLocation.getLatitude() == 0 && amapLocation.getLongitude() == 0)
            return;
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mapPosition.onDestroy()，实现地图生命周期管理
        mapPosition.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mapPosition.onResume ()，实现地图生命周期管理
        mapPosition.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mapPosition.onPause ()，实现地图生命周期管理
        mapPosition.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mapPosition.onSaveInstanceState (outState)，实现地图生命周期管理
        mapPosition.onSaveInstanceState(outState);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null)
            currentMarker.hideInfoWindow();
    }

    private void drawMap( List<LatLng> latLngs) {
        Marker marker;
        // 画线
        aMap.addPolyline((new PolylineOptions().addAll(latLngs)).color(getResources().getColor(R.color.main_img)));
    }

}
