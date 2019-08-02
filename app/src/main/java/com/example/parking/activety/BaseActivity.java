package com.example.parking.activety;



import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.parking.R;
import com.example.parking.fragment.MyFragment;
import com.example.parking.fragment.OrderFragment;
import com.example.parking.fragment.ParkingFragment;
import com.example.parking.http.HttpCallBack2;
import com.example.parking.util.FileUtil;
import com.example.parking.util.StringUtil;

import java.io.File;
import java.util.Map;



public class BaseActivity extends AppCompatActivity implements HttpCallBack2 {



    private static final String TAG = "BaseActivity";

    protected static Activity activity;


    //TODO 提示
    protected void toast_makeText(final String text) {

        Log.i(TAG,text);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO 动态权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case 1: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("动态权限","权限成功："+ permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.e("动态权限","权限失败："+ permissions[i]);
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //TODO 动态获取Url
    protected Uri getOutputMediaFileUri() {

        File mediaFile = null;
        try {

            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) { return null; }

            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "parking/" + StringUtil.getUuid() + ".jpg");
            mediaFile.getAbsolutePath();

            //TODO sdk >= 24  android7.0以上，报FileUriExposedException
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(this,
                        this.getApplicationContext().getPackageName() + ".provider",//与清单文件中android:authorities的值保持一致
                        mediaFile);//FileProvider方式或者ContentProvider。也可使用VmPolicy方式
                return contentUri;
            } else {
                return Uri.fromFile(mediaFile);//或者 Uri.isPaise("file://"+file.toString()
            }

        } catch (Exception e) {
            Log.w(TAG, e);
            return null;
        }
    }

    @Override
    public void onResponseGET(String url, Map<String, String> param, String sign, String object) { }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) { }

    @Override
    public void onResponseFile(String url, Map<String, String> param, String sign, String object) { }
}
