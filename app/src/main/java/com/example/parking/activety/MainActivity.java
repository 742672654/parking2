package com.example.parking.activety;


import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.bean.JiguangBean;
import com.example.parking.bean.http.OrderAddBean;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.bean.http.PhotoToOssBean;
import com.example.parking.bean.http.Report_orderlistBean;
import com.example.parking.bean.http.SelectSubPlaceBean;
import com.example.parking.db.BaseSQL_DB;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.fragment.ParkingBase;
import com.example.parking.http.HttpCallBack2;
import com.example.parking.http.HttpManager2;
import com.example.parking.jiguang.ExampleUtil;
import com.example.parking.jiguang.JiGuangAPI;
import com.example.parking.jiguang.LocalBroadcastManager;
import com.example.parking.util.FileUtil;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;


public class MainActivity extends MainBaseActivity implements HttpCallBack2 {


    private static final String TAG = "MainActivity<主>";

    public static final int parkingPhoto = 101; //停车拍照<全景>
    public static final int parkingPhoto2 = 1001; //停车拍照<车牌>
    public static final int orderPhoto = 102; //订单拍照
    public static final int updateTieleParking = 103;//修改车位数量
    public static final int alertPhoto = 104; //警告拍照
    public static final int JiguangPush = 1000001;//极光推送

    private Uri imageUri; //照片的中的URi
    public BaseSQL_DB baseSQL_DB = null;

    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.activity = this;
        super.onCreate(savedInstanceState);

        baseSQL_DB = new BaseSQL_DB(this, "parking.db", null, 6);

        //初始化推送
        registerMessageReceiver();  // used for receive msg

        //登录
        JPushInterface.init(this);

        new Thread( new Runnable(){
            @Override
            public void run() {

                //等待2秒再开启摄像头
                try {  Thread.sleep(5000);  } catch (InterruptedException e) { Log.w(TAG,e); }

                //设置极光别名
                new JiGuangAPI().setAlias(activity,userBean.getParkid().replaceAll("-",""));
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            Log.i(TAG, "收到Handler"+"joinType:" + msg.getData().get("joinType")+";msg.what="+msg.what);

            switch (msg.what) {

                //TODO 停车拍照<全景>
                case MainActivity.parkingPhoto:

                        //获取安卓Uri
                        imageUri = getOutputMediaFileUri();
                        //发起拍照
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //Android7.0添加临时权限标记
                        startActivityForResult( openCameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION), MainActivity.parkingPhoto);
                    break;

                //TODO 停车拍照<车牌>
                case MainActivity.parkingPhoto2:

                    //获取安卓Uri
                    imageUri = getOutputMediaFileUri();
                    //发起拍照
                    Intent openCameraIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    openCameraIntent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    //Android7.0添加临时权限标记
                    startActivityForResult( openCameraIntent2.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION), MainActivity.parkingPhoto2);
                    break;


                //TODO 订单拍照
                case MainActivity.orderPhoto:

                    //获取安卓Uri
                    imageUri = getOutputMediaFileUri();
                    //发起拍照
                    Intent openCameraIntent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    openCameraIntent3.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    //Android7.0添加临时权限标记
                    startActivityForResult( openCameraIntent3.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION), MainActivity.orderPhoto);
                    break;


                //TODO 订单拍照
                case MainActivity.alertPhoto:

                    //获取安卓Uri
                    imageUri = getOutputMediaFileUri();
                    //发起拍照
                    Intent openCameraIntent4 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    openCameraIntent4.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    //Android7.0添加临时权限标记
                    startActivityForResult( openCameraIntent4.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION), MainActivity.alertPhoto);
                    break;


                //TODO 修改title车位数量
                case MainActivity.updateTieleParking:
                    parking_surplus.setText(String.valueOf(msg.getData().getInt("surplus")));
                    parking_already.setText(String.valueOf(msg.getData().getInt("already")));
                    break;

                //TODO 极光推送
                case MainActivity.JiguangPush:

                    JiguangBean jiguangBean = (JiguangBean)msg.obj;
                    switch (jiguangBean.getMsgType()){
                        case "move":
                            mediaPlayerMP3(jiguangBean.getInOut());
                            break;
                        case "alert":
                            mediaPlayerMP3("alert");
                            break;
                        case "finish":
                            mediaPlayerMP3("finish");
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //TODO 打开停车保存页面
    public void openParking(final SelectSubPlaceBean.SelectSubPlaceData selectSubPlaceDate){

        if ( FragmentStartTAG.equals(parkingFragment.TAG) || FragmentStartTAG.equals(ParkingBase.TAG)){
            openWhite();
        }

       MainActivity.this.runOnUiThread(new Runnable() {
           @Override
           public void run() {


               FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.activity_fragment, parkingFragment);

               Bundle bundle = new Bundle();
               bundle.putSerializable("selectSubPlaceDate", selectSubPlaceDate);
               bundle.putString("joinType", TAG);
               parkingFragment.setArguments(bundle);
               fragmentTransaction.addToBackStack(null);
               fragmentTransaction.commit();

               //清空停车位
               parkingFragment.sss = true;
           }
       });
   }

