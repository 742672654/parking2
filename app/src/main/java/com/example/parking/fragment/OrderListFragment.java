package com.example.parking.fragment;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.example.parking.R;
import com.example.parking.ui.dataChoice.DataChoiceReturn;
import com.example.parking.ui.dataChoice.DatePickerDialogCustom;
import com.example.parking.ui.dataChoice.SpecificDateBean;
import com.example.parking.util.InputUtil;
import com.example.parking.util.TimeUtil;
import com.example.parking.view.KeyboardViewPager;
import java.util.Map;


/**
 * 功能说明: 全部订单Fragment
 * 日期:	2019年7月24日
 * 开发者:	KXD
 */
public class OrderListFragment extends OrderListBase implements DataChoiceReturn, View.OnTouchListener{

    public static final String TAG = "OrderListFragment<全部订单>";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        order_all_textview_cph = rootView.findViewById(R.id.order_all_textview_cph);
        order_all_textview_cph.setOnTouchListener(this);
        order_all_textview_cph.setOnClickListener(this);
        InputUtil.hideTypewriting(activity, order_all_textview_cph);

        order_all_textview_cph.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节数
                if ( count>before &&  s.length()>5 ){
                    if(kvp!=null && kvp.mPopupWindow!=null)kvp.mPopupWindow.dismiss();
                }
                pointOrderReport_orderlist("onStart");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);
        pointOrderReport_orderlist("onStart");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.order_all_textview_rucangtime_kaisi:

                DatePickerDialogCustom.showDateDialog(activity,this,"请选择开始入场时间",TimeUtil.dateAddDay(-7),"order_all_textview_rucangtime_kaisi");
                break;

            case R.id.order_all_textview_rucangtime_jiesu:

                DatePickerDialogCustom.showDateDialog(activity,this,"请选择结束入场时间","order_all_textview_rucangtime_jiesu");
                break;
            default: break;
        }
    }

    @Override
    public void getSpecificDate(SpecificDateBean specificDate, String sign) {

        switch ( sign ){
            case "order_all_textview_rucangtime_kaisi":
                order_all_textview_rucangtime_kaisi_lian.setText(String.valueOf(specificDate.getYear()));
                order_all_textview_rucangtime_kaisi_yue.setText( specificDate.getMonthOfYear()<10 ? ("0"+specificDate.getMonthOfYear()) : String.valueOf(specificDate.getMonthOfYear()));
                order_all_textview_rucangtime_kaisi_ri.setText(specificDate.getDayOfMonth()<10 ? ("0"+specificDate.getDayOfMonth()) : String.valueOf(specificDate.getDayOfMonth()));
                pointOrderReport_orderlist("onStart");
                break;

            case "order_all_textview_rucangtime_jiesu":
                order_all_textview_rucangtime_jiesu_lian.setText( String.valueOf(specificDate.getYear()));
                order_all_textview_rucangtime_jiesu_yue.setText( specificDate.getMonthOfYear()<10 ? ("0"+specificDate.getMonthOfYear()) : String.valueOf(specificDate.getMonthOfYear()));
                order_all_textview_rucangtime_jiesu_ri.setText(specificDate.getDayOfMonth()<10 ? ("0"+specificDate.getDayOfMonth()) : String.valueOf(specificDate.getDayOfMonth()));
                pointOrderReport_orderlist("onStart");
                break;

            default:break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if ("请输入车牌号".equals(order_all_textview_cph.getText().toString())){
            order_all_textview_cph.setText("");
        }

        if (v.getId() == R.id.order_all_textview_cph && !(kvp != null && kvp.mPopupWindow != null && kvp.mPopupWindow.isShowing())) {
            if (kvp == null) { kvp = new KeyboardViewPager(activity, true); }
            kvp.setEt_carnumber(order_all_textview_cph);
            kvp.showPopupWindow(order_all_textview_cph);
        }
        return false;
    }



}