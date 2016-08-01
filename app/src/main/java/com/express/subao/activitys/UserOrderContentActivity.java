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
import com.express.subao.adaptera.OrderItemAdapter;
import com.express.subao.adaptera.UserOrderAdaper;
import com.express.subao.box.OrderObj;
import com.express.subao.box.handlers.OrderObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
 * Created by Hua on 16/7/29.
 */
public class UserOrderContentActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;
    @ViewInject(R.id.userOrderContent_progress)
    private ProgressBar progress;
    @ViewInject(R.id.userOrderContent_addressText)
    private TextView addressText;
    @ViewInject(R.id.userOrderContent_userNameText)
    private TextView userNameText;
    @ViewInject(R.id.userOrderContent_userTelText)
    private TextView userTelText;
    @ViewInject(R.id.userOrderContent_storeName)
    private TextView storeName;
    @ViewInject(R.id.userOrderContent_payText)
    private TextView payText;
    @ViewInject(R.id.userOrderContent_sendText)
    private TextView sendText;
    @ViewInject(R.id.userOrderContent_dataList)
    private ListView dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_content);

        context = this;
        ViewUtils.inject(this);

        initActivity();
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
        titleName.setText("訂單詳情");
        TitleHandler.setTitle(context, titleLayout);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            downloadData(b.getString("id"));
        } else {
            finish();
        }
    }

    private void setDataMessage(OrderObj obj) {
        addressText.setText(obj.getAddressInfo());
        userNameText.setText(obj.getUserName());
        userTelText.setText(obj.getUserTel());
        storeName.setText(obj.getStoreName());
        payText.setText(obj.getPayment_type());
        sendText.setText(obj.getShipment_type());
        if (!obj.isNull()) {
            dataList.setAdapter(new OrderItemAdapter(context, obj.getItemList()));
        }
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getOrder(id) + "?sessiontoken=" + UserObjHandler.getSessionToken(context);

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
                                JSONObject resultJson = JsonHandle.getJSON(json, "results");
                                OrderObj obj = OrderObjHandler.getOrderObj(resultJson);
                                setDataMessage(obj);
                            }

                        }

                    }

                });
    }

}
