package com.example.parking.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.PrintBillBean;
import com.example.parking.bean.http.HttpBean;
import com.example.parking.bean.http.OrderAddBean;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.bean.http.PhotoToOssBean;
import com.example.parking.bean.http.SelectSubPlaceBean;
import com.example.parking.http.HttpManager2;
import com.example.parking.listView.OrderView;
import com.example.parking.util.InputUtil;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.StringUtil;
import com.example.parking.util.TimeUtil;
import com.google.gson.Gson;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressLint("LongLogTag")
public class OrderPayBackFragment extends BaseFragment {


    public static final String TAG = "OrderPayBackFragment<订单补缴>";

    private OrderAddBean.OrderAddDate orderAddDate;//逃单对象

    private TextView escapeprice_text,carnum_text,point_text,parkDateStr_text;
    private Button bujiao_button,dayin_qianfei,dayin_jiaofei;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_payback, container, false);

        escapeprice_text = rootView.findViewById(R.id.escapeprice_text);
        carnum_text = rootView.findViewById(R.id.carnum_text);
        point_text = rootView.findViewById(R.id.point_text);
        parkDateStr_text = rootView.findViewById(R.id.parkDateStr_text);

        bujiao_button = rootView.findViewById(R.id.bujiao_button);
        bujiao_button.setOnClickListener(this);

        dayin_qianfei = rootView.findViewById(R.id.dayin_qianfei);
        dayin_qianfei.setOnClickListener(this);


        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        onPosition(TAG);
        orderAddDate=null;

        try{
            orderAddDate = (OrderAddBean.OrderAddDate) getArguments().getSerializable("orderAddDate");

            escapeprice_text.setText(orderAddDate.getEscapeprice());
            carnum_text.setText(orderAddDate.getCarnum());
            point_text.setText(activity.userBean.getParkname());
            parkDateStr_text.setText(orderAddDate.getParkDateStr());
        }catch (Exception e){
            Log.w(TAG,e);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bujiao_button:

                Map<String,String> params = new HashMap<String,String>() ;
                params.put("token", activity.userBean.getToken());
                params.put("id", String.valueOf(orderAddDate.getId()));
                HttpManager2.requestPost(Static_bean.escapedelete(), params, this,"escapedelete");
                break;

            case R.id.dayin_qianfei:

                StringBuffer buf = new StringBuffer("\r\n\r\n---------------------------\r\n");
                buf.append("补交金额："+orderAddDate.getEscapeprice()+".0元\r\n\r\n");
                buf.append("车牌号："+orderAddDate.getCarnum()+" \r\n\r\n");
                buf.append("停车场："+activity.userBean.getParkname()+"   \r\n\r\n");
                buf.append("欠费时间"+ orderAddDate.getParkDateStr()+" \r\n");

                buf.append("您上次停车忘记缴费了哦，麻烦请您补缴上次停车费。\r\n\r\n");
                buf.append("收费单位：泉州市畅顺停车管理有限公司\r\n\r\n");
                buf.append("监督电话：0595-28282818");


                StringBuffer qRcode = new StringBuffer("http://wx.yilufa.net/pointpay/pointzgr/redict?orderid=");
                qRcode.append(orderAddDate.getOrderid());
                qRcode.append("&pointid=");
                qRcode.append(activity.userBean.getParkid());

                printer_marking(new PrintBillBean(3,buf.toString(),qRcode.toString()));
                break;

            default:break;
        }
    }

    private void escapedelete(final String object){

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                HttpBean httpBean = JsonUtil2.fromJson(object,HttpBean.class);

                if (httpBean.getCode()==200){

                    //返回上个页面
                   activity.onKeyDown(KeyEvent.KEYCODE_BACK, null);
                }else {
                    toast_makeText(httpBean.getMessage());
                }
            }
        });
    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        super.onResponsePOST( url,  param,  sign,  object);
        Log.i(TAG, "url="+url + "----param="+param +";sign="+sign + ";object="+object);

        switch (sign) {
            case "escapedelete":escapedelete(object);
                break;

            default:
                break;
        }
    }

}
