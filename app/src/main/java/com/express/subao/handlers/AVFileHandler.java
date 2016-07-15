package com.express.subao.handlers;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.express.subao.download.DownloadImageLoader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
 * Created by Hua on 16/1/8.
 */
public class AVFileHandler {

    public static void uploadFile(File f, final AVSaveCallback callback) {
        try {
            Log.e("av file", f.getName() + "     " + f.toString());
            Log.e("av file", DownloadImageLoader.getImagePath() + "/" + f.getName());
            final AVFile file = AVFile.withAbsoluteLocalPath(f.getName(), f.toString());
//            final AVFile file = new AVFile(f.getName(), getFileByte(f));
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    callback.callnack(e, file);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] getFileByte(File f) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new byte[0];
    }

    public interface AVSaveCallback {
        void callnack(AVException e, AVFile file);
    }

}
