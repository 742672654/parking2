package com.example.parking.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.http.HttpBean;
import com.example.parking.bean.http.PhotoToOssBean;
import com.example.parking.bean.http.SelectSubPlaceBean;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.JsonUtil2;
import com.google.gson.Gson;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * 功能说明: 订单Fragment
 * 日期:	2019年5月29日
 * 开发者:	KXD
 */
public class OrderFragment extends OrderBase {



    public static final String TAG = "OrderFragment<订单>";
    public String outimage = "";


    @Override
    public void onStart() {
        super.onStart();
        onPosition(TAG);

        outimage = "";

        Map<String,String> param = new HashMap<String,String>(1);
        param.put("token",activity.userBean.getToken());
        HttpManager2.requestPost(Static_bean.selectSubPlace,  param, this, "selectSubPlace");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.allCharge:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("一键出场");
                        builder.setMessage("所有清单全部出场?");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Map<String,String> param = new HashMap<String,String>(1);
                                param.put("token",activity.userBean.getToken());
                                HttpManager2.requestPost(Static_bean.allCharge,  param, OrderFragment.this, "allCharge");
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                        builder.show();
                    }
                });
                break;

            case R.id.btn_free:orderlist();
                break;

            case R.id.order_photo:
                Bundle bundle = new Bundle();
                bundle.putString( "joinType",TAG );
                bundle.putString("text","订单拍照");
                Message message2 = new Message();
                message2.what = MainActivity.orderPhoto;
                Handler handler2 = activity.handler;
                handler2.sendMessage(message2);
                break;

            default:break;
        }
    }

    //TODO >>>>>出场接收
    public void parking_carmun(final String obj) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    final PhotoToOssBean photoToOssBean = new Gson().fromJson(URLDecoder.decode(obj, "UTF-8"), PhotoToOssBean.class);
                    Log.i(TAG, obj+"----"+URLDecoder.decode(obj, "UTF-8") + "---------" + photoToOssBean.toString());

                    if (photoToOssBean.getCarmun()==null || "".equals(photoToOssBean.getCarmun())){

                        toast_makeText("车牌识别错误，请手动输入车牌");
                        outimage = photoToOssBean.getImgurl();
                        carNun.setText("车牌识别错误");
                        return;
                    }

                    outimage = photoToOssBean.getImgurl();
                    carNun.setText(photoToOssBean.getCarmun());

                } catch (Exception e) {
                    Log.w(TAG, e);
                    toast_makeText("车牌识别错误，请手动输入车牌");
                }
            }
        });
    }

    //TODO >>>>>一键出场接收
    private void allCharge(final String obj) {


        try {

            final HttpBean httpBean = new Gson().fromJson(URLDecoder.decode(obj, "UTF-8"), HttpBean.class);
            Log.i(TAG, obj + "----" + URLDecoder.decode(obj, "UTF-8") + "---------" + httpBean.toString());

            if (httpBean.getCode() == 200) {
                //刷新list
                orderlist();
                //刷新title
                Map<String, String> param = new HashMap<String, String>();
                param.put("token", activity.userBean.getToken());
                HttpManager2.requestPost(Static_bean.selectSubPlace, param, this, "selectSubPlace");
            } else {
                toast_makeText(httpBean.getMessage());
            }

        } catch (Exception e) {
            Log.w(TAG, e);
            toast_makeText("一键出场错误");
        }

    }

    //TODO >>>>>修改title车位数量
    private void selectSubPlace( String object ) {
        try {


            SelectSubPlaceBean selectSubPlace = JsonUtil2.fromJson(object, SelectSubPlaceBean.class);

            if (selectSubPlace == null || selectSubPlace.getData() == null || selectSubPlace.getCode() != 200)
                return;
            Log.i(TAG, selectSubPlace.toString());

            //修改title的车位数量
            int surplus = 0, already = 0;
            for (int i = 0; i < selectSubPlace.getData().size(); i++) {

                if (selectSubPlace.getData().get(i).getHavecar() == 2) {
                    already++;
                } else {
                    surplus++;
                }
            }
            Bundle bundle = new Bundle();
            bundle.putString("joinType", TAG);
            bundle.putInt("surplus", surplus);
            bundle.putInt("already", already);
            Message message = new Message();
            message.what = MainActivity.updateTieleParking;
            message.setData(bundle);
            Handler handler = activity.handler;
            handler.sendMessage(message);

        }catch (Exception e){
            Log.w(TAG,e);
        }

    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        super.onResponsePOST( url,  param,  sign,  object);
        Log.i(TAG, "url="+url + "----param="+param +";sign="+sign + ";object="+object+"'****"+param);

        switch (sign) {
            case "allCharge":allCharge(object);
                break;

            case "selectSubPlace":selectSubPlace(object);
                break;
            default:
                break;
        }
    }



}