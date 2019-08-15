package com.example.parking.activety;



import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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

    public String imageUriString; //照片的中的URi

    public static String ActivityStartTAG = "";

    private static final String TAG = "BaseActivity";

    public static Activity activity;


    //TODO 所在地址
    public void onPosition(String TAG) {
        ActivityStartTAG = TAG;
    }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();    StrictMode.setVmPolicy(builder.build());}
//        File mediaFile = null;
//        try {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//                StrictMode.setVmPolicy(builder.build());
//            }
//
           File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
//
//            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) { return null; }

//            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "parkings/" + StringUtil.getUuid() + ".jpg");
//            mediaFile.getAbsolutePath();

//            //TODO sdk >= 24  android7.0以上，报FileUriExposedException
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Uri contentUri = FileProvider.getUriForFile(this,
//                        this.getApplicationContext().getPackageName() + ".provider",//与清单文件中android:authorities的值保持一致
//                        mediaFile);//FileProvider方式或者ContentProvider。也可使用VmPolicy方式
//                return contentUri;
//            } else {
//                return Uri.fromFile(mediaFile);//或者 Uri.isPaise("file://"+file.toString()
//            }


String filePath = mediaStorageDir.getPath() + File.separator + "parkings/" + StringUtil.getUuid() + ".jpg";

        imageUriString = filePath;


Log.i(TAG,"--------------"+filePath);
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName()+".fileprovider", new File(filePath));
            } else {
                uri = Uri.fromFile(new File(filePath));
            }
            return uri;
//
//
//        } catch (Exception e) {
//            Log.w(TAG, e);
//            return null;
//        }
    }

    //TODO 1是进，2是出，3是警告
    MediaPlayer mMediaPlayer = null;
    protected void mediaPlayerMP3(String type){

        if (mMediaPlayer!=null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }

        switch ( type ){

            case "in":mMediaPlayer = MediaPlayer.create(this, R.raw.in);
                break;
            case "out":mMediaPlayer = MediaPlayer.create(this, R.raw.out);
                break;
            case "alert":mMediaPlayer = MediaPlayer.create(this, R.raw.alert);
                break;
            case "finish":mMediaPlayer = MediaPlayer.create(this, R.raw.tiqianjiaofei);
                break;
            default:
                return;
        }

        mMediaPlayer.start();

//        start();//开始播放
//        pause();//暂停播放
//        reset()//清空MediaPlayer中的数据
//        setLooping(boolean);//设置是否循环播放
//        seekTo(msec)//定位到音频数据的位置，单位毫秒
//        stop();//停止播放
//        relase();//释放资源
    }


    @Override
    public void onResponseGET(String url, Map<String, String> param, String sign, String object) { }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) { }

    @Override
    public void onResponseFile(String url, Map<String, String> param, String sign, String object) { }
}
