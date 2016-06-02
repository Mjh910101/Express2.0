package com.express.subao.http;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.http.RequestParams;

public class HttpUtilsBox {

	private static MyHttpUtils http;

	public static MyHttpUtils getHttpUtil() {
		if (http == null) {
			http = new MyHttpUtils();
			http.configTimeout((int) 1000 * 10);
			http.configRequestThreadPoolSize(1);
			http.configCurrentHttpCacheExpiry(0);
		}
		return http;
	}

	public static RequestParams getRequestParams(Context context) {
		RequestParams params = new RequestParams();
		return params;
	}

}
