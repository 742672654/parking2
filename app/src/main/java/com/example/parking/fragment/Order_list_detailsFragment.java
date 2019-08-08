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
import com.example.parking.bean.http.OrderAddBean;
import com.example.parking.bean.http.OrderDetailsBean;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.bean.http.ParkingSpaceBean;
import com.example.parking.bean.http.ParkingSpaceData;
import com.example.parking.bean.http.Report_orderlistBean;
import com.example.parking.db.Order_DB;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@SuppressLint("LongLogTag")
public class Order_list_detailsFragment extends BaseFragment{

    public static final String TAG = "Order_detailsFragment<订单收费>";



    private TextView order_details_a11,order_details_a22,order_details_a22222,order_details_a33,
                order_details_a44,order_details_a55,order_details_a66;
//    protected Button order_lis_dayin_rucang;

    private ImageView order_lis_panoramaImageView;  // 车牌本地地址
    private String order_lis_panoramaImage;
    protected ImageView order_lis_inimageImageView;   // 全景本地地址
    private String order_lis_inimageImage;

    private Report_orderlistBean.Report_orderlistList article;
    private PopupWindow popupWindow;
    protected ImageView popuwindow_ImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_list_details, container, false);

        initView(rootView);
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        order_lis_panoramaImage = null;
        order_lis_inimageImage = null;
    }

    private void initView(View rootView) {

        order_details_a11 = rootView.findViewById(R.id.order_details_a11);
        order_details_a22 = rootView.findViewById(R.id.order_details_a22);
        order_details_a22222 = rootView.findViewById(R.id.order_details_a22222);
        order_details_a33 = rootView.findViewById(R.id.order_details_a33);
        order_details_a44 = rootView.findViewById(R.id.order_details_a44);
        order_details_a55 = rootView.findViewById(R.id.order_details_a55);
        order_details_a66 = rootView.findViewById(R.id.order_details_a66);

//        order_lis_dayin_rucang = rootView.findViewById(R.id.order_lis_dayin_rucang);
//        order_lis_dayin_rucang.setOnClickListener(this);

        order_lis_panoramaImageView = rootView.findViewById(R.id.order_lis_panoramaImageView);    //车牌本地地址
        order_lis_panoramaImageView.setOnClickListener(this);
        order_lis_inimageImageView = rootView.findViewById(R.id.order_lis_inimageImageView);   // 全景本地地址
        order_lis_inimageImageView.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);

        article = (Report_orderlistBean.Report_orderlistList) getArguments().getSerializable("report_orderlistList");

        order_details_a11.setText(article.CarNum);
        order_details_a22.setText(article.OrderPrice);
        order_details_a22222.setText(article.PrePrice);

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(article.ParkDate);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(article.LeaveDate);
            order_details_a33.setText(TimeUtil.getDatePoor(date2,date));
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
            default:break;
        }
    }

}

