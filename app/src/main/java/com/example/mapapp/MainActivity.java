package com.example.mapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.track.TraceAnimationListener;
import com.baidu.mapapi.map.track.TraceOptions;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private BitmapDescriptor bitmapDescriptor;
    private Button button,button1,button2;

    @Override
    protected void onDestroy() {
        locationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mapView.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button5);
        button1=findViewById(R.id.button6);
        button2=findViewById(R.id.button7);
        mapView=findViewById(R.id.bmapView);
//        mapView.getMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap=mapView.getMap();

        baiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
            }
        });

//??????LocationClientOption??????LocationClient????????????
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // ??????gps
        option.setCoorType("bd09ll"); // ??????????????????
        option.setScanSpan(1000);

//??????locationClientOption
        locationClient.setLocOption(option);

//??????LocationListener?????????
        MyLocationListener myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
//????????????????????????
        locationClient.start();
        MyLocationConfiguration.LocationMode locationMode= MyLocationConfiguration.LocationMode.FOLLOWING;
        bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.kkk);


        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(locationMode,
                false,
                bitmapDescriptor,
                0,
                0));
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView ???????????????????????????????????????
            if (location == null || mapView == null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // ?????????????????????????????????????????????????????????0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
        }
    }
}