package com.example.parking.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.bean.OrderDbBean;
import com.example.parking.bean.PrintBillBean;
import com.example.parking.bean.http.HttpBean;
import com.example.parking.bean.http.OrderDetailsBean;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.bean.http.ParkingSpaceBean;
import com.example.parking.bean.http.ParkingSpaceData;
import com.example.parking.db.Order_DB;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.JsonUtil2;
import java.util.HashMap;
import java.util.Map;


@SuppressLint("LongLogTag")
public class Order_detailsFragment extends BaseFragment{

    public static final String TAG = "Order_detailsFragment<订单收费>";

    protected Button soufei_Button,taofei_Button,dayin_rucang;
    protected TextView a1,a22,a3,a33,a4,a5,a6;

    protected ImageView inimageImageView;    //车牌本地地址
    protected ImageView panoramaImageView;   // 全景本地地址

    protected ImageView popuwindow_ImageView;

    private String inimageImageString = null;
    private String panoramaImageString = null;

    private OrderlistBean.OrderlistData  orderlistData;
    private PopupWindow popupWindow;

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

        Map<String,String> param = new HashMap<String,String>(4);
        param.put("token",activity.userBean.getToken());
        param.put("id",orderlistData.getId());
        param.put("camum",orderlistData.getCarNo());
        param.put("subname",orderlistData.getSubname());
        HttpManager2.requestPost(Static_bean.getLeavePageOrder,  param, this, "getLeavePageOrder");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.soufei:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("结算订单");
                builder.setMessage("是否收费出场");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Map<String, String> param = new HashMap<String, String>(6);
                        param.put("token", activity.userBean.getToken());
                        param.put("id", orderlistData.getId());
                        param.put("orderprice", a3.getText().toString());
                        param.put("subid", orderlistData.getSubid());
                        param.put("subname", orderlistData.getSubname());
                        param.put("outimage", activity.orderFragment.outimage);
                        HttpManager2.requestPost(Static_bean.payPointOrderToPoint, param, Order_detailsFragment.this, "payPointOrderToPoint");
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
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
                                Map<String,String> param = new HashMap<String,String>(6);
                                param.put("token",activity.userBean.getToken());
                                param.put("id",orderlistData.getId());
                                param.put("subid",orderlistData.getSubid());
                                param.put("subname",orderlistData.getSubname());
                                param.put("carnum",orderlistData.getCarNo());
                                HttpManager2.requestPost(Static_bean.escape_add,  param, Order_detailsFragment.this, "escape_add");
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

                Map<String,String> param = new HashMap<String,String>(2);
                param.put("token",activity.userBean.getToken());
                param.put("id",orderlistData.getId());
                HttpManager2.requestPost(Static_bean.selectOrderSubPlace,  param, this, "selectOrderSubPlace");
                break;

            case R.id.dayin_rucang:

                StringBuffer buf = new StringBuffer("\r\n\r\n---------------------------\r\n");
                buf.append("停车场："+activity.userBean.getParkname()+"   \r\n\r\n");
                buf.append("车位号："+orderlistData.getSubname()+" \r\n\r\n");
                buf.append("车牌号："+orderlistData.getCarNo()+" \r\n\r\n");
                buf.append("驶入时间"+ (a5.getText())+" \r\n");
                buf.append("预交金额："+a33.getText().toString()+"元\r\n\r\n");

                buf.append("每天单次收费5元，晚上12点后重新收费。车辆离开车位后，视为停车订单结算完成。\r\n\r\n");
                buf.append("收费单位：泉州市畅顺停车管理有限公司\r\n\r\n");
                buf.append("监督电话：0595-28282818");

                StringBuffer qRcode = new StringBuffer(Static_bean.QRcode_redict);
                qRcode.append("?orderid=").append(orderlistData.getId());
                qRcode.append("&pointid=");
                qRcode.append(activity.userBean.getParkid());

                PrintBillBean PrintBillBean = new PrintBillBean(1,buf.toString(),qRcode.toString());

                printer_marking(PrintBillBean);
                break;

            case R.id.panoramaImageView:showPopupWindow(panoramaImageString);
                break;

            case R.id.inimageImageView:showPopupWindow(inimageImageString);
                break;

        default:break;
        }
    }


    private void showPopupWindow(String path) {

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
            popupWindow.setHeight((int) (dm.heightPixels * 0.8 - 8));

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
        //  popupWindow.showAsDropDown(inimageImageView,0,dm.heightPixels);
        popupWindow.showAtLocation(a1, Gravity.NO_GRAVITY, 0, 150);
        popuwindow_ImageView.setImageBitmap( BitmapFactory.decodeFile(path));
    }

    // 初始化控件
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

                    OrderDbBean orderBean = Order_DB.query_Order(activity.baseSQL_DB,String.valueOf(orderDetailsBean.getData().getId()));
                    panoramaImageView.setImageBitmap( BitmapFactory.decodeFile(orderBean.getPhoto1_path()));
                    panoramaImageString = orderBean.getPhoto1_path();

                    inimageImageView.setImageBitmap( BitmapFactory.decodeFile(orderBean.getPhoto2_path()));
                    inimageImageString = orderBean.getPhoto2_path();

                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            }
        });
    }

    //TODO >>>获取订单收费结果
    private void payPointOrderToPoint(final Map<String, String> param, final String object) {

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
            buf.append("收费单位：泉州市畅顺停车管理有限公司\r\n\r\n");
            buf.append("监督电话：0595-28282818");

            String QRcode = Static_bean.QRcode_orderdetail+"?orderid="+param.get("id");
            PrintBillBean PrintBillBean = new PrintBillBean(2,buf.toString(),QRcode);

            printer_marking(PrintBillBean);

            activity.openOrder();
        }else if (httpBean.getCode()==400){

            toast_makeText(httpBean.getMessage());
        }
    }

    //TODO >>>接收逃费处理
    private void escape_add(final Map<String, String> param, final String object) {

        HttpBean httpBean = JsonUtil2.fromJson(object,HttpBean.class);
        if (httpBean.getCode()==200){

            toast_makeText("订单已提交");
            activity.openOrder();
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
                    toast_makeText("订单已提交");

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

    @SuppressLint("LongLogTag")
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

