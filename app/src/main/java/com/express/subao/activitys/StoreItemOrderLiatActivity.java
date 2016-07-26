package com.express.subao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.StoreObj;
import com.express.subao.box.UserAddressObj;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.express.subao.box.handlers.UserAddressObjHandler;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.express.subao.views.ItemOrderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Identity;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
public class StoreItemOrderLiatActivity extends BaseActivity {

    public final static int RC = 1005;

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
    @ViewInject(R.id.itemOrder_totalPriceText)
    private TextView totalPriceText;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    private Map<String, List<StoreItemObj>> orderMap;
    private List<ItemOrderView> orderList;
    private Map<String, ItemOrderView> orderViewMap;

    private String ordersn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_order_list);

        context = this;
        ViewUtils.inject(this);

        initActivity();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC:
                setDeletedAddress(UserAddressObjHandler.getUserAddressObj());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.title_back, R.id.orderHead_headLayout, R.id.itemOrder_commitOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.orderHead_headLayout:
                Passageway.jumpActivity(context, UserAddressListActivity.class, RC);
                break;
            case R.id.itemOrder_commitOrder:
                if (!ordersn.equals("")) {
                    commitOrderPre();
                }
                break;
        }
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText("確認訂單");

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
        orderViewMap = new HashMap<>();
        for (Map.Entry entry : map.entrySet()) {
            String id = (String) entry.getKey();
            StoreObj obj = ShoppingCarHandler.getStoreForId(context, id);
            if (obj != null) {
                ItemOrderView view = new ItemOrderView(context, obj, map.get(id));
                orderList.add(view);
                orderViewMap.put(id, view);
                orderLayout.addView(getFillers());
                orderLayout.addView(view);
            }
        }
        totalPriceText.setText(new DecimalFormat("0.00").format(gettotalPrice()));
    }

    public double gettotalPrice() {
        double s = 0;
        for (ItemOrderView view : orderList) {
            s += view.getItemSumPrice();
        }
        return s;
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
        if (obj != null && !obj.isNull()) {
            notAddressLayout.setVisibility(View.GONE);
            addressLayout.setVisibility(View.VISIBLE);

            userNameText.setText("收貨人：" + obj.getReceiver());
            userTelText.setText(obj.getContact());
            userAddressText.setText(obj.getAddress());
            uploadOrderPre(obj.getObjectId());
        }
    }

    public View getFillers() {
        View v = new View(context);
        v.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
        return v;
    }


    public JSONObject getStoreItemJson() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for (ItemOrderView view : orderList) {
            List<StoreItemObj> list = view.getStoreItemList();
            for (StoreItemObj obj : list) {
                JsonHandle.put(array, obj.toJson());
            }
        }
        JsonHandle.put(json, "cartData", array);
        return json;
    }

    public JSONObject getStoreJson() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for (ItemOrderView view : orderList) {
            JsonHandle.put(array, view.toJson());
        }
        JsonHandle.put(json, "remark", array);
        return json;
    }

    private void updateOrderView(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = JsonHandle.getJSON(array, i);
            orderViewMap.get(JsonHandle.getString(json, "store_id")).upload(json);
        }
    }

    private void deleteItem(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            ShoppingCarHandler.deleteItem(context, JsonHandle.getString(array, i));
        }
    }

    private void uploadOrderPre(String id) {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getOrderPre();

        RequestParams params = new RequestParams();
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("address_id", id);
        params.addBodyParameter("data", getStoreItemJson().toString());


        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
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
                            JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                            if (JsonHandle.getInt(json, "status") == 1) {
                                if (resultsJson != null) {
                                    JSONArray ordersArray = JsonHandle.getArray(resultsJson, "orders");
                                    if (ordersArray != null) {
                                        updateOrderView(ordersArray);
                                    }
                                }
                                ordersn = JsonHandle.getString(json, "ordersn");
                            } else {
                                ordersn = "";
                                MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                            }
                        }
                    }

                });
    }

    private void commitOrderPre() {
        progress.setVisibility(View.VISIBLE);
        String url = Url.getOrderCommit();

        RequestParams params = new RequestParams();
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("ordersn", ordersn);
        params.addBodyParameter("data", getStoreJson().toString());

        Log.e("ordersn", ordersn);
        Log.e("data", getStoreJson().toString());

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
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
                                    if (JsonHandle.getInt(json, "status") == 1) {
                                        if (resultsJson != null) {
                                            deleteItem(JsonHandle.getArray(resultsJson, "items"));
                                            finish();
                                        }
                                        ordersn = JsonHandle.getString(json, "ordersn");
                                    } else {
                                        MessageHandler.showToast(context, JsonHandle.getString(resultsJson, "message"));
                                    }
                                }
                            }
                        }
                    }

                });
    }

}
