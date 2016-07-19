package com.express.subao.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.ExpresAdaper;
import com.express.subao.adaptera.QueryExpresAdaper;
import com.express.subao.box.ExpresObj;
import com.express.subao.box.handlers.ExpresObjHandler;
import com.express.subao.handlers.ColorHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
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
 * Created by Hua on 15/12/24.
 */
public class QueryExpressActivity extends BaseActivity {

    private final static int NO_RECEIVED = 1;
    private final static int RECEIVED = 2;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.query_express_notReceivedText)
    private TextView notReceivedText;
    @ViewInject(R.id.query_express_receivedText)
    private TextView receivedText;
    @ViewInject(R.id.query_express_notReceivedLine)
    private View notReceivedLine;
    @ViewInject(R.id.query_express_receivedLine)
    private View receivedLine;
    @ViewInject(R.id.query_express_progress)
    private ProgressBar progress;
    @ViewInject(R.id.query_express_dataList)
    private ListView dataList;
    @ViewInject(R.id.query_express_codeInput)
    private EditText codeInput;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    private List<ExpresObj> notReceivedList;
    private List<ExpresObj> receivedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_express);

        context = this;
        ViewUtils.inject(this);

        initActivity();
        downloadData();
    }

    @OnClick({R.id.title_back, R.id.query_express_checkBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.query_express_checkBtn:
                jumpCheckActivity();
                break;
        }
    }

    @OnClick({R.id.query_express_notReceivedBox, R.id.query_express_receivedBox})
    public void onClickTitle(View view) {
        initTitle();
        switch (view.getId()) {
            case R.id.query_express_notReceivedBox:
                notReceivedText.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
                notReceivedLine.setBackgroundResource(R.color.text_orange);
                notReceivedLine.setVisibility(View.VISIBLE);
                dataList.setAdapter(new QueryExpresAdaper(context, notReceivedList, false));
                break;
            case R.id.query_express_receivedBox:
                receivedText.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
                receivedLine.setBackgroundResource(R.color.text_orange);
                receivedLine.setVisibility(View.VISIBLE);
                dataList.setAdapter(new QueryExpresAdaper(context, receivedList, false));
                break;
        }
    }

    private void initTitle() {
        notReceivedText.setTextColor(ColorHandler.getColorForID(context, R.color.text_gray_01));
        receivedText.setTextColor(ColorHandler.getColorForID(context, R.color.text_gray_01));

        notReceivedLine.setBackgroundResource(R.color.text_gray_01);
        receivedLine.setBackgroundResource(R.color.text_gray_01);

        notReceivedLine.setVisibility(View.GONE);
        receivedLine.setVisibility(View.GONE);
    }


    private void jumpCheckActivity() {
        String code = TextHandeler.getText(codeInput);
        if (code.equals("")) {
            MessageHandler.showToast(context, "请输入快递单号");
        } else {
            Bundle b = new Bundle();
            b.putString(CheckExpressActivity.CODE_KEY, code);
            Passageway.jumpActivity(context, CheckExpressActivity.class, b);
        }
    }

    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.query_express_text));

        notReceivedList = new ArrayList<ExpresObj>();
        receivedList = new ArrayList<ExpresObj>();

        setTitleListSize();
    }

    private void setTitleListSize() {
        notReceivedText.setText("途中(" + String.valueOf(notReceivedList.size()) + ")");
        receivedText.setText("已送達(" + String.valueOf(receivedList.size()) + ")");
    }

    private void finishingList(List<ExpresObj> list) {
        for (ExpresObj obj : list) {
            if (obj.getStatus() == 1) {
                receivedList.add(obj);
            } else {
                notReceivedList.add(obj);
            }
        }
        setTitleListSize();
        findViewById(R.id.query_express_notReceivedBox).performClick();
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        String url = Url.getExpress();

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
                                    List<ExpresObj> list = ExpresObjHandler.getExpresObjList(array);
                                    finishingList(list);
                                }
                            }

                        }
                    }

                });
    }


}
