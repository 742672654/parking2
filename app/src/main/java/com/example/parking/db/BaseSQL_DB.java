package com.example.parking.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseSQL_DB extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteOpenHelper";


    // TODO: 下面是默认构造方法
    public BaseSQL_DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override   // TODO: 创建数据库
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG,"数据库");
        sqLiteDatabase.execSQL(static_SQL.CREATE_TEBLE);
    }

    @Override   // TODO: 数据库升级时调用
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        onCreate(sqLiteDatabase);
    }

}

class static_SQL{


    static final String CREATE_TEBLE = "CREATE TABLE localOrder(id text NOT NULL," +
            "photo1_path text,photo1_url text,photo2_path text,photo2_url text,time text,PRIMARY KEY (id))";

}