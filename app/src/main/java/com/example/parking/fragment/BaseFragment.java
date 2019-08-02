package com.example.parking.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.parking.Static_bean;
import com.example.parking.activety.LoginActivity;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.PrintBillBean;
import com.example.parking.bean.http.HttpBean;
import com.example.parking.http.HttpCallBack2;
import com.example.parking.http.HttpManager2;
import com.example.parking.printer.PrintBillService;
import com.example.parking.util.JsonUtil2;
import java.util.HashMap;
import java.util.Map;



public class BaseFragment extends Fragment implements View.OnClickListener, HttpCallBack2 {

    private static final String TAG = "BaseFragment<Base>";
    public MainActivity activity;
    private MediaPlayer mMediaPlayer = null;

    protected static String[] string_pre_price = null;


    @Override
    public void onStart() {
        super.onStart();

        try {
            // 在activity结束的时候回收资源
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }

        Map<String,String> param = new HashMap<String,String>(1);
        param.put("token",activity.userBean.getToken());
        HttpManager2.requestPost(Static_bean.checktoken,  param, this, "checktoken");
    }

    @Override
    public void onAttach(Activity activity) {

        if (string_pre_price==null){
            string_pre_price = new String[101];
            for (int s=0;s<=100;s++){

                string_pre_price[s]=String.valueOf(s);
            }
        }

        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }


    public void onPosition(String TAG) { MainActivity.FragmentStartTAG = TAG; }


    //TODO 判断是否是主线程
    protected boolean isOnMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }


    //TODO 提示
    protected void toast_makeText(final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(activity,text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //TODO 播放音乐
    protected void playMusic(){  }


    //TODO 打印提示
    protected void AlertDialog_Builder(final String title, final String text ) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(title);
                builder.setMessage(text);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }


    //TODO 打印机
    protected void printer_marking(PrintBillBean PrintBillBean){

        if ( PrintBillBean==null ) return;

        Intent intentService = new Intent(getActivity(), PrintBillService.class);
        intentService.putExtra("SPRT", PrintBillBean);
        intentService.putExtra("type", PrintBillBean.getType());
        getActivity().startService(intentService);
    }

    /**
     * 让屏幕变暗
     */
    protected void makeWindowDark(){
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

    /**
     * 让屏幕变亮
     */
    protected void makeWindowLight(){
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1f;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) { }


    @Override
    public void onResponseGET(String url, Map<String, String> param, String sign, String object) { }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        Log.i(TAG, "url="+url + "----param="+param +";sign="+sign + ";object="+object+"'****"+param);

        switch (sign) {
            case "checktoken": {
                try {
                    HttpBean httpBean = JsonUtil2.fromJson(object, HttpBean.class);
                    if (httpBean.getCode() == 200) {
                        return;
                    }
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.putExtra("joinType", TAG);
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            Log.w(TAG, e);
                        }
                    }
                });
            };
            break;
        }
    }

    @Override
    public void onResponseFile(String url, Map<String, String> param, String sign, String object) { }

    @Override
    public void onStop() {
        super.onStop();

        try {
            // 在activity结束的时候回收资源
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

}

