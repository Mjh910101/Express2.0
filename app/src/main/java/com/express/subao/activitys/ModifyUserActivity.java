package com.express.subao.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.express.subao.R;
import com.express.subao.box.handlers.UserObjHandler;
import com.express.subao.dialogs.ListDialog;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.handlers.AVFileHandler;
import com.express.subao.handlers.DateHandle;
import com.express.subao.handlers.JsonHandle;
import com.express.subao.handlers.MessageHandler;
import com.express.subao.handlers.TextHandeler;
import com.express.subao.handlers.TitleHandler;
import com.express.subao.http.HttpUtilsBox;
import com.express.subao.http.Url;
import com.express.subao.tool.Passageway;
import com.express.subao.tool.WinTool;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * Created by Hua on 15/12/29.
 */
public class ModifyUserActivity extends BaseActivity {

    public final static int REQUEST_CODE = 103;

    @ViewInject(R.id.title_back)
    private ImageView backIcon;
    @ViewInject(R.id.title_name)
    private TextView titleName;
    @ViewInject(R.id.modifyUser_userTel)
    private TextView userTel;
    @ViewInject(R.id.title_saveText)
    private TextView saveText;
    @ViewInject(R.id.modifyUser_userNameInput)
    private EditText userNameInput;
    @ViewInject(R.id.modifyUser_pic)
    private ImageView userPic;
    @ViewInject(R.id.modifyUser_progress)
    private ProgressBar progress;
    @ViewInject(R.id.title_titleLayout)
    private RelativeLayout titleLayout;

    private int userPicWidth = 0;
    private long start = 0;
    private String picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case Passageway.IMAGE_REQUEST_CODE:
                    if (data != null) {
                        resizeImage(data.getData());
                    }
                    break;
                case Passageway.CAMERA_REQUEST_CODE:
                    if (isSdcardExisting()) {
                        resizeImage(getImageUri());
                    } else {
                        MessageHandler.showToast(context, "找不到SD卡");
                    }
                    break;
                case Passageway.RESULT_REQUEST_CODE:
                    if (data != null) {
                        getResizeImage(data);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_back, R.id.modifyUser_picBox, R.id.title_saveText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.modifyUser_picBox:
                showPicList();
                break;
            case R.id.title_saveText:
                saveUserAvatar();
                break;
        }
    }


    private void initActivity() {
        TitleHandler.setTitle(context, titleLayout);
        backIcon.setVisibility(View.VISIBLE);
        saveText.setVisibility(View.VISIBLE);
        titleName.setText(TextHandeler.getText(context, R.string.compile_myself));
        userTel.setText(UserObjHandler.getUserTel(context));
        userNameInput.setText(UserObjHandler.getNickName(context));
        userPicWidth = WinTool.getWinWidth(context) / 10;
        setUserAvatar();
    }

    private void setUserAvatar() {
        userPic.setLayoutParams(new RelativeLayout.LayoutParams(userPicWidth, userPicWidth));
        UserObjHandler.setUserAvatar(context, userPic, userPicWidth / 2);
    }

    private void showPicList() {
        start = DateHandle.getTime();
        final List<String> msgList = getMsgList();
        final ListDialog dialog = new ListDialog(context);
        dialog.setTitleGone();
        dialog.setList(msgList);
        dialog.setItemListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (msgList.get(position).equals("拍照")) {
                    takePhoto();
                } else {
                    selectImage();
                }
                dialog.dismiss();
            }

        });
    }

    public List<String> getMsgList() {
        List<String> list = new ArrayList<String>();
        list.add("拍照");
        list.add("本地相册");
        return list;
    }

    private void takePhoto() {
        picPath = Passageway.takePhoto(context);
    }

    private void selectImage() {
        Passageway.selectImage(context);
    }

    private Uri getImageUri() {
        return Uri.fromFile(new File(DownloadImageLoader.getImagePath(),
                picPath));
    }

    public void resizeImage(Uri uri) {
        Passageway.resizeImage(context, uri);
    }

    private boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private File getImageFile() {
        String name = "head_" + start + ".jpg";
        return new File(DownloadImageLoader.getImagePath(), name);
    }

    private void getResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            FileOutputStream foutput = null;
            try {
                foutput = new FileOutputStream(getImageFile());
                photo.compress(Bitmap.CompressFormat.JPEG, 100, foutput);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (null != foutput) {
                    try {
                        foutput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            DownloadImageLoader.loadImageForFile(userPic, getImageFile()
                    .toString(), userPicWidth / 2);
        }
    }

    private void saveUserAvatar() {
        progress.setVisibility(View.VISIBLE);
        File f = getImageFile();
        if (f.exists()) {
            AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
//                        UserObjHandler.saveUserAvatar(context, file.getUrl());
                        uploadMessage(file.getObjectId());
                    }else{
                        e.printStackTrace();
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }
                }
            });
        } else {
            uploadMessage("");
        }

    }

    private void uploadMessage(String s) {
        String url = Url.getUserModify();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("username", TextHandeler.getText(userNameInput));
        if (!s.equals("")) {
            params.addBodyParameter("fileId", s);
        }

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            JSONObject resultsJson = JsonHandle.getJSON(json, "results");
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                UserObjHandler.saveUserObj(context, UserObjHandler.getUserObj(resultsJson), false);
                                finish();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            }
                        }
                    }

                });
    }

}
