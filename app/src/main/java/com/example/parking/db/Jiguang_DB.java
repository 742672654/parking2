package com.example.parking.db;


import android.database.Cursor;
import android.util.Log;
import com.example.parking.bean.JiguangBean;
import com.example.parking.util.StringUtil;
import com.example.parking.util.TimeUtil;
import java.util.ArrayList;
import java.util.List;


public class Jiguang_DB {

    private static final String TAG = "Jiguang_DB";


    public static boolean insert_in_Jiguang(BaseSQL_DB baseSQL_DB, JiguangBean jiguangBean) {

        try{


        StringBuffer buf = new StringBuffer("INSERT INTO localJiguang(nOTIFICATION_ID, pushTime, pushTimeLong, msgid, devDock, devDockName, inOut, msgType) VALUES ");
        buf.append("('").append(jiguangBean.getnOTIFICATION_ID()).append("',");
        buf.append("'").append(jiguangBean.getPushTime()).append("',");
        buf.append("").append(jiguangBean.getPushTimeLong()).append(",");
        buf.append("'").append(jiguangBean.getMsgid()).append("',");
        buf.append("'").append(jiguangBean.getDevDock()).append("',");
        buf.append("'").append(jiguangBean.getDevDockName()).append("',");
        buf.append("'").append(jiguangBean.getInOut()).append("',");
        buf.append("'").append(jiguangBean.getMsgType()).append("')");
            Log.i(TAG,jiguangBean.toString() + "--------"+  buf.toString());

        baseSQL_DB.getWritableDatabase().execSQL(buf.toString());

        return true;
        }catch (Exception e){
            Log.w(TAG,e);
            return false;
        }
    }

    public static JiguangBean query_Jiguang(BaseSQL_DB baseSQL_DB, String nOTIFICATION_ID) {

        Cursor cursor = null;
        try {

            String buf = new StringBuffer(" select * from localJiguang where nOTIFICATION_ID = '").append(nOTIFICATION_ID).append("'; ").toString();
            Log.i(TAG,buf);

            cursor = baseSQL_DB.getWritableDatabase().rawQuery(buf, null);
            if (cursor.getCount()==0){ return null; }
            cursor.moveToFirst();
            JiguangBean jiguangBean = new JiguangBean();
            if (cursor.getColumnIndex("nOTIFICATION_ID")!=-1)   jiguangBean.setnOTIFICATION_ID(cursor.getString(cursor.getColumnIndex("nOTIFICATION_ID")));
            if (cursor.getColumnIndex("pushTime")!=-1)   jiguangBean.setPushTime(cursor.getString(cursor.getColumnIndex("pushTime")));
            if (cursor.getColumnIndex("pushTimeLong")!=-1)   jiguangBean.setPushTimeLong(cursor.getLong(cursor.getColumnIndex("pushTimeLong")));
            if (cursor.getColumnIndex("msgid")!=-1)   jiguangBean.setMsgid(cursor.getString(cursor.getColumnIndex("msgid")));
            if (cursor.getColumnIndex("devDock")!=-1)   jiguangBean.setDevDock(cursor.getString(cursor.getColumnIndex("devDock")));
            if (cursor.getColumnIndex("devDockName")!=-1)   jiguangBean.setDevDockName(cursor.getString(cursor.getColumnIndex("devDockName")));
            if (cursor.getColumnIndex("inOut")!=-1)   jiguangBean.setInOut(cursor.getString(cursor.getColumnIndex("inOut")));
            if (cursor.getColumnIndex("msgType")!=-1)   jiguangBean.setMsgType(cursor.getString(cursor.getColumnIndex("msgType")));

            if (cursor.getColumnIndex("photo_path")!=-1)   jiguangBean.setPhoto_path(cursor.getString(cursor.getColumnIndex("photo_path")));

            if (cursor.getColumnIndex("state")!=-1)   jiguangBean.setState(cursor.getInt(cursor.getColumnIndex("state")));
            if (cursor.getColumnIndex("stateTime")!=-1)   jiguangBean.setStateTime(cursor.getString(cursor.getColumnIndex("stateTime")));


            Log.i(TAG,jiguangBean.toString()+""+cursor.getString(cursor.getColumnIndex("nOTIFICATION_ID")));
            return jiguangBean;
        } catch (Exception e) {
            Log.w(TAG,e);
            return null;
        } finally {
            try { if (cursor != null){ cursor.close(); } } catch (Exception e) { }
        }
    }

