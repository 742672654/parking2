package com.example.parking.db;


import android.database.Cursor;
import android.util.Log;

import com.example.parking.bean.OrderDbBean;


public class Order_DB {

    private static final String TAG = "Order_DB";

    public static OrderDbBean query_Order(BaseSQL_DB baseSQL_DB, String id) {

        Cursor cursor = null;
        try {

            String buf = new StringBuffer(" select * from localOrder where id = '").append(id).append("'; ").toString();

            Log.i(TAG,buf.toString());


            cursor = baseSQL_DB.getWritableDatabase().rawQuery(buf, null);
            if (cursor.getCount()==0){ return null; }
            cursor.moveToFirst();
            OrderDbBean orderBean = new OrderDbBean();
            if (cursor.getColumnIndex("id")!=-1)orderBean.setId(cursor.getString(cursor.getColumnIndex("id")));
            if (cursor.getColumnIndex("photo1_path")!=-1)   orderBean.setPhoto1_path(cursor.getString(cursor.getColumnIndex("photo1_path")));
            if (cursor.getColumnIndex("photo1_url")!=-1)   orderBean.setPhoto1_url(cursor.getString(cursor.getColumnIndex("photo1_url")));
            if (cursor.getColumnIndex("photo2_path")!=-1)   orderBean.setPhoto2_path(cursor.getString(cursor.getColumnIndex("photo2_path")));
            if (cursor.getColumnIndex("photo2_url")!=-1)   orderBean.setPhoto2_url(cursor.getString(cursor.getColumnIndex("photo2_url")));
            if (cursor.getColumnIndex("time")!=-1)   orderBean.setTime(cursor.getString(cursor.getColumnIndex("time")));
            Log.i(TAG,orderBean.toString()+""+cursor.getString(cursor.getColumnIndex("id")));
            return orderBean;
        } catch (Exception e) {
           Log.w(TAG,e);
            return null;
        } finally {
            try {
                if (cursor != null){ cursor.close(); }
            } catch (Exception e) {
            }
        }
    }

    public static void insert_in_Order(BaseSQL_DB baseSQL_DB, OrderDbBean orderBean) {


        StringBuffer buf = new StringBuffer("INSERT INTO localOrder(id, photo1_path, photo1_url, photo2_path, photo2_url, time) VALUES ");
        buf.append("('").append(orderBean.getId()).append("',");
        buf.append("'").append(orderBean.getPhoto1_path()).append("',");
        buf.append("'").append(orderBean.getPhoto1_url()).append("',");
        buf.append("'").append(orderBean.getPhoto2_path()).append("',");
        buf.append("'").append(orderBean.getPhoto2_url()).append("',");
        buf.append("'").append(orderBean.getTime()).append("')");

        baseSQL_DB.getWritableDatabase().execSQL(buf.toString());
        Log.i(TAG,orderBean.toString() + "--------"+  buf.toString());
    }





}
