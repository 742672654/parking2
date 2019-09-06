package com.example.parking.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.OrderDbBean;
import com.example.parking.bean.PrintBillBean;
import com.example.parking.bean.http.HttpBean;
import com.example.parking.bean.http.OrderAddBean;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.bean.http.PhotoToOssBean;
import com.example.parking.bean.http.PointEscapeEscapePicBean;
import com.example.parking.bean.http.SelectSubPlaceBean;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.db.Order_DB;
import com.example.parking.http.HttpManager2;
import com.example.parking.listView.OrderView;
import com.example.parking.util.FileUtil;
import com.example.parking.util.InputUtil;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.StringUtil;
import com.example.parking.util.TimeUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressLint("LongLogTag")
public class OrderPayBackFragment extends BaseFragment {


    public static final String TAG = "OrderPayBackFragment<订单补缴>";

    private OrderAddBean.OrderAddDate orderAddDate;//逃单对象
    private TextView escapeprice_text,carnum_text,point_text,parkDateStr_text,parkdate_text;
    private Button bujiao_button,dayin_qianfei,dayin_jiaofei;
    private RelativeLayout parkDateStr_Relative_bujiao;
    //车辆入场照片
    protected ImageView order_detailsLeftImage,order_detailsRightImage;
    //车辆入场照片
    private String order_detailsLeftString = null,order_detailsRightString=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_payback, container, false);

        escapeprice_text = rootView.findViewById(R.id.escapeprice_text);
        carnum_text = rootView.findViewById(R.id.carnum_text);
        point_text = rootView.findViewById(R.id.point_text);
        parkDateStr_text = rootView.findViewById(R.id.parkDateStr_text);
        parkdate_text = rootView.findViewById(R.id.parkdate_text);

        bujiao_button = rootView.findViewById(R.id.bujiao_button);
        bujiao_button.setOnClickListener(this);

        dayin_qianfei = rootView.findViewById(R.id.dayin_qianfei);
        dayin_qianfei.setOnClickListener(this);


        order_detailsLeftImage = rootView.findViewById(R.id.order_detailsLeftImage);
        order_detailsLeftImage.setOnClickListener(this);

        order_detailsRightImage = rootView.findViewById(R.id.order_detailsRightImage);
        order_detailsRightImage.setOnClickListener(this);


        parkDateStr_Relative_bujiao = rootView.findViewById(R.id.parkDateStr_Relative_bujiao);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        onPosition(TAG);
        orderAddDate=null;

        try{
            orderAddDate = (OrderAddBean.OrderAddDate) getArguments().getSerializable("orderAddDate");

            //如果没有补缴，就隐藏提示
            if (orderAddDate.sfjiaofei==null || !orderAddDate.sfjiaofei){
                parkDateStr_Relative_bujiao.setVisibility( View.INVISIBLE );
            }

            parkdate_text.setText(StringUtil.is_valid(orderAddDate.getParkdate())?orderAddDate.getParkdate().substring(0,10)+" "+orderAddDate.getParkdate().substring(11,19):"");

            escapeprice_text.setText(orderAddDate.getEscapeprice());
            carnum_text.setText(orderAddDate.getCarnum());
            point_text.setText(activity.userBean.getParkname());
            parkDateStr_text.setText(orderAddDate.getParkDateStr());

            //从数据库里面查询订单的照片，如果没有就去请求接口下载
            OrderDbBean orderDbBean = Order_DB.query_Order(activity.baseSQL_DB,String.valueOf(orderAddDate.getOrderid()));
            if ( orderDbBean != null ){

                order_detailsLeftString = orderDbBean.getPhoto1_path();
                order_detailsRightString = orderDbBean.getPhoto2_path();
                order_detailsLeftImage.setImageBitmap( BitmapFactory.decodeFile(order_detailsLeftString));
                order_detailsRightImage.setImageBitmap( BitmapFactory.decodeFile(order_detailsRightString));
            }else{

                Map<String,String> params = new HashMap<String,String>(5) ;
                params.put("token", activity.userBean.getToken());
                params.put("id", String.valueOf(orderAddDate.getId()));
                params.put("orderid", String.valueOf(orderAddDate.getOrderid()));
                HttpManager2.requestPost(Static_bean.pointEscapeEscapePic(), params, this,"pointEscapeEscapePic");
            }

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
                buf.append("您上次停车忘记缴费了哦，麻烦请您补缴上次停车费。\r\n\r\n");
                buf.append("补交金额："+orderAddDate.getEscapeprice()+"元\r\n\r\n");
                buf.append("车牌号："+orderAddDate.getCarnum()+" \r\n\r\n");
                buf.append("停车场："+activity.userBean.getParkname()+"   \r\n\r\n");
                buf.append("欠费时间"+ orderAddDate.getParkDateStr()+" \r\n");
                buf.append(Static_bean.ChargingTime);
                buf.append(Static_bean.ChargingStandard);
                buf.append(Static_bean.ChargingUnit);
                buf.append(Static_bean.ComplaintTelephone);


                StringBuffer qRcode = new StringBuffer("http://wx.yilufa.net/pointpay/pointzgr/redict?orderid=");
                qRcode.append(orderAddDate.getOrderid());
                qRcode.append("&pointid=");
                qRcode.append(activity.userBean.getParkid());

                printer_marking(new PrintBillBean(3,buf.toString(),qRcode.toString()));
                break;

            case R.id.order_detailsLeftImage:
                if (order_detailsLeftString!=null)super.showPopupWindow(escapeprice_text,order_detailsLeftString);
                break;

            case R.id.order_detailsRightImage:
                if (order_detailsRightString!=null)super.showPopupWindow(escapeprice_text,order_detailsRightString);
                break;

            default:break;
        }
    }

    //TODO >>>>> 补缴费用完成后跳转
    private void escapedelete(final String object){

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpBean httpBean = JsonUtil2.fromJson(object, HttpBean.class);

                    if (httpBean.getCode() == 200) {

//                    Instrumentation inst = new Instrumentation();
//                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
//                    //返回上个页面
//                   activity.onKeyDown(KeyEvent.KEYCODE_BACK,  null);

                    } else {
                        toast_makeText(httpBean.getMessage());
                    }
                } catch (IOException e) {
                    Log.w(TAG,e);
                }
            }
        });
    }

    //TODO >>>>> 获取逃费出场的照片地址
    private void pointEscapeEscapePic(final Map<String, String> param, final String object){


        try{

            final PointEscapeEscapePicBean picBean = JsonUtil2.fromJson(object,PointEscapeEscapePicBean.class);

            if (picBean.getData()==null){ return; }

            String uuidPanorama = StringUtil.getUuid();
            String uuidInimage = StringUtil.getUuid();

            final String panoramaPath = FileUtil.downloadFile1(picBean.getData().getPanorama(),FileUtil.getSDCardPath() + "/parkings",uuidPanorama);
            final String inimagePath = FileUtil.downloadFile1(picBean.getData().getInimage(),FileUtil.getSDCardPath() + "/parkings",uuidInimage);

            order_detailsLeftString = panoramaPath;
            order_detailsRightString = inimagePath;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {

                        //修改图片暂时
                        order_detailsLeftImage.setImageBitmap( BitmapFactory.decodeFile(order_detailsLeftString));
                        order_detailsRightImage.setImageBitmap( BitmapFactory.decodeFile(order_detailsRightString));

                        //保存到数据库
                        OrderDbBean orderBean = new OrderDbBean();
                        orderBean.setId(param.get("orderid"));
                        orderBean.setPhoto1_url(picBean.getData().getPanorama());
                        orderBean.setPhoto2_url(picBean.getData().getInimage());
                        orderBean.setPhoto1_path(order_detailsLeftString);
                        orderBean.setPhoto2_path(order_detailsRightString);

                        Order_DB.insert_in_Order(activity.baseSQL_DB,orderBean);

                    } catch (Exception e) {
                        Log.w(TAG, e);
                    }

                }
            });

        }catch (Exception e){
            Log.w(TAG,e);
        }
    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        super.onResponsePOST( url,  param,  sign,  object);
        Log.i(TAG, "url="+url + "----param="+param +";sign="+sign + ";object="+object);

        switch (sign) {
            case "escapedelete":escapedelete(object);
                break;

            //TODO  获取逃费出场的照片地址
            case "pointEscapeEscapePic":pointEscapeEscapePic(param,object);
                break;

            default:
                break;
        }
    }

}
