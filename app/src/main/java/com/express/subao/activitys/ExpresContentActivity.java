package com.express.subao.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.ExpresObj;
import com.express.subao.box.RebateObj;
import com.express.subao.box.handlers.ExpresObjHandler;
import com.express.subao.box.handlers.RebateObjHandler;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.tool.Passageway;
import com.express.subao.views.LazyWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
public class ExpresContentActivity extends BaseActivity {

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.expres_content_price)
    private TextView contentPrice;
    @ViewInject(R.id.expres_content_verify)
    private TextView contentVerify;
    @ViewInject(R.id.expres_content_tips)
    private TextView contentTips;
    @ViewInject(R.id.expres_content_area)
    private TextView contentArea;
    @ViewInject(R.id.expres_content_part)
    private TextView contentPart;
    @ViewInject(R.id.expres_content_code)
    private TextView contentCode;
    @ViewInject(R.id.expres_content_companyName)
    private TextView contentCompanyName;
    @ViewInject(R.id.expres_content_postman)
    private TextView contentPostman;
    @ViewInject(R.id.expres_content_expressAt)
    private TextView contentExpressAt;
    @ViewInject(R.id.expres_content_mobBox)
    private RelativeLayout mobBox;
    @ViewInject(R.id.expres_content_mobLine)
    private View mobLine;


    private ExpresObj mExpresObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expres_content);

        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_back, R.id.expres_content_openBtn, R.id.expres_content_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.expres_content_openBtn:
                Passageway.jumpActivity(context, PayActivity.class);
                break;
            case R.id.expres_content_check:
                if (mExpresObj != null) {
                    jumpCheckActivity();
                }
                break;
        }
    }

    private void jumpCheckActivity() {
//        Bundle b = new Bundle();
//        b.putString(CheckExpressActivity.CODE_KEY, mExpresObj.getExpreser().getExpress_id());
//        Passageway.jumpActivity(context, CheckExpressActivity.class, b);
        Bundle b = new Bundle();
        b.putString(WebActivity.URL, "https://m.baidu.com/from=1013665e/s?word=" + mExpresObj.getExpreser().getCompanyInfo().getName() + "快递" + mExpresObj.getExpreser().getExpress_id());
        Passageway.jumpActivity(context, WebActivity.class, b);
    }

    private void initActivity() {
        backIcon.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.detailed_text));

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getBoolean("isShowMob")) {
                mobBox.setVisibility(View.VISIBLE);
                mobLine.setVisibility(View.VISIBLE);
            }
        }

        mExpresObj = ExpresObjHandler.getExpresObj();
        if (mExpresObj != null) {
            setMessageView(mExpresObj);
        }
    }

    public void setMessageView(ExpresObj obj) {
        contentPrice.setText("MOB" + obj.getPrice());
        contentTips.setText(obj.getTips());
        contentArea.setText("位置：" + obj.getAddress().getArea());
        contentPart.setText(obj.getAddress().getPart());
        contentCode.setText("編號：" + obj.getAddress().getCode());
        contentCompanyName.setText(obj.getExpreser().getCompanyInfo().getName() + " " + obj.getExpreser().getExpress_id());
        contentPostman.setText("快遞員" + obj.getExpreser().getPostman());
        contentExpressAt.setText("投件時間" + obj.getExpreser().getExpressAt());

        switch (obj.getStatus()) {
            case -1:
            case 0:
            case 2:
                contentVerify.setText("開箱碼：" + obj.getVerify());
                contentTips.setVisibility(View.VISIBLE);
                break;
            default:
                contentVerify.setText("已取件");
                contentTips.setVisibility(View.GONE);
                break;
        }
    }
}
