package com.example.parking.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.bean.OrderDbBean;
import com.example.parking.bean.PrintBillBean;
import com.example.parking.bean.http.HttpBean;
import com.example.parking.bean.http.OrderDetailsBean;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.bean.http.ParkingSpaceBean;
import com.example.parking.bean.http.ParkingSpaceData;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.db.Order_DB;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.StringUtil;

import java.util.HashMap;
import java.util.Map;


@SuppressLint({"LongLogTag"})
public class Order_detailsFragment extends BaseFragment{

    public static final String TAG = "Order_detailsFragment<订单收费>";

    protected Button soufei_Button,taofei_Button,dayin_rucang;
    protected TextView a1,a22,a3,a33,a4,a5,a6;
    protected ImageView inimageImageView;    //车牌本地地址
    protected ImageView panoramaImageView;   // 全景本地地址
    private String inimageImageString = null;
    private String panoramaImageString = null;

    public OrderlistBean.OrderlistData  orderlistData;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_details, container, false);

        initView(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);
        orderlistData = null;
        orderlistData = (OrderlistBean.OrderlistData) getArguments().getSerializable("orderlistData");

        // 去除<未处理>标识
        Jiguang_DB.updata_Jiguang(activity.baseSQL_DB,null,orderlistData.getSubid());

        Map<String,String> param = new HashMap<String,String>(6);
        param.put("token",activity.userBean.getToken());
        param.put("id",orderlistData.getId());
        param.put("camum",orderlistData.getCarNo());
        param.put("subname",orderlistData.getSubname());
        HttpManager2.requestPost(Static_bean.getLeavePageOrder(),  param, this, "getLeavePageOrder");
    }

    @Override
    public void onStop() {
        super.onStop();
        inimageImageString = null;
        panoramaImageString = null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.soufei:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Light_Dialog);

                builder.setTitle("选择支付方式");
                builder.setIcon(R.mipmap.money);

                //    指定下拉列表的显示数据
                final String[] cities = {"现金支付", "扫码支付"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getActivity(), "支付方式：" + cities[which], Toast.LENGTH_SHORT).show();

                        switch (cities[which]){

                            case "现金支付":
                                Map<String, String> param2 = new HashMap<String, String>(11);
                                param2.put("token", activity.userBean.getToken());
                                param2.put("id", orderlistData.getId());
                                param2.put("paytype", "1");
                                param2.put("orderprice", a3.getText().toString());
                                param2.put("subid", orderlistData.getSubid());
                                param2.put("subname", orderlistData.getSubname());
                                param2.put("outimage", activity.orderFragment.outimage);
                                HttpManager2.requestPost(Static_bean.payPointOrderToPoint(), param2, Order_detailsFragment.this, "payPointOrderToPoint");
                                break;

                            case "扫码支付":
                                Map<String, String> param = new HashMap<String, String>(11);
                                param.put("token", activity.userBean.getToken());
                                param.put("id", orderlistData.getId());
                                param.put("paytype", "2");
                                param.put("orderprice", a3.getText().toString());
                                param.put("subid", orderlistData.getSubid());
                                param.put("subname", orderlistData.getSubname());
                                param.put("outimage", activity.orderFragment.outimage);
                                HttpManager2.requestPost(Static_bean.payPointOrderToPoint(), param, Order_detailsFragment.this, "payPointOrderToPoint");
                                break;

                            default: break;
                        }
                    }
                });
                builder.show();
                break;

            case R.id.taofei:

                try {
                    if ("0".equals(a3.getText().toString())){
                        toast_makeText("订单0元，请结算订单");
                        return;
                    }

                    if ( Double.valueOf(a22.getText().toString())<=0){
                        toast_makeText("无需再缴费，请结算订单");
                        return;
                    }
                }catch (Exception e){
                    Log.w(TAG,e);
                    return;
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("逃费处理");
                        builder.setMessage("是否按照逃费处理");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String,String> param = new HashMap<String,String>(8);
                                param.put("token",activity.userBean.getToken());
                                param.put("id",orderlistData.getId());
                                param.put("subid",orderlistData.getSubid());
                                param.put("subname",orderlistData.getSubname());
                                param.put("carnum",orderlistData.getCarNo());
                                HttpManager2.requestPost(Static_bean.escape_add(),  param, Order_detailsFragment.this, "escape_add");
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                        builder.show();
                    }
                });
                break;

            case R.id.order_details_a6:

                Map<String,String> param = new HashMap<String,String>(3);
                param.put("token",activity.userBean.getToken());
                param.put("id",orderlistData.getId());
                HttpManager2.requestPost(Static_bean.selectOrderSubPlace(),  param, this, "selectOrderSubPlace");
                break;

            case R.id.dayin_rucang:
                printing();
