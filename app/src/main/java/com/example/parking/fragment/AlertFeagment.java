package com.example.parking.fragment;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.JiguangBean;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.util.StringUtil;


public class AlertFeagment extends BaseFragment {


    public static final String TAG = "AlertFeagment<警报拍照>";

    public ImageView alert_imageview;

    private TextView alert_fragment_carmun_edittext;

    private Button alert_photo;

    public String inimageImageString = "";
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

        JiguangBean jiguangBean0 = (JiguangBean) getArguments().getSerializable("jiguangBean");

        jiguangBean = Jiguang_DB.query_Jiguang(activity.baseSQL_DB,jiguangBean0.getnOTIFICATION_ID());

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

                Log.i(TAG,"------"+jiguangBean.getPhoto_path());

                if (StringUtil.is_valid(jiguangBean.getPhoto_path())){
                    super.showPopupWindow(alert_imageview,jiguangBean.getPhoto_path());
                }
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
