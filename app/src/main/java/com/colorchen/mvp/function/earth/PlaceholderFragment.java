package com.colorchen.mvp.function.earth;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.colorchen.R;
import com.colorchen.utils.Logger;

/**
 * 功能：
 * 1 地图地点名字和经纬度相互转换
 * Author ChenQ on 2017/7/19
 * email：wxchenq@yutong.com
 */
public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int typePage = 1;//分页的标识
    private View rootView = null;
    private BaiDuEarthActivity mActivit;


    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    BaiduMap mBaiDuMap = null;
    MapView mMapView = null;

    public PlaceholderFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivit = (BaiDuEarthActivity) context;
    }

    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        typePage = getArguments().getInt(ARG_SECTION_NUMBER);
        if (typePage == 1) {
            rootView = inflater.inflate(R.layout.fragment_bai_du_earth, container, false);
        } else if (typePage == 2) {
            rootView = inflater.inflate(R.layout.fragment_bai_du_earth, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_bai_du_earth_geocoder, container, false);
        }

        Logger.i("earth", "onCreateView:" + typePage);
        initMap(rootView);
        return rootView;
    }

    private void initMap(final View rootView) {
        if (typePage == 1){
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, typePage));
        }else  if (typePage == 2){
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, typePage));
        }else{
            // 地图初始化
            mMapView = (MapView) rootView.findViewById(R.id.bMapView);
            mBaiDuMap = mMapView.getMap();
            // 初始化搜索模块，注册事件监听
            mSearch = GeoCoder.newInstance();
            mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        Toast.makeText(getContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    mBaiDuMap.clear();
                    mBaiDuMap.addOverlay(new MarkerOptions().position(result.getLocation())
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_markc)));
                    mBaiDuMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                            .getLocation()));
                    String strInfo = String.format("纬度：%f 经度：%f",
                            result.getLocation().latitude, result.getLocation().longitude);
                    Toast.makeText(getContext(), strInfo, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        Toast.makeText(getContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    String strInfo = String.format("纬度：%f 经度：%f",
                            result.getLocation().latitude, result.getLocation().longitude);
                    Toast.makeText(getContext(), strInfo,
                            Toast.LENGTH_LONG).show();
                    mBaiDuMap.clear();
                    mBaiDuMap.addOverlay(new MarkerOptions().position(result.getLocation())
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_markc)));
                    mBaiDuMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                            .getLocation()));
                    Toast.makeText(getContext(), result.getAddress(),
                            Toast.LENGTH_LONG).show();
                }
            });
            final EditText editCity = (EditText) rootView.findViewById(R.id.city);
            final EditText editGeoCodeKey = (EditText) rootView.findViewById(R.id.geocodekey);
            final EditText lat = (EditText) rootView.findViewById(R.id.lat);
            final EditText lon = (EditText) rootView.findViewById(R.id.lon);
            rootView.findViewById(R.id.geocode).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Geo搜索
                    mSearch.geocode(new GeoCodeOption().city(
                            editCity.getText().toString()).address(editGeoCodeKey.getText().toString()));
                }
            }); rootView.findViewById(R.id.reverseGeocode).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LatLng ptCenter = new LatLng((Float.valueOf(lat.getText()
                            .toString())), (Float.valueOf(lon.getText().toString())));
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
                    Logger.i("searchButtonStr",""+lat.getText().toString()+"="+lon.getText().toString());
                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mSearch != null) {
            mSearch.destroy();
        }
        super.onDestroy();
    }
}
