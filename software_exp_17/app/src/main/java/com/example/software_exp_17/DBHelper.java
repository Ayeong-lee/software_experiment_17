package com.example.software_exp_17;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "planRes.db";
    static final int DATABASE_VERSION = 1;

    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
/* 칼럼은 날짜 / 계획 달성률 / 공부시간으로 설정한다. */

    @Override
    public void onCreate(SQLiteDatabase db) {
        String resSQL = "create table if not exists tableName" + "(_id integer primary key autoincrement,"
               + "date text," + "achieve integer," + "time integer);";
        db.execSQL(resSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS tableName");
            onCreate(db);
        }
    }
}
