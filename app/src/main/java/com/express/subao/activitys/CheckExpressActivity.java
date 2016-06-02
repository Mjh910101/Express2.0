package com.express.subao.activitys;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.adaptera.ExpresAdaper;
import com.express.subao.box.ExpresObj;
import com.express.subao.box.handlers.ExpresObjHandler;
import com.express.subao.handlers.ColorHandler;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.views.InsideListView;
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
 * Created by Hua on 16/1/28.
 */
public class CheckExpressActivity extends BaseActivity {

    public final static String CODE_KEY = "code";

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.checkExpress_progress)
    private ProgressBar progress;
    @ViewInject(R.id.checkExpress_code)
    private TextView codeText;
    @ViewInject(R.id.checkExpress_dataList)
    private InsideListView dataList;

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_express);

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
        titleName.setText("快遞跟蹤");

        Bundle b = getIntent().getExtras();
        if (b != null) {
//            code = b.getString(CODE_KEY);
            code = "518071530781";
            codeText.setText(code);
            dowbloadData(code);
        } else {
            finish();
        }
    }


    public void setExpressView(JSONObject json) {
        JSONArray array = JsonHandle.getArray(json, "list");
        if (array != null) {
            dataList.setAdapter(new ExpresDataAdaper(array));
        }
    }

    private void dowbloadData(String code) {
        progress.setVisibility(View.VISIBLE);

        String url = "http://api.jisuapi.com/express/query" +
                "?appkey=4c3165e53d6cb0d1&type=auto&number=" + code;

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
                            if (JsonHandle.getInt(json, "status") == 0) {
                                JSONObject resultJson = JsonHandle.getJSON(json, "result");
                                if (resultJson != null) {
                                    setExpressView(resultJson);
                                }
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "msg"));
                            }
                        }
                    }

                });
    }

    class ExpresDataAdaper extends BaseAdapter {

        JSONArray array;
        LayoutInflater inflater;

        ExpresDataAdaper(JSONArray array) {
            this.array = array;
            initBaseAdapter();
        }

        private void initBaseAdapter() {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return array.length();
        }

        @Override
        public Object getItem(int position) {
            return JsonHandle.getJSON(array, position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holder;
            if (convertView == null) {
                convertView = inflater.inflate(
                        R.layout.layout_expres_data_items, null);
                holder = new HolderView();

                holder.name = (TextView) convertView.findViewById(R.id.expresData_name);
                holder.time = (TextView) convertView.findViewById(R.id.expresData_time);
                holder.icon = (ImageView) convertView.findViewById(R.id.expresData_icon);

                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }

            setMessage(holder, position);

            return convertView;
        }

        private void setMessage(HolderView holder, int i) {
            JSONObject json = JsonHandle.getJSON(array, i);
            holder.name.setText(JsonHandle.getString(json, "status"));
            if (i == 0) {
                holder.icon.setImageResource(R.drawable.orange_boll_icon);
                holder.name.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
                holder.time.setTextColor(ColorHandler.getColorForID(context, R.color.text_orange));
            } else {
                holder.icon.setImageResource(R.drawable.gary_boll_icon);
                holder.name.setTextColor(ColorHandler.getColorForID(context, R.color.text_gray_01));
                holder.time.setTextColor(ColorHandler.getColorForID(context, R.color.text_gray_01));
            }

            String time = JsonHandle.getString(json, "time");
            if (time.indexOf("\"") > 0) {
                time=time.substring(0, time.indexOf("\""));
            }
            holder.time.setText(time);

        }
    }


    class HolderView {
        TextView name;
        TextView time;
        ImageView icon;
    }

}
