package com.example.parking.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.parking.R;



/**
 * 功能说明: 空白Fragment
 * 日期:	2019年8月8日
 * 开发者:	KXD
 */
public class WhiteFragment extends BaseFragment {


    public static final String TAG = "WhiteFragment<空白>";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.white_fragment, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);
    }

}