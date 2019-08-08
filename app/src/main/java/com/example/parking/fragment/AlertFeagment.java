package com.example.parking.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.JiguangBean;
import com.example.parking.bean.http.Report_orderlistBean;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.util.FileUtil;
import com.example.parking.util.StringUtil;


public class AlertFeagment extends BaseFragment {


    public static final String TAG = "AlertFeagment<警报拍照>";

    public ImageView alert_imageview;

    private TextView alert_fragment_carmun_edittext;

    private Button alert_photo;

    public String inimageImageString;
    public JiguangBean jiguangBean;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alert_fragment, container, false);

        alert_imageview = rootView.findViewById(R.id.alert_imageview);   // 全景本地地址
        alert_imageview.setOnClickListener(this);

        alert_photo = rootView.findViewById(R.id.alert_photo);
        alert_photo.setOnClickListener(this);

        alert_fragment_carmun_edittext = rootView.findViewById(R.id.alert_fragment_carmun_edittext);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);

        jiguangBean = (JiguangBean) getArguments().getSerializable("jiguangBean");
        alert_fragment_carmun_edittext.setText(jiguangBean.getDevDockName());

        //将jiguangBean里面照片放进ImageView里面
        if ( StringUtil.is_valid(inimageImageString = jiguangBean.getPhoto_path()) ){

            alert_imageview.setImageBitmap(BitmapFactory.decodeFile( jiguangBean.getPhoto_path()));
        }

        //取消未处理编辑
        Jiguang_DB.updata_Jiguang(activity.baseSQL_DB,null,jiguangBean.getDevDock());
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.alert_imageview:
                Log.i(TAG,inimageImageString);
                super.showPopupWindow(alert_imageview,"/storage/emulated/0/parking/4b04809a65f747898248c882373d4c4f.jpg");
                break;

            case R.id.alert_photo:
                Bundle bundle = new Bundle();
                bundle.putString( "joinType",TAG );
                bundle.putString("text","警告拍照");
                Message message2 = new Message();
                message2.what = MainActivity.alertPhoto;
                activity.handler.sendMessage(message2);
                break;

            default: break;
        }
    }



}
