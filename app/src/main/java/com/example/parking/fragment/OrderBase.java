package com.example.parking.fragment;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.adapter.ProvinceGridViewAdapter;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.http.HttpCallBack2;
import com.example.parking.http.HttpManager2;
import com.example.parking.listView.OrderView;
import com.example.parking.util.InputUtil;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 功能说明: 订单Fragment
 * 日期:	2019年5月29日
 * 开发者:	KXD
 */
public class OrderBase extends BaseFragment implements OnClickListener, HttpCallBack2, View.OnTouchListener {


    public static final String TAG = "OrderBase<订单>";

    protected EditText carNun;
    protected Button btn_free,allCharge,order_photo;
    private PopupWindow popupWindow = null;
    private ListView listView;
    List<OrderlistBean.OrderlistData> orderlistData_list = null;
    public OrderView myAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_fragment, container, false);

        listView = rootView.findViewById(R.id.orderlistview);
        carNun = rootView.findViewById(R.id.order_carmun);
        carNun.setOnTouchListener(this);
        carNun.setOnClickListener(this);
        InputUtil.hideTypewriting(activity, carNun);

        btn_free = rootView.findViewById(R.id.btn_free);
        btn_free.setOnClickListener(this);
        allCharge = rootView.findViewById(R.id.allCharge);
        allCharge.setOnClickListener(this);
        order_photo = rootView.findViewById(R.id.order_photo);
        order_photo.setOnClickListener(this);

        carNun.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //s:变化后的所有字符
                //Log.i("Seachal:","变化:"+s+";"+start+";"+before+";"+count);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数
               // Log.i("Seachal:","变化前:"+s+";"+start+";"+count+";"+after);
            }
            @Override
            public void afterTextChanged(Editable s) {
                //S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节数
                Log.i("Seachal:","变化后:"+orderlistData_list);
                //光标移动到最后
                carNun.setSelection(s.length());//
                if (carNun.getText().toString().contains("错误")){return;}

                if (StringUtil.is_valid(s.toString())){
                    ArrayList<OrderlistBean.OrderlistData> list =  new ArrayList<OrderlistBean.OrderlistData>();
                    for (int i=0;orderlistData_list!=null && i<orderlistData_list.size();i++){

                        if(orderlistData_list.get(i).getCarNo().contains(s)){
                            list.add(orderlistData_list.get(i));
                        }
                    }
                    //第二步：创建适配器,存入数据
                    myAdapter = new OrderView(activity,OrderBase.this, list);
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            //第三步：绑定适配器
                            listView.setAdapter(myAdapter);
                        }
                    });
                }else{
                    orderlist();
                    return;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        carNun.getText().clear();
        super.onStart();
        super.onPosition(TAG);
    }

    @Override
    public void onClick(View v){}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //刷出订单list
        orderlist();
    }

    //TODO 刷出订单list
    protected void orderlist(){

        carNun.getText().clear();

        Map<String,String> param = new HashMap<String,String>(1);
        param.put("token",activity.userBean.getToken());
        HttpManager2.requestPost(Static_bean.orderlist,  param, this, "orderlist");
    }

    //TODO >>>>刷出订单list
    private void orderlist( String obj ){

        Log.i(TAG,"--------------"+obj);
        OrderlistBean orderlistBean = JsonUtil2.fromJson(obj,OrderlistBean.class);

        if(orderlistBean==null || orderlistBean.getCode()!=200 || orderlistBean.getData()==null || orderlistBean.getData().size()==0){

            myAdapter = null;
        }else{

            orderlistData_list = orderlistBean.getData();
            //第二步：创建适配器,存入数据
            myAdapter = new OrderView(activity,this, orderlistData_list);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //第三步：绑定适配器
                listView.setAdapter(myAdapter);
            }
        });
    }

    private void showPopupWindow(final TextView textView ) {
        //获取自定义的菜单布局文件
        View inflate = getLayoutInflater().inflate(R.layout.popuwindow_layout,null,false);
        //创建popupwindow的实例
        popupWindow = new PopupWindow(inflate, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);

        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        popupWindow.setWidth(dm.widthPixels);
        popupWindow.setHeight((int)(dm.heightPixels*0.40));

        //单机其他地方消失
        inflate.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent notionEvent) {
                //如果菜单存在并且为显示状态，就关闭菜单，并且初始化菜单
                if(popupWindow !=null && popupWindow.isShowing()){ popupWindow.dismiss(); }
                return false;
            }
        });
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(textView, Gravity.NO_GRAVITY, 0, dm.heightPixels);

        GridView gv_province = inflate.findViewById(R.id.viewpager222);
        gv_province.setSelector(new ColorDrawable(Color.TRANSPARENT));

        final String[] province = new String[] { "0", "1", "A", "B", "C", "D",
                "E", "2", "3", "F", "G", "H", "J", "K","4", "5", "L", "M",
                "N", "P", "Q", "6", "7", "R", "S", "T", "U", "V", "8", "9",
                "W", "X", "Y", "Z", ""};
        ArrayList<String> provinces = new ArrayList<String>();
        for (int i = 0; i < province.length; i++) { provinces.add(province[i]); }

        ProvinceGridViewAdapter adapter = new ProvinceGridViewAdapter(activity, provinces, false);
        gv_province.setAdapter(adapter);

        gv_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG,"------------"+province[position]);
                if (position == 34){
                    String text = textView.getText().toString();
                    textView.setText(text.length()!=0?text.subSequence(0,text.length()-1):"");
                }else{
                    textView.setText(textView.getText()+province[position]);
                }
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (popupWindow==null || !popupWindow.isShowing()){
            showPopupWindow( carNun );
        }
        return false;
    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {

        switch (sign) {
            case "orderlist":
                orderlist(object);
                break;
            default:
                break;
        }
    }

}
