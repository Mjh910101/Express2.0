package com.express.subao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.SdyOrderObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.StoreObj;
import com.express.subao.box.UserAddressObj;
import com.express.subao.box.handlers.SdyOrderObjHandler;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.express.subao.box.handlers.UserAddressObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.views.ItemOrderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * Created by Hua on 16/7/12.
 */
public class ItemOrderLiatActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.itemOrder_progress)
    private ProgressBar progress;
    @ViewInject(R.id.itemOrder_orderLayout)
    private LinearLayout orderLayout;
    @ViewInject(R.id.orderHead_notAddressLayout)
    private RelativeLayout notAddressLayout;
    @ViewInject(R.id.orderHead_addressLayout)
    private RelativeLayout addressLayout;
    @ViewInject(R.id.orderHead_userNameText)
    private TextView userNameText;
    @ViewInject(R.id.orderHead_userTelText)
    private TextView userTelText;
    @ViewInject(R.id.orderHead_userAddressText)
    private TextView userAddressText;

    private Map<String, List<StoreItemObj>> orderMap;
    private List<ItemOrderView> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_order_list);

        context = this;
        ViewUtils.inject(this);

        initActivity();

    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText("填寫訂單");

        orderMap = ShoppingCarHandler.getChoiseMap();
        if (orderMap == null) {
            finish();
        } else {
            initOrderList(orderMap);
            downloadAddressData();
        }
    }

    private void initOrderList(Map<String, List<StoreItemObj>> map) {
        orderList = new ArrayList<>();
        for (Map.Entry entry : map.entrySet()) {
            String id = (String) entry.getKey();
            StoreObj obj = ShoppingCarHandler.getStoreForId(context, id);
            if (obj != null) {
                ItemOrderView view = new ItemOrderView(context, obj, map.get(id));
                orderList.add(view);
                orderLayout.addView(getFillers());
                orderLayout.addView(view);
            }
        }
    }


    private void downloadAddressData() {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getUserAddressDefault() + "?sessiontoken=" + UserObjHandler.getSessionToken(context);

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
                                JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                                if (resultsJson != null) {
                                    UserAddressObj address = UserAddressObjHandler.getUserAddressObj(resultsJson);
                                    setDeletedAddress(address);
                                }
                            }
                        }
                    }

                });
    }

    public void setDeletedAddress(UserAddressObj obj) {
        if (!obj.isNull()) {
            notAddressLayout.setVisibility(View.GONE);
            addressLayout.setVisibility(View.VISIBLE);

            userNameText.setText("收貨人：" + obj.getReceiver());
            userTelText.setText(obj.getContact());
            userAddressText.setText(obj.getAddress());
        }
    }

    public View getFillers() {
        View v = new View(context);
        v.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
        return v;
    }
}