    //TODO 查询两天的
    public static List<JiguangBean> query_Jiguang_Yesterday(BaseSQL_DB baseSQL_DB ) {

        Cursor cursor = null;
        try {

            //20190803145127
            String yesterdayString = String.valueOf(TimeUtil.getDateTimeLong() - 1000000).substring(0,8)+"000000";

            long yesterdayLong =TimeUtil.StringToDate(yesterdayString,"yyyyMMddHHmmss").getTime();

            String buf = " select * from localJiguang where pushTimeLong >" + yesterdayLong + "    order by  pushTimeLong desc; ";
            Log.i(TAG,buf);

            cursor = baseSQL_DB.getWritableDatabase().rawQuery(buf, null);
            if (cursor.getCount()==0){ return null; }

            List<JiguangBean> list = new ArrayList<JiguangBean>(cursor.getCount());


            for (int i =0;i<cursor.getCount();i++){

                cursor.moveToNext();

                Log.i(TAG,"devDockName"+cursor.getString(cursor.getColumnIndex("devDockName")));

                JiguangBean jiguangBean = new JiguangBean();
                if (cursor.getColumnIndex("nOTIFICATION_ID")!=-1)jiguangBean.setnOTIFICATION_ID(cursor.getString(cursor.getColumnIndex("nOTIFICATION_ID")));
                if (cursor.getColumnIndex("pushTime")!=-1)jiguangBean.setPushTime(cursor.getString(cursor.getColumnIndex("pushTime")));
                if (cursor.getColumnIndex("pushTimeLong")!=-1)jiguangBean.setPushTimeLong(cursor.getLong(cursor.getColumnIndex("pushTimeLong")));
                if (cursor.getColumnIndex("msgid")!=-1)jiguangBean.setMsgid(cursor.getString(cursor.getColumnIndex("msgid")));
                if (cursor.getColumnIndex("devDock")!=-1)jiguangBean.setDevDock(cursor.getString(cursor.getColumnIndex("devDock")));
                if (cursor.getColumnIndex("devDockName")!=-1)jiguangBean.setDevDockName(cursor.getString(cursor.getColumnIndex("devDockName")));
                if (cursor.getColumnIndex("inOut")!=-1)jiguangBean.setInOut(cursor.getString(cursor.getColumnIndex("inOut")));
                if (cursor.getColumnIndex("msgType")!=-1)jiguangBean.setMsgType(cursor.getString(cursor.getColumnIndex("msgType")));

                if (cursor.getColumnIndex("photo_path")!=-1)   jiguangBean.setPhoto_path(cursor.getString(cursor.getColumnIndex("photo_path")));

                if (cursor.getColumnIndex("state")!=-1)jiguangBean.setState(cursor.getInt(cursor.getColumnIndex("state")));
                if (cursor.getColumnIndex("stateTime")!=-1)jiguangBean.setStateTime(cursor.getString(cursor.getColumnIndex("stateTime")));

                Log.i(TAG,jiguangBean.toString());
                list.add(jiguangBean);
            }

            return list;
        } catch (Exception e) {
            Log.w(TAG,e);
            return null;
        } finally {
            try {
                if (cursor != null){ cursor.close(); }
            } catch (Exception e) {
                Log.w(TAG,e);
            }
        }
    }

    //TODO 去除<未处理>标识，根据id或是车位id
    public static boolean updata_Jiguang(BaseSQL_DB baseSQL_DB, String nOTIFICATION_ID, String subId) {

        try {

            StringBuffer buf = new StringBuffer();
                buf.append("update localJiguang");
                buf.append(" set state = 1 ,stateTime = '").append(TimeUtil.getDateTime()).append("'");
                buf.append(" where  1 = 1 " );

            if (StringUtil.is_valid(nOTIFICATION_ID)) {
                buf.append(" and nOTIFICATION_ID = '").append(nOTIFICATION_ID).append("'");
            } else if (StringUtil.is_valid(subId)) {

                String yesterdayString = String.valueOf(TimeUtil.getDateTimeLong() - 1000000).substring(0,8)+"000000";
                long yesterdayLong =TimeUtil.StringToDate(yesterdayString,"yyyyMMddHHmmss").getTime();

                buf.append(" and devDock = '").append(subId).append("' ");
                buf.append(" and pushTimeLong > ").append( yesterdayLong );
            }

            Log.i(TAG, "nOTIFICATION_ID=" + nOTIFICATION_ID + "--------" + buf.toString());
            baseSQL_DB.getWritableDatabase().execSQL(buf.toString());

            return true;
        } catch (Exception e) {
            Log.w(TAG, e);
            return false;
        }
    }

    //TODO 根据id添加警告拍照照片地址
    public static boolean updata_Jinggao(BaseSQL_DB baseSQL_DB, String nOTIFICATION_ID, String photo_path) {

        try {

            StringBuffer buf = new StringBuffer();
            buf.append("update localJiguang");
            buf.append(" set photo_path = '").append(photo_path).append("'");
            buf.append(" where nOTIFICATION_ID = '").append(nOTIFICATION_ID).append("'");

            Log.i(TAG, "nOTIFICATION_ID=" + nOTIFICATION_ID + "--------" + buf.toString());
            baseSQL_DB.getWritableDatabase().execSQL(buf.toString());

            return true;
        } catch (Exception e) {
            Log.w(TAG, e);
            return false;
        }
    }



}
