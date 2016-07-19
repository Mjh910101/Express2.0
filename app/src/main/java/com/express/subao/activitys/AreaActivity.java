package com.express.subao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.AreaAdaper;
import com.express.subao.box.AreaObj;
import com.express.subao.box.handlers.AreaObjHandler;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.interfaces.CallbackForString;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 15/12/22.
 */
public class AreaActivity extends BaseActivity {

    public final static int REQUEST_CODE = 100;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.area_progress)
    private ProgressBar progress;
    @ViewInject(R.id.area_dataList)
    private ListView dataList;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        downloadData();
    }

    @OnClick({R.id.title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.area_text));
        TitleHandler.setTitle(context, titleLayout);
    }


    public void setAreaData(JSONArray array) {
        List<AreaObj> liat = AreaObjHandler.getAreaObjList(array);
        AreaAdaper adaper = new AreaAdaper(context, liat);
        adaper.setCallback(new CallbackForString() {
            @Override
            public void callback(String result) {
                AreaObjHandler.saveAreaName(context, result);
                finish();
            }
        });
        dataList.setAdapter(adaper);
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getArea();

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress.setVisibility(View.GONE);
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {

                            if (JsonHandle.getInt(json, "status") == 1) {
                                JSONArray array = JsonHandle.getArray(json, "results");

                                if (array != null) {
                                    setAreaData(array);
                                }
                            }

                        }

                    }

                });
    }

}
