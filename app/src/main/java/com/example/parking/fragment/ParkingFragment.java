package com.example.parking.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.OrderDbBean;
import com.example.parking.bean.PrintBillBean;
import com.example.parking.bean.http.FirstPageRecordBean;
import com.example.parking.bean.http.HttpBean;
import com.example.parking.bean.http.OrderAddBean;
import com.example.parking.bean.http.PhotoToOssBean;
import com.example.parking.db.Order_DB;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.FileUtil;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.TimeUtil;
import com.google.gson.Gson;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * 功能说明: 停车Fragment
 * 日期:	2019年5月29日
 * 开发者:	KXD
 */
public class ParkingFragment extends ParkingBase {



    public static final String TAG = "ParkingFragment";

    public String inimageURL = null; //车牌远程地址
    public String panoramaURL = null; //全景远程地址
    public String inimagePath = null; //车牌本地地址
    public String panoramaPath = null; // 全景本地地址

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView( inflater, container, savedInstanceState );
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);

        if (selectSubPlaceDate!=null){
            parking_carmun.setText(selectSubPlaceDate.getCarnum()==null?"":selectSubPlaceDate.getCarnum());
            //parking_pre_price.setText(selectSubPlaceDate.getPreprice()==null?"":selectSubPlaceDate.getPreprice().toString());
        }
        Log.i(TAG,getArguments().getString("joinType")+"---------------------");
        if ( getArguments().getString("joinType")!=null){
            inimageURL = "";
            panoramaURL = "";
            inimagePath = "";
            panoramaPath = "";
            distinguish_carmun = null;
            // 剩余车位和预约车位
            Map<String,String> param = new HashMap<String,String>(1);
            param.put("token",activity.userBean.getToken());
            HttpManager2.requestPost(Static_bean.firstPageRecord(),  param, this, "firstPageRecord");
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {

            case R.id.parking_photo:
                Bundle bundle = new Bundle();
                bundle.putString( "joinType",TAG );
                bundle.putString("text","停车拍照<全景>");
                Message message = new Message();
                message.what = MainActivity.parkingPhoto;
                activity.handler.sendMessage(message);
                break;

            case R.id.parking_photo3:
                Bundle bundle2 = new Bundle();
                bundle2.putString( "joinType",TAG );
                bundle2.putString("text","停车拍照<车牌>");
                Message message2 = new Message();
                message2.what = MainActivity.parkingPhoto2;
                activity.handler.sendMessage(message2);
                break;

            case R.id.button_order_add:
                Map<String,String> params = new HashMap<String,String>() ;
                params.put("token", activity.userBean.getToken());
                params.put("carnum", parking_carmun.getText().toString());
                params.put("preprice", parking_pre_price.getText().toString());
                params.put("ordertype", String.valueOf(1));
                params.put("inimage", inimageURL);
                params.put("panorama", panoramaURL);
                params.put("subid", selectSubPlaceDate.getId());
                params.put("subname", selectSubPlaceDate.getCode());
                HttpManager2.requestPost(Static_bean.orderAdd(), params, this,"orderAdd");
                break;
            default:
                break;
        }
    }



    //TODO 全景拍照
    public void parking_quanjin(final Map<String, String> param, final String obj ) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    final PhotoToOssBean photoToOssBean = new Gson().fromJson(URLDecoder.decode(obj, "UTF-8"), PhotoToOssBean.class);
                    Log.i(TAG, obj+"----"+URLDecoder.decode(obj, "UTF-8") + "---------" + photoToOssBean.toString());

                    panoramaURL = photoToOssBean.getImgurl();
                    panoramaPath = param.get("panoramaPath");

                    inimagePath = param.get("inimagePath");
                    inimageURL = param.get("inimageURL");
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            }
        });
    }


    //TODO 车牌拍照
    public void parking_carmun(final Map<String, String> param, final String obj ) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    final PhotoToOssBean photoToOssBean = new Gson().fromJson(URLDecoder.decode(obj, "UTF-8"), PhotoToOssBean.class);
                    Log.i(TAG, obj+"----"+URLDecoder.decode(obj, "UTF-8") + "---------" + photoToOssBean.toString());

                    inimagePath = param.get("inimagePath");
                    panoramaURL = param.get("panoramaURL");
                    panoramaPath = param.get("panoramaPath");
                    inimageURL = photoToOssBean.getImgurl();

                    if (photoToOssBean.getCarmun()==null || "".equals(photoToOssBean.getCarmun())){
                        toast_makeText("车牌识别错误，请手动输入车牌");
                        parking_carmun.setText("车牌识别错误");
                        distinguish_carmun = null;
                        return;
                    }

                    parking_carmun.setText(photoToOssBean.getCarmun());
                    distinguish_carmun = photoToOssBean.getCarmun();
                } catch (Exception e) {
                    Log.w(TAG, e);
                    toast_makeText("车牌识别错误，请手动输入车牌");
                }
            }
        });

    }



    //TODO >>>>>接收上传订单
    public void orderAdd(final String obj) {

        try {

            OrderAddBean httpBean = JsonUtil2.fromJson(URLDecoder.decode(obj, "UTF-8"), OrderAddBean.class);

            if ("SUCCESS".equals(httpBean.getMessage())){

                Log.i(TAG,"打开ParkingFragment");
                toast_makeText("订单上传成功");

                StringBuffer buf = new StringBuffer("\r\n\r\n---------------------------\r\n");
                buf.append("停车场："+activity.userBean.getParkname()+"   \r\n\r\n");
                buf.append("车位号："+button_choice_parkingspace.getText().toString()+" \r\n\r\n");
                buf.append("车牌号："+parking_carmun.getText().toString()+" \r\n\r\n");
                buf.append("驶入时间"+ httpBean.getData().getParkTime()+" \r\n");
                buf.append("预交金额："+parking_pre_price.getText().toString()+".0元\r\n\r\n");

                buf.append("每天单次收费5元，晚上12点后重新收费。车辆离开车位后，视为停车订单结算完成。\r\n\r\n");
                buf.append("收费单位：泉州市畅顺停车管理有限公司\r\n\r\n");
                buf.append("监督电话：0595-28282818");


                StringBuffer qRcode = new StringBuffer(Static_bean.QRcode_redict());
                qRcode.append("?orderid=").append(httpBean.getData().getId());
                qRcode.append("&pointid=").append(activity.userBean.getParkid());


                PrintBillBean PrintBillBean = new PrintBillBean(1,buf.toString(),qRcode.toString());
                printer_marking(PrintBillBean);

                //保存到数据库
                OrderDbBean orderDbBean = new OrderDbBean();
                orderDbBean.setId(String.valueOf(httpBean.getData().getId()));
                orderDbBean.setPhoto1_path(panoramaPath);
                orderDbBean.setPhoto1_url(panoramaURL);
                orderDbBean.setPhoto2_path(inimagePath);
                orderDbBean.setPhoto2_url(inimageURL);
                Order_DB.insert_in_Order(activity.baseSQL_DB,orderDbBean);

                activity.openParkingIndex();

            }else if ("order".equals(httpBean.getMessage())){

                AlertDialog_Builder("提示","已有订单");
            }else if ("escape".equals(httpBean.getMessage())){

                activity.openOrderPayBack(httpBean.getData());
            }else{

                AlertDialog_Builder("上传失败",httpBean.getMessage());
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    //TODO >>>>>剩余车位/预约车位
    public void firstPageRecord(final String obj) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    final FirstPageRecordBean firstPageRecordBean = JsonUtil2.fromJson(URLDecoder.decode(obj, "UTF-8"), FirstPageRecordBean.class);

                    Log.i(TAG, "剩余车位/预约车位=" + firstPageRecordBean.toString());

                    nouse.setText( String.valueOf(firstPageRecordBean.getData().getNouse()) );
                    wxnum.setText( String.valueOf(firstPageRecordBean.getData().getWxnum()) );
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            }
        });

    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        super.onResponsePOST(url,  param,  sign,  object);
        Log.i(TAG,"url="+url+";param="+param+";sign="+sign+";\r\n object="+object);

        switch (sign){
            case "orderAdd": orderAdd(object);
            break;
            case "firstPageRecord": firstPageRecord(object);
                break;

            default:break;
        }

    }

}