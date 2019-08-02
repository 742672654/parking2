package com.example.parking.listView;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.example.parking.R;
import com.example.parking.bean.http.ParkingSpaceData;
import com.example.parking.fragment.ParkingFragment;

import java.util.List;


@SuppressLint({ "NewApi", "ValidFragment" })
public class ParkingSpaceView extends Fragment {


    private static final String TAG = "ParkingSpaceView";

    private Activity activity;
    private View view;
    private ParkingFragment parkingFragment;

    private ListView listview;
    private PopupWindow popupWindow;



    public ParkingSpaceView( ParkingFragment parkingFragment,View view ){

        this.parkingFragment = parkingFragment;
        this.activity = parkingFragment.getActivity();
        this.view = view;
    }


    /**
     * 车位选择
     */
    @SuppressWarnings({ "deprecation", "unused" })
    public void showPopupWindow(final List<ParkingSpaceData> list ) {


        if(listview == null){
            listview = (ListView) LayoutInflater.from(activity).inflate(R.layout.account_droplist, null);
        }

        if(list.size() != 0){
            listview.setAdapter( new ParkingSpaceAdapter(activity, list) );
        }else{
            return;
        }

        if (popupWindow == null) {
            popupWindow = new PopupWindow(listview, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            DisplayMetrics dm = new DisplayMetrics();
            //获取屏幕信息
            ((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            //获取屏幕高度 dm.heightPixels;
            popupWindow.setWidth(view.getWidth());
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        view.getLocationOnScreen(new int[2]);
        popupWindow.showAsDropDown(view);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                Log.e(TAG,"选择车场点击的是："+position);


                parkingFragment.setListview_item_title( list.get(position) );
                popupWindow.dismiss();
            }
        });
    }


}
