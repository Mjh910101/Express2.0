package com.express.subao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.StoreAdapter;
import com.express.subao.box.CategoryObj;
import com.express.subao.box.SliderObj;
import com.express.subao.box.StoreObj;
import com.express.subao.box.handlers.CategoryObjHandler;
import com.express.subao.box.handlers.SliderObjHandler;
import com.express.subao.box.handlers.StoreObjHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.views.HeaderGridView;
import com.express.subao.views.HeaderSliderView;
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
 * Created by Hua on 15/12/23.
 */
public class CategoryActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.category_progress)
    private ProgressBar progress;
    @ViewInject(R.id.category_dataGrid)
    private HeaderGridView dataGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

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
        CategoryObj mCategoryObj = CategoryObjHandler.getCategoryObj();
        if (mCategoryObj != null) {
            titleName.setText(mCategoryObj.getTitle());
            downloadData(mCategoryObj.getObjectId());
        }
    }

    private void setDataView(List<SliderObj> sliderList, List<StoreObj> storeObj) {
        dataGrid.addHeaderView(new HeaderSliderView(context,sliderList));
        dataGrid.setAdapter(new StoreAdapter(context,storeObj));
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getCategory(id);

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
                                if (resultJson != null) {
                                    List<SliderObj> sliderList= SliderObjHandler.getSliderObjList(JsonHandle.getArray(resultJson,"slider"));
                                    List<StoreObj> storeObj= StoreObjHandler.getStoreObjList(JsonHandle.getArray(resultJson,"store"));
                                    setDataView(sliderList,storeObj);
                                }
                            }

                        }

                    }

                });
    }


}
