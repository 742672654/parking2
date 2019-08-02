package com.example.parking.ui.dataChoice;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerDialogCustom{


    //TODO 默认当前时间
    public static void showDateDialog(Context context, final DataChoiceReturn dataChoiceReturn,String title, final String sign) {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        // 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
        Date myDate = new Date();
        // 创建一个Date实例
        d.setTime(myDate);
        // 设置日历的时间，把一个新建Date实例myDate传入
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        //初始化默认日期year, month, day
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            /**
             * 点击确定后，在这个方法中获取年月日
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
             //   toast_makeText(year + "年" + monthOfYear + "月" + dayOfMonth + "日");

                dataChoiceReturn.getSpecificDate( new SpecificDateBean(year,monthOfYear+1,dayOfMonth),sign );
            }
        },year, month, day);
        datePickerDialog.setMessage(title);
        datePickerDialog.show();
    }

    //TODO 指定时间
    public static void showDateDialog(Context context,  final DataChoiceReturn dataChoiceReturn,String title, Date date,final String sign) {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        // 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
        // 创建一个Date实例
        d.setTime(date);
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            /**
             * 点击确定后，在这个方法中获取年月日
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                dataChoiceReturn.getSpecificDate( new SpecificDateBean(year,monthOfYear+1,dayOfMonth),sign );
            }
        },year, month, day);
        datePickerDialog.setMessage(title);
        datePickerDialog.show();
    }



}
