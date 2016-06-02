package com.express.subao.views;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VestrewWebView extends WebView {

    private final static String HEAD = "<html><head><style>img { max-width: 100%; width: auto; height: auto; } p{word-break:break-all; width:100%;margin-bottom:8px;margin-left:0px;}</style></head><body style='margin:5px; padding:0;'>";
    private final static String BR = "<br><br>";
    private final static String FOOT = "</body></html>";

    public VestrewWebView(Context context) {
        super(context);
        setWebViewClient(client);
    }

    public VestrewWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWebViewClient(client);
    }

    public VestrewWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWebViewClient(client);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data,
                                    String mimeType, String encoding, String historyUrl) {
        Log.e("HTTP", data);
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    private WebViewClient client = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }
    };

    public void loadDataWithBaseURL(String baseUrl, String data,
                                    String historyUrl) {
        loadDataWithBaseURL(baseUrl, data, "text/html", "utf-8", historyUrl);
    }

    public void loadData(String data) {
        StringBuffer sb = new StringBuffer();
        sb.append(HEAD);
        sb.append(data);
        sb.append(FOOT);
        loadDataWithBaseURL(null, sb.toString(), null);
    }

    public void loadDataWithBaseURL(String data, boolean isBr) {
        StringBuffer str = new StringBuffer();
        str.append(HEAD);
        str.append(Html.fromHtml(data).toString());
        if (isBr) {
            str.append(BR);
        }
        str.append(FOOT);
        loadDataWithBaseURL(null, str.toString(), null);
    }

    public void loadDataWithBaseURL(String data) {
        loadDataWithBaseURL(data, true);
    }

    public void omitDataWithBaseURL(String data) {
        StringBuffer sb = new StringBuffer();
        sb.append(HEAD);
        sb.append(Html.fromHtml(data).toString());
        sb.append(FOOT);
        String str = fiterHtmlTag(sb.toString(), "img", "<br>...");
        loadDataWithBaseURL(null, str, null);
    }

    public static String fiterHtmlTag(String str, String tag, String replace) {
        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, replace);
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String addJavaScript(String str) {
        try {
            String newStr = str;
            Pattern pattern = Pattern.compile("<img(.*?)>");
            Matcher matcher = pattern.matcher(newStr);
            while (matcher.find()) {
                String basisImg = matcher.group();
                int list;
                if (basisImg.indexOf(".jpg", 0) >= 0) {
                    list = basisImg.indexOf(".jpg") + 4;
                } else if (basisImg.indexOf(".JPG", 0) >= 0) {
                    list = basisImg.indexOf(".JPG") + 4;
                } else if (basisImg.indexOf(".PNG", 0) >= 0) {
                    list = basisImg.indexOf(".PNG") + 4;
                } else {
                    list = basisImg.indexOf(".png") + 4;
                }
                Log.d("img", basisImg);
                String url = basisImg.substring(basisImg.indexOf("src=") + 5, list);
                Log.d("url", url);
                String newImg = basisImg.replaceAll(">",
                        " onClick=\"window.ImageOnClick.onClickForImg(\'" + url
                                + "\') \" >");
                Log.d("new Img", newImg);
                newStr = newStr.replaceAll(basisImg, newImg);
            }
            Log.d("newStr", newStr);
            return newStr;
        } catch (Exception e) {
            return str;
        }
    }

}
