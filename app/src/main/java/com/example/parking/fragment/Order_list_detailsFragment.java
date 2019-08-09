package com.example.parking.fragment;


import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.bean.OrderDbBean;
import com.example.parking.bean.PrintBillBean;
import com.example.parking.bean.http.Report_orderlistBean;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.db.Order_DB;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.TimeUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class Order_list_detailsFragment extends BaseFragment{


    public static final String TAG = "Order_list_detailsFragment<历史订单的详情>";


    private TextView order_details_a11,order_details_aa22,order_details_a22222,order_details_aa33,
                order_details_a44,order_details_a55,order_details_a66;
    protected Button order_lis_dayin_rucang;

    private ImageView order_lis_panoramaImageView;  // 车牌本地地址
    private String order_lis_panoramaImage;
    protected ImageView order_lis_inimageImageView;   // 全景本地地址
    private String order_lis_inimageImage;

    private Report_orderlistBean.Report_orderlistList article;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_list_details, container, false);

        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {

        order_details_a11 = rootView.findViewById(R.id.order_details_a11);
        order_details_aa22 = rootView.findViewById(R.id.order_details_aa22);
        order_details_a22222 = rootView.findViewById(R.id.order_details_a22222);
        order_details_aa33 = rootView.findViewById(R.id.order_details_aa33);
        order_details_a44 = rootView.findViewById(R.id.order_details_a44);
        order_details_a55 = rootView.findViewById(R.id.order_details_a55);
        order_details_a66 = rootView.findViewById(R.id.order_details_a66);

        order_lis_dayin_rucang = rootView.findViewById(R.id.order_lis_dayin_rucang);
        order_lis_dayin_rucang.setOnClickListener(this);

        order_lis_panoramaImageView = rootView.findViewById(R.id.order_lis_panoramaImageView);    //车牌本地地址
        order_lis_panoramaImageView.setOnClickListener(this);
        order_lis_inimageImageView = rootView.findViewById(R.id.order_lis_inimageImageView);   // 全景本地地址
        order_lis_inimageImageView.setOnClickListener(this);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);

        article = (Report_orderlistBean.Report_orderlistList) getArguments().getSerializable("report_orderlistList");
        if (article==null)return;

        order_details_a11.setText(article.CarNum);
        order_details_aa22.setText(article.OrderPrice);
        order_details_a22222.setText(article.PrePrice);

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(article.ParkDate);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(article.LeaveDate);
            order_details_aa33.setText(TimeUtil.getDatePoor(date2,date));
        } catch (ParseException e) {
            Log.w(TAG,e);
        }
        order_details_a44.setText(article.ParkDate );
        order_details_a55.setText(article.LeaveDate );
        order_details_a66.setText( article.SubName);

        OrderDbBean orderBean = Order_DB.query_Order(activity.baseSQL_DB,String.valueOf(article.id));
        if (orderBean==null)return;

        order_lis_panoramaImageView.setImageBitmap( BitmapFactory.decodeFile(orderBean.getPhoto1_path()));
        order_lis_panoramaImage = orderBean.getPhoto1_path();

        order_lis_inimageImageView.setImageBitmap( BitmapFactory.decodeFile(orderBean.getPhoto2_path()));
        order_lis_inimageImage = orderBean.getPhoto2_path();

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.order_lis_panoramaImageView:
                if (order_lis_panoramaImage!=null)super.showPopupWindow(order_details_a11,order_lis_panoramaImage);
                break;

            case R.id.order_lis_inimageImageView:
                if (order_lis_inimageImage!=null)super.showPopupWindow(order_details_a11,order_lis_inimageImage);
                break;

            case R.id.order_lis_dayin_rucang:
                printingTickets();
                break;

            default:break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        order_lis_panoramaImage = null;
        order_lis_inimageImage = null;
    }

    public void printingTickets() {

        StringBuffer buf = new StringBuffer("\r\n\n---------------------------\r\n");
        buf.append("停车场："+activity.userBean.getParkname()+"   \r\n\r\n");
        buf.append("车位号："+order_details_a66.getText().toString()+"\r\n\r\n");
        buf.append("车牌号："+order_details_a11.getText().toString()+"\r\n\r\n");
        buf.append("驶入时间"+order_details_a44.getText().toString()+"\r\n\r\n");
        buf.append("驶出时间"+order_details_a55.getText().toString()+"\r\n\r\n");
        buf.append("停车时长："+order_details_aa33.getText().toString()+"\r\n\r\n");
        buf.append("总停车费："+ order_details_aa22.getText().toString()+"元\r\n\r\n");
        buf.append("已缴金额："+ order_details_a22222.getText().toString()+"元\r\n\r\n");
        buf.append("本次收取："+ ( Double.valueOf(order_details_aa22.getText().toString())
                -Double.valueOf(order_details_a22222.getText().toString()) )+"元\r\n\r\n");
        buf.append("收费单位：泉州市畅顺停车管理有限公司\r\n\r\n");
        buf.append("监督电话：0595-28282818");

        String QRcode = Static_bean.QRcode_orderdetail()+"?orderid=" + article.id;
        PrintBillBean PrintBillBean = new PrintBillBean(2,buf.toString(),QRcode);

        printer_marking(PrintBillBean);

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResponsePOST(String url, final Map<String, String> param, String sign, final String object) {
        super.onResponsePOST(url, param, sign, object);

        Log.i(TAG,"url="+url+";param="+param+";sign="+sign+";\r\n object="+object);


        switch (sign){

            case "pointOrderReport_orderlist":

                activity.openOrder_list_details(getArticleList(object));
                break;

            default:break;
        }

    }

    //TODO 接收数据返回
    @SuppressLint("LongLogTag")
    private Report_orderlistBean.Report_orderlistList getArticleList( String object ) {

        try {

            Report_orderlistBean report_orderlistBean = JsonUtil2.fromJson(object, Report_orderlistBean.class);
            Log.i(TAG, "解析http返回的json数据=" + report_orderlistBean.toString());

            if (report_orderlistBean.getCode() == 200 && report_orderlistBean.getData() != null) {

                return report_orderlistBean.getData().getList().get(0);
            }
            toast_makeText(report_orderlistBean.getMessage());
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return null;
    }

}

