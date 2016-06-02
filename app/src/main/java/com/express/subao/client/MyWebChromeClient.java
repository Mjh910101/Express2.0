package com.express.subao.client;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.express.subao.interfaces.CallbackForString;


public class MyWebChromeClient extends WebChromeClient {

	private CallbackForString callback;

	public MyWebChromeClient(CallbackForString callback) {
		this.callback = callback;
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
		if (callback != null) {
			callback.callback(title);
		}
	}
}
