package com.example.parking.fragment;



import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.parking.util.JsonUtil2;
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

    public static boolean sss = false;

    public static final String TAG = "ParkingFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView( inflater, container, savedInstanceState );
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);
        Log.i(TAG,getArguments().getString("joinType")+"---------------------");

        if (sss){
            distinguish_carmun = null;
            parking_carmun.setText("");
            // 剩余车位和预约车位
            Map<String, String> param = new HashMap<String, String>(2);
            param.put("token", activity.userBean.getToken());
            HttpManager2.requestPost(Static_bean.firstPageRecord(), param, this, "firstPageRecord");
            sss = false;
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
                params.put("inimage", right_photo.getTag()==null ?"":right_photo.getTag().toString().split("###")[1]);
                params.put("panorama",left_photo.getTag()==null ?"": left_photo.getTag().toString().split("###")[1]);
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

                   left_photo.setBackground(Drawable.createFromPath(param.get("panoramaPath")));

                    final PhotoToOssBean photoToOssBean = new Gson().fromJson(URLDecoder.decode(obj, "UTF-8"), PhotoToOssBean.class);
                    Log.i(TAG, obj+"----"+URLDecoder.decode(obj, "UTF-8") + "---------" + photoToOssBean.toString());

                    left_photo.setTag(param.get("panoramaPath")+"###"+photoToOssBean.getImgurl());

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

                    right_photo.setBackground(Drawable.createFromPath(param.get("inimagePath")));
                    right_photo.setTag(param.get("inimagePath")+"###"+photoToOssBean.getImgurl());

                    if (photoToOssBean.getCarmun()==null || "".equals(photoToOssBean.getCarmun())){
                        toast_makeText("车牌识别错误，请手动输入车牌");
                        parking_carmun.setText("车牌识别错误");
                        distinguish_carmun = null;
                        return;
                    }

                    distinguish_carmun = photoToOssBean.getCarmun();
                    parking_carmun.setText(photoToOssBean.getCarmun());
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

                String[] inimage = right_photo.getTag()==null ?new String[]{"",""}:right_photo.getTag().toString().split("###");
                String[] panorama = left_photo.getTag()==null ?new String[]{"",""}:left_photo.getTag().toString().split("###");

                OrderDbBean orderDbBean = new OrderDbBean();
                orderDbBean.setId(String.valueOf(httpBean.getData().getId()));
                orderDbBean.setPhoto1_path(panorama[0]);
                orderDbBean.setPhoto1_url(panorama[1]);
                orderDbBean.setPhoto2_path(inimage[0]);
                orderDbBean.setPhoto2_url(inimage[1]);
                Order_DB.insert_in_Order(activity.baseSQL_DB,orderDbBean);

                activity.openParkingIndex();

            }else if ("order".equals(httpBean.getMessage())){

                super.AlertDialog_Builder("提示","已有订单","确定");
            }else if ("escape".equals(httpBean.getMessage())){

                activity.openOrderPayBack(httpBean.getData());
            }else{

                super.AlertDialog_Builder("上传失败",httpBean.getMessage(),"确定");
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