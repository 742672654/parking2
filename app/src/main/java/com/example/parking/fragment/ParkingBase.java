package com.example.parking.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.bean.http.ParkingSpaceData;
import com.example.parking.bean.http.SelectSubPlaceBean;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.util.InputUtil;
import com.example.parking.util.StringUtil;
import com.example.parking.view.KeyboardViewPager;


/**
 * 功能说明: 停车Fragment
 * 日期:	2019年5月29日
 * 开发者:	KXD
 */
public class ParkingBase extends BaseFragment implements View.OnTouchListener {


    public static final String TAG = "ParkingBase";


    public EditText parking_carmun;//车牌号
    protected EditText parking_pre_price;//预交费用

    protected TextView nouse; //剩余车位
    protected TextView wxnum; //预约车位

    public RelativeLayout left_photo,right_photo; //左右拍照

    protected Button parking_photo;
    protected Button parking_photo2;
    protected Button button_order_add;
    protected TextView button_choice_parkingspace;

    public String distinguish_carmun;//网上识别的车牌号

    private AlertDialog alertDialog = null;

    //车位listView
    protected SelectSubPlaceBean.SelectSubPlaceData selectSubPlaceDate;

    public KeyboardViewPager kvp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.parking_fragment, container, false);

        initView(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);
        //车位listView
        selectSubPlaceDate = (SelectSubPlaceBean.SelectSubPlaceData) getArguments().getSerializable("selectSubPlaceDate");
        //修改停车位
        button_choice_parkingspace.setText( selectSubPlaceDate.getCode() );

        // 去除<未处理>标识
        Jiguang_DB.updata_Jiguang(activity.baseSQL_DB,null,selectSubPlaceDate.getId());
    }


    public void setListview_item_title(final ParkingSpaceData parkingSpaceBean){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                button_choice_parkingspace.setText(parkingSpaceBean.getSubname());
            }
         });
    }

    // 初始化控件
    private void initView(View rootView) {


        left_photo = rootView.findViewById(R.id.left_photo);
        right_photo = rootView.findViewById(R.id.right_photo);

        nouse = rootView.findViewById(R.id.nouse);
        wxnum = rootView.findViewById(R.id.wxnum);

        parking_photo = rootView.findViewById(R.id.parking_photo);
        parking_photo.setOnClickListener(this);

        parking_photo2 = rootView.findViewById(R.id.parking_photo3);
        parking_photo2.setOnClickListener(this);

        button_order_add = rootView.findViewById(R.id.button_order_add);
        button_order_add.setOnClickListener(this);

        button_choice_parkingspace = rootView.findViewById(R.id.btn_choice_parkingspace);
        button_choice_parkingspace.setOnClickListener(this);

        parking_pre_price = rootView.findViewById(R.id.parking_pre_price);
        parking_pre_price.setOnTouchListener(this);
        InputUtil.hideTypewriting(activity, parking_pre_price);

        parking_carmun = rootView.findViewById(R.id.parking_carmun);
        parking_carmun.setOnTouchListener(this);
        parking_carmun.setOnClickListener(this);
        InputUtil.hideTypewriting(activity, parking_carmun);

        parking_carmun.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //s:变化后的所有字符
                Log.i("Seachal:","变化:"+s+";"+start+";"+before+";"+count);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数
                Log.i("Seachal:","变化前:"+s+";"+start+";"+count+";"+after);
            }
            @Override
            public void afterTextChanged(Editable s) {
                //S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节数
                Log.i("Seachal:","变s.length()化后:"+s);

                if (  s.length() >=  (StringUtil.is_valid(distinguish_carmun)?distinguish_carmun.length():8)){
                    if(kvp!=null && kvp.mPopupWindow!=null)kvp.mPopupWindow.dismiss();
                }
            }
        });
    }

    //TODO 判断软键盘是否展开
    public boolean isShow(){

        return kvp!=null && kvp.mPopupWindow!=null && kvp.mPopupWindow.isShowing();
    }



    //TODO 展开软键盘
    public void charge_carNumber(){

        if(kvp == null){
            kvp = new KeyboardViewPager(activity, true);
        }
        kvp.setEt_carnumber(parking_carmun);

        kvp.showPopupWindow(parking_carmun);

        if (parking_carmun.getText().toString().contains("错误")){
            parking_carmun.setText("");
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (v.getId()==R.id.parking_carmun && !isShow()){
            charge_carNumber();

        }else if(v.getId()==R.id.parking_pre_price){

            if ( alertDialog!=null && alertDialog.isShowing()){
                return false;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Light_Dialog);

            alertDialog= builder.setTitle("请选择").setItems(string_pre_price, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which){
                        case 0: parking_pre_price.setText("0元");break;
                        case 1: parking_pre_price.setText("1元");break;
                        case 2: parking_pre_price.setText("3元");break;
                        case 3: parking_pre_price.setText("5元");break;
                        case 4: parking_pre_price.setText("10元");break;

                        default:break;
                    }

                }
            }).show();
        }

        return false;
        }


}
