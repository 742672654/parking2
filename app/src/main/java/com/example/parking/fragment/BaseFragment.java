package com.example.parking.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.parking.R;
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

    protected static String[] string_pre_price = new String[]{"0元","1元","3元","5元","10元"};

    @Override
    public void onStart() {
        super.onStart();

        try {
            if (dialog != null) { dialog.dismiss(); }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
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
        HttpManager2.requestPost(Static_bean.checktoken(),  param, this, "checktoken");
    }

    @Override
    public void onAttach(Activity activity) {

        if (string_pre_price==null){ string_pre_price = new String[]{"3","5","10"}; }
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    public void onPosition(String TAG) { MainActivity.FragmentStartTAG = TAG; }

    //TODO 判断是否是主线程
    protected boolean isOnMainThread() { return Thread.currentThread() == Looper.getMainLooper().getThread(); }

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

    protected static AlertDialog dialog ;
    protected void AlertDialog_Builder(final String title,final String Message,final String text ) {


        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    if (dialog != null) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Log.w(TAG, e);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(title);
                builder.setMessage(Message);
                builder.setPositiveButton(text, null);
                dialog = builder.show();
            }
        });
    }

    //TODO 打印提示
    protected void AlertDialog_Builder(final String title, final String Message ) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    if (dialog != null) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Log.w(TAG, e);
                }


                AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
                builder.setTitle(title);
                builder.setMessage(Message);
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
                  dialog =  builder.show();
            }
        });
    }

    //TODO 打印机
    protected void printer_marking(PrintBillBean PrintBillBean){

        try {


            if (PrintBillBean == null) return;

            Intent intentService = new Intent(getActivity(), PrintBillService.class);
            intentService.putExtra("SPRT", PrintBillBean);
            intentService.putExtra("type", PrintBillBean.getType());
            getActivity().startService(intentService);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    //TODO 让屏幕变暗
    protected void makeWindowDark(){
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

     //TODO 让屏幕变亮
    protected void makeWindowLight(){
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1f;
        window.setAttributes(lp);
    }

    //TODO 放大展示照片
    private PopupWindow popupWindow;
    private ImageView popuwindow_ImageView;
    protected void showPopupWindow( View parent, String path){

        if (popupWindow == null) {
            //获取自定义的菜单布局文件
            View inflate = getLayoutInflater().inflate(R.layout.popuwindow_photo_layout, null, false);
            //创建popupwindow的实例
            popupWindow = new PopupWindow(inflate, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);

            popuwindow_ImageView = inflate.findViewById(R.id.popuwindow_ImageView);

            DisplayMetrics dm = new DisplayMetrics();
            //获取屏幕信息
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            popupWindow.setWidth(dm.widthPixels);
            popupWindow.setHeight((int) (dm.heightPixels * 0.8));

            //单机其他地方消失
            inflate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent notionEvent) {
                    //如果菜单存在并且为显示状态，就关闭菜单，并且初始化菜单
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    return false;
                }
            });
            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        //设置PopupWindow显示在按钮的下面
        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 150);

        Log.i(TAG,path+"**************");
        popuwindow_ImageView.setImageBitmap( BitmapFactory.decodeFile(path));
    }

    @Override
    public void onClick(View v) { }

    @Override
    public void onResponseGET(String url, Map<String, String> param, String sign, String object) { }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        Log.i(TAG, "url="+url +";sign="+sign + ";\r\n object="+object);

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

