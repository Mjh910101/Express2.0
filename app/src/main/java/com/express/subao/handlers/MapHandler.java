package com.express.subao.handlers;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class MapHandler {


    public interface MapListener {
        public void callback(BDLocation location);
    }

    public static LocationClient getAddress(final Context context, final MapListener callback) {
        final LocationClient locationClient = new LocationClient(context);
        LocationClientOption option = getLocationClientOption();
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location != null) {
                    Log.e("baidu", "lat: " + location.getLatitude() + "    lon : "
                            + location.getLongitude() + "      add : " + location.getAddrStr());
                    callback.callback(location);
                    if (location.getAddrStr() == null || location.getAddrStr().equals("null")) {
                        MessageHandler.showToast(context,"請確定您已經開啟速寶的定位權限");
                    }
                }
                locationClient.stop();
            }
        });

        locationClient.start();
        return locationClient;
    }

    private static LocationClientOption getLocationClientOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setOpenGps(true);
        int span = 1000;
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
//        option.SetIgnoreCacheException(true);
        return option;
    }

}
