package com.example.parking.fragment;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.http.SelectSubPlaceBean;
import com.example.parking.http.HttpManager2;
import com.example.parking.listView.PrrkingindexView;
import com.example.parking.util.JsonUtil;
import com.example.parking.util.JsonUtil2;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 功能说明: 停车Fragment
 * 日期:	2019年5月29日
 * 开发者:	KXD
 */
public class ParkingIndexFragment extends BaseFragment {



    public static final String TAG = "ParkingIndexFragment九宫格";
    public PrrkingindexView myAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.parkingindex_fragment, container, false);

        listView = rootView.findViewById(R.id.prrkingindex_listView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Map<String,String> param = new HashMap<String,String>(1);
        param.put("token",activity.userBean.getToken());
        HttpManager2.requestPost(Static_bean.selectSubPlace(),  param, this, "selectSubPlace");
    }

    //TODO 打开停车保存页面
    public void openParking( SelectSubPlaceBean.SelectSubPlaceData selectSubPlaceDate) {

        activity.openParking(selectSubPlaceDate);
    }

    private void selectSubPlace( String object ){

        try {

            if (object == null || object.length()<7) {
                toast_makeText("当前停车场没有车位");
                return;
            }

            // TODO  自定义配置器
            //第一步：准备数据源
            SelectSubPlaceBean selectSubPlace = JsonUtil2.fromJson(object, SelectSubPlaceBean.class);

            if (selectSubPlace == null || selectSubPlace.getData()==null || selectSubPlace.getCode()!=200) return;
            Log.i(TAG, selectSubPlace.toString());


            //修改title的车位数量
            int surplus = 0,already = 0;
            for( int i=0; i<selectSubPlace.getData().size(); i++ ){

                if (selectSubPlace.getData().get(i).getHavecar()==2){
                    already++;
                }else{
                    surplus++;
                }
            }

            Bundle bundle = new Bundle();
            bundle.putString( "joinType",TAG );
            bundle.putInt( "surplus",surplus );
            bundle.putInt( "already",already );
            Message message = new Message();
            message.what = MainActivity.updateTieleParking;
            message.setData(bundle);
            Handler handler = activity.handler;
            handler.sendMessage(message);


            //第二步：创建适配器,存入数据
            myAdapter = new PrrkingindexView(getActivity(), this, selectSubPlace.getData());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //第三步：绑定适配器
                    listView.setAdapter(myAdapter);
                }
            });
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        super.onResponsePOST(url, param, sign, object);


        Log.i(TAG,";object9="+object);

        Log.i(TAG,"url="+url+";param="+param+";sign="+sign+";object="+object);

        switch (sign){
            case "selectSubPlace": selectSubPlace( object );
                break;

             default:break;
        }


    }
}