//                StringBuffer buf = new StringBuffer("\r\n\r\n---------------------------\r\n");
//                buf.append("停车场："+activity.userBean.getParkname()+"   \r\n\r\n");
//                buf.append("车位号："+orderlistData.getSubname()+" \r\n\r\n");
//                buf.append("车牌号："+orderlistData.getCarNo()+" \r\n\r\n");
//                buf.append("驶入时间"+ (a5.getText())+" \r\n");
//                buf.append("预付金额："+a33.getText().toString()+"元\r\n\r\n");
//
//                buf.append("每天单次收费5元，晚上12点后重新收费。车辆离开车位后，视为停车订单结算完成。\r\n\r\n");
//                buf.append("收费单位：泉州市畅顺停车管理有限公司\r\n\r\n");
//                buf.append("监督电话：0595-28282818");
//
//                StringBuffer qRcode = new StringBuffer(Static_bean.QRcode_redict());
//                qRcode.append("?orderid=").append(orderlistData.getId());
//                qRcode.append("&pointid=");
//                qRcode.append(activity.userBean.getParkid());
//
//                printer_marking( new PrintBillBean(1,buf.toString(),qRcode.toString()) );
                break;

            case R.id.panoramaImageView:
                if (panoramaImageString!=null)super.showPopupWindow(a1,panoramaImageString);
                break;

            case R.id.inimageImageView:
                if (inimageImageString!=null)super.showPopupWindow(a1,inimageImageString);
                break;

        default:break;
        }
    }


    //TODO 初始化控件
    private void initView(View rootView) {

        soufei_Button = rootView.findViewById(R.id.soufei);
        soufei_Button.setOnClickListener(this);

        taofei_Button = rootView.findViewById(R.id.taofei);
        taofei_Button.setOnClickListener(this);

        dayin_rucang = rootView.findViewById(R.id.dayin_rucang);
        dayin_rucang.setOnClickListener(this);

        a1 = rootView.findViewById(R.id.order_details_a1);
        a22 = rootView.findViewById(R.id.order_details_a22);
        a3 = rootView.findViewById(R.id.order_details_a3);
        a33 = rootView.findViewById(R.id.order_details_a33);
        a4 = rootView.findViewById(R.id.order_details_a4);
        a5 = rootView.findViewById(R.id.order_details_a5);
        a6 = rootView.findViewById(R.id.order_details_a6);

        a6.setOnClickListener(this);

        inimageImageView = rootView.findViewById(R.id.inimageImageView);    //车牌本地地址
        inimageImageView.setOnClickListener(this);
        panoramaImageView = rootView.findViewById(R.id.panoramaImageView);   // 全景本地地址
        panoramaImageView.setOnClickListener(this);
    }

    //TODO >>>接收订单详情
    private void getLeavePageOrder(final Map<String, String> param, final String object) {

        activity.runOnUiThread(new Runnable() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                try {

                    OrderDetailsBean  orderDetailsBean = JsonUtil2.fromJson(object, OrderDetailsBean.class);
                    a1.setText(orderDetailsBean.getData().getCarnum()==null?"":orderDetailsBean.getData().getCarnum());
                    a22.setText(String.valueOf(orderDetailsBean.getData().getWait_price()));
                    a3.setText(String.valueOf(orderDetailsBean.getData().getPrice()));
                    a33.setText(String.valueOf(orderDetailsBean.getData().getPre_price()));
                    a4.setText(orderDetailsBean.getData().getTime());
                    a5.setText(orderDetailsBean.getData().getStartTime());
                    a6.setText(param.get("subname"));

                    //去数据库查询出照片来
                    OrderDbBean orderBean = Order_DB.query_Order(activity.baseSQL_DB,String.valueOf(orderDetailsBean.getData().getId()));

                    if (orderBean==null){

                    }

                    if ( StringUtil.is_valid(orderBean.getPhoto1_path()) ){
                        panoramaImageView.setImageBitmap( BitmapFactory.decodeFile(orderBean.getPhoto1_path()));
                        panoramaImageString = orderBean.getPhoto1_path();
                    }

                    if ( StringUtil.is_valid(orderBean.getPhoto2_path()) ){
                        inimageImageView.setImageBitmap( BitmapFactory.decodeFile(orderBean.getPhoto2_path()));
                        inimageImageString = orderBean.getPhoto2_path();
                    }

                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            }
        });
    }

    //TODO >>>获取订单收费结果
    public void payPointOrderToPoint(final Map<String, String> param, final String object) {

        HttpBean httpBean = JsonUtil2.fromJson(object,HttpBean.class);
        if (httpBean.getCode()==200){

            toast_makeText("订单结算完成");

            StringBuffer buf = new StringBuffer("\r\n\n---------------------------\r\n");
            buf.append("停车场："+activity.userBean.getParkname()+"   \r\n\r\n");
            buf.append("车位号："+a6.getText().toString()+"\r\n\r\n");
            buf.append("车牌号："+a1.getText().toString()+"\r\n\r\n");
            buf.append("驶入时间"+a5.getText().toString()+"\r\n\r\n");
            buf.append("驶出时间"+httpBean.getData()+"\r\n\r\n");
            buf.append("停车时长："+a4.getText().toString()+"\r\n\r\n");
            buf.append("总停车费："+ a3.getText().toString()+"元\r\n\r\n");
            buf.append("已缴金额："+ a33.getText().toString()+"元\r\n\r\n");
            buf.append("本次收取："+ a22.getText().toString()+"元\r\n\r\n");
            buf.append(Static_bean.ChargingTime);
            buf.append(Static_bean.ChargingStandard);
            buf.append(Static_bean.ChargingUnit);
            buf.append(Static_bean.ComplaintTelephone);

            String QRcode = Static_bean.QRcode_orderdetail()+"?orderid="+param.get("id");
            PrintBillBean PrintBillBean = new PrintBillBean(2,buf.toString(),QRcode);

            printer_marking(PrintBillBean);

            activity.openParkingIndex();
        }else if (httpBean.getCode()==400){

            toast_makeText(httpBean.getMessage());
        }
    }

    //TODO >>>接收逃费处理
    private void escape_add(final Map<String, String> param, final String object) {

        HttpBean httpBean = JsonUtil2.fromJson(object,HttpBean.class);
        if (httpBean.getCode()==200){

            toast_makeText("订单已提交");
            activity.openParkingIndex();
        }else if (httpBean.getCode()==400){
            toast_makeText(httpBean.getMessage());
        }
    }

    //TODO >>>接收可用车位接口
    private void selectOrderSubPlace(final Map<String, String> param, final String object) {

        final ParkingSpaceBean httpBean = JsonUtil2.fromJson(object,ParkingSpaceBean.class);

        if (httpBean.getData()==null){return;}
        final String[] listString= new String[httpBean.getData().size()];
        for (int i=0;i<httpBean.getData().size();i++){
            listString[i]=httpBean.getData().get(i).getSubname();
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (httpBean.getCode() == 200) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Light_Dialog);
                    builder.setItems(listString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int w) {
                            for (int i = 0; i < listString.length; i++) {
                                if (httpBean.getData().get(i).getSubname().equals(listString[w])) {

                                    ParkingSpaceData selectSubPlaceData = httpBean.getData().get(i);
                                    orderlistData.setSubid(selectSubPlaceData.getId());
                                    orderlistData.setSubname(selectSubPlaceData.getSubname());

                                    a6.setText(selectSubPlaceData.getSubname());
                                }
                            }
                        }
                    }).show();

                } else if (httpBean.getCode() == 400) {
                    toast_makeText(httpBean.getMessage());
                }
            }
        });
    }

    //TODO 打印入场小票
    private void printing(){

        StringBuffer buf = new StringBuffer("\r\n\r\n---------------------------\r\n");
        buf.append("停车场："+activity.userBean.getParkname()+"   \r\n\r\n");
        buf.append("车位号："+orderlistData.getSubname()+" \r\n\r\n");
        buf.append("车牌号："+orderlistData.getCarNo()+" \r\n\r\n");
        buf.append("驶入时间"+ (a5.getText())+" \r\n");
        buf.append("预付金额："+a33.getText().toString()+"元\r\n\r\n");
        buf.append(Static_bean.ChargingTime);
        buf.append(Static_bean.ChargingStandard);
        buf.append(Static_bean.ChargingUnit);
        buf.append(Static_bean.ComplaintTelephone);

        StringBuffer qRcode = new StringBuffer(Static_bean.QRcode_redict());
        qRcode.append("/").append(orderlistData.getId());
        qRcode.append("/");
        qRcode.append(activity.userBean.getParkid());

        printer_marking( new PrintBillBean(1,buf.toString(),qRcode.toString()) );
    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        super.onResponsePOST(url, param, sign, object);
        Log.i(TAG,"url="+url+";param="+param+";sign="+sign+";object="+object);
        switch (sign){

            case "getLeavePageOrder":getLeavePageOrder(param,object);
                break;

            case "payPointOrderToPoint":payPointOrderToPoint(param,object);
                break;

            case "escape_add":escape_add(param,object);
                break;

            case "selectOrderSubPlace":selectOrderSubPlace(param,object);
                break;
            default:break;
        }

    }

}

