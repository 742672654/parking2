package com.example.parking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.parking.R;


public class AlertFeagment extends BaseFragment {


    public static final String TAG = "AlertFeagment<警报拍照>";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alert_fragment, container, false);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);
    }




}
