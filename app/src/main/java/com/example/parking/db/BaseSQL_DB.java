package com.example.parking.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseSQL_DB extends SQLiteOpenHelper {

    private static final String TAG = "BaseSQL_DB";


    // TODO: 下面是默认构造方法
    public BaseSQL_DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override   // TODO: 创建数据库
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL(static_SQL.CREATE_TEBLE);
        sqLiteDatabase.execSQL(static_SQL.CREATE_TEBLE2);
    }


    // TODO: 数据库升级时调用
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG,"数据库升级才执行的");



        switch (oldVersion){
            case 2:upgrade2( sqLiteDatabase, oldVersion, newVersion );
            case 3:upgrade3( sqLiteDatabase, oldVersion, newVersion );
            case 4:upgrade4( sqLiteDatabase, oldVersion, newVersion );
            case 5:;
        }
    }

    private void upgrade2( SQLiteDatabase sqLiteDatabase,int oldVersion, int newVersion ){

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery("select count(*) as total from sqlite_master where type='table' and name = 'localOrder';", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();

                long total = cursor.getLong(cursor.getColumnIndex("total"));
                if (total == 0) { sqLiteDatabase.execSQL(static_SQL.CREATE_TEBLE); }
            } else {
                sqLiteDatabase.execSQL(static_SQL.CREATE_TEBLE);
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        } finally {
            if (null != cursor && !cursor.isClosed()) { cursor.close(); }
        }

        Cursor cursor2 = null;
        try {
            cursor2 = sqLiteDatabase.rawQuery("select count(*) as total from sqlite_master where type='table' and name = 'localJiguang';", null);
            if (cursor2.getCount()!=0){
                cursor2.moveToFirst();

                long total = cursor2.getLong(cursor2.getColumnIndex("total"));
                if (total==0){ sqLiteDatabase.execSQL(static_SQL.CREATE_TEBLE2); }
            }else{
                sqLiteDatabase.execSQL(static_SQL.CREATE_TEBLE2);
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        } finally {
            if (null != cursor2 && !cursor2.isClosed()) { cursor2.close(); }
        }
    }

    private void upgrade3( SQLiteDatabase sqLiteDatabase,int oldVersion, int newVersion ){


        try {

              sqLiteDatabase.execSQL("alter table localJiguang add column devDockName text;");

        } catch (Exception e) {
            Log.w(TAG, e);
        }

    }

    private void upgrade4( SQLiteDatabase sqLiteDatabase,int oldVersion, int newVersion ){


        try {

            sqLiteDatabase.execSQL("alter table localJiguang add column state INTEGER ;");
            sqLiteDatabase.execSQL("alter table localJiguang add column stateTime text;");
        } catch (Exception e) {
            Log.w(TAG, e);
        }

    }
}

class static_SQL{


    static final String CREATE_TEBLE = "CREATE TABLE localOrder(id text NOT NULL," +
            "photo1_path text,photo1_url text,photo2_path text,photo2_url text,time text,PRIMARY KEY (id))";

    static final String CREATE_TEBLE2 = "CREATE TABLE localJiguang(nOTIFICATION_ID text NOT NULL," +
            "pushTime text,pushTimeLong INTEGER,msgid text,devDock text,devDockName text,inOut text,msgType text,PRIMARY KEY (nOTIFICATION_ID))";
}