    //TODO 打开首页
    public void openParkingIndex(){

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_fragment, parkingIndexFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    //TODO 打开订单列表
    public void openOrderList(){

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_fragment,orderListFragment );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    //TODO 打开收费页面
    public void openOrder_details(final OrderlistBean.OrderlistData orderlistData ){

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_fragment, order_detailsFragment);
                fragmentTransaction.addToBackStack(null);

                Bundle bundle = new Bundle();
                bundle.putSerializable("orderlistData",orderlistData);

                order_detailsFragment.setArguments(bundle);

                fragmentTransaction.commit();
            }
        });
    }

    //TODO 打开订单列表页面
    public void openOrder(){

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_fragment, orderFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    //TODO 打开补缴费用页面
    public void openOrderPayBack(final OrderAddBean.OrderAddDate orderAddDate ){

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_fragment, orderPayBackFragment);
                fragmentTransaction.addToBackStack(null);

                Bundle bundle = new Bundle();
                bundle.putSerializable("orderAddDate",orderAddDate);
                orderPayBackFragment.setArguments(bundle);
                fragmentTransaction.commit();
            }
        });
    }

    //TODO 打开订单列表详情页面
    public void openOrder_list_details(final Report_orderlistBean.Report_orderlistList article){

        if ( FragmentStartTAG.equals(order_list_detailsFragment.TAG)){ openWhite(); }

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_fragment, order_list_detailsFragment);
                fragmentTransaction.addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("report_orderlistList",article);
                order_list_detailsFragment.setArguments(bundle);
                fragmentTransaction.commit();
            }
        });
    }

    //TODO 打开警告拍照页面
    public void openAlert(final JiguangBean jiguangBean){

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_fragment, alertFeagment);
                fragmentTransaction.addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("jiguangBean",jiguangBean);
                alertFeagment.setArguments(bundle);
                fragmentTransaction.commit();
            }
        });
    }

    //TODO 打开空白页面
    public void openWhite(){

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_fragment, whiteFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG,resultCode == -1 ?FileUtil.getFile_Suffix(imageUri)+"----"+ FileUtil.getFile_Byte(imageUri).length
                +"系统相机拍照完成，requestCode="+requestCode+"; resultCode="+resultCode+"; File_Path="+FileUtil.getFile_Path(imageUri):"拍照取消");

        //TODO 上传全景
        if ( requestCode == MainActivity.parkingPhoto && resultCode == -1 ){
            Map<String,String> params = new HashMap<String,String>(5) ;
            params.put("token", userBean.getToken());
            params.put("type", "2");
            params.put("panoramaPath",FileUtil.getFile_Path(imageUri));
            HttpManager2.onResponseFile(Static_bean.photoToOss(), params,
                    "file",
                     FileUtil.getFile_Suffix(imageUri),
                     FileUtil.getFile_Byte(imageUri),
                    this,
                    "photoToOssNO");
            Log.i(TAG,"上传停车页面拍摄的全景照片");
            toast_makeText("照片正在上传...请稍等");
            return;
        }

        //TODO 上传车牌特写
        if ( requestCode == MainActivity.parkingPhoto2 && resultCode == -1 ){
            Map<String,String> params = new HashMap<String,String>(5) ;
            params.put("token", userBean.getToken());
            params.put("type", "1");
            params.put("inimagePath",FileUtil.getFile_Path(imageUri));
            HttpManager2.onResponseFile(Static_bean.photoToOss(), params,
                    "file",
                     FileUtil.getFile_Suffix(imageUri),
                     FileUtil.getFile_Byte(imageUri),
                    this,
                    "photoToOss");
            toast_makeText("照片正在上传...请稍等");
            return;
        }

        //TODO 订单拍照
        if ( requestCode == MainActivity.orderPhoto && resultCode == -1 ){
            Map<String,String> params = new HashMap<String,String>(2) ;
            params.put("token", userBean.getToken());
            params.put("type", "1");
            HttpManager2.onResponseFile(Static_bean.photoToOss(), params,
                    "file",
                    FileUtil.getFile_Suffix(imageUri),
                    FileUtil.getFile_Byte(imageUri),
                    this,
                    "orderPhoto");
            toast_makeText("照片正在上传...请稍等");
            return;
        }

        //TODO 警告拍照
        if ( requestCode == MainActivity.alertPhoto && resultCode == -1 ){

            alertFeagment.alert_imageview.setImageBitmap(
                    BitmapFactory.decodeFile( alertFeagment.inimageImageString = FileUtil.getFile_Path(imageUri) ));

            Jiguang_DB.updata_Jinggao(baseSQL_DB,alertFeagment.jiguangBean.getnOTIFICATION_ID(),alertFeagment.inimageImageString);

            Log.i(TAG," 警告拍照"+alertFeagment.inimageImageString);

            Map<String,String> params = new HashMap<String,String>(3) ;
            params.put("subId", alertFeagment.jiguangBean.getDevDock());
            params.put("nOTIFICATION_ID", alertFeagment.jiguangBean.getnOTIFICATION_ID());

            HttpManager2.onResponseFile(Static_bean.photoToAlert(), params,
                    "file",
                    FileUtil.getFile_Suffix(imageUri),
                    FileUtil.getFile_Byte(imageUri),
                    this,
                    "photoToAlert");
            toast_makeText("照片正在上传...请稍等");
            return;
        }
    }

    @Override
    public void onResponseFile(String url, Map<String, String> param, String sign, String object) {
        super.onResponseFile( url,  param,  sign,  object);

        Log.i( TAG,"url="+url+";  param="+param+"; \r\n object="+object );
        try {
            switch (sign) {

                case "photoToOssNO":
                    parkingFragment.parking_quanjin(param, object);
                    break;

                case "photoToOss":
                    parkingFragment.parking_carmun(param, object);
                    break;

                case "orderPhoto":
                    orderFragment.parking_carmun(object);
                    break;

                case "photoToAlert":

                    //PhotoToOssBean photoToOssBean = new Gson().fromJson(URLDecoder.decode(object, "UTF-8"), PhotoToOssBean.class);

                    break;
                default: break;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    @Override
    protected void onResume() { isForeground = true;super.onResume(); }

    @Override
    protected void onPause() { isForeground = false;super.onPause(); }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    toast_makeText(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }


}
