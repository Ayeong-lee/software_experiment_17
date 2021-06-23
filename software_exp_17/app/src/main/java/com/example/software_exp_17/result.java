package com.example.software_exp_17;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.kakao.sdk.common.KakaoSdk;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.software_exp_17.DBHelper.DATABASE_VERSION;
import static com.example.software_exp_17.MainActivity.db;
import static com.example.software_exp_17.MainActivity.helper;
import static com.example.software_exp_17.MainPlan.items;

public class result extends AppCompatActivity {

    public String totalstudytime = stopwatch.studytime;
    public int time = MainPlan.progress;
    TextView timeText, planText;
    String date, timetxt;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
    static int count, Value, Value2, stTime, stPlan, stTopTime;
    static int restart = 0;
    static String stTopDate;
    Button comBtn, calBtn, staBtn;

    Cursor cursor;


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.resultmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_main:
                Intent intent = new Intent(this, MainPlan.class);
                items.clear();
                startActivity(intent);
                return true;
            case R.id.action_share:
                String context;

                context = "오늘의 공부 결과\n\n공부시간: ";
                context += totalstudytime; // 오늘 공부한 시간(00:00:00)
                context += "\n계획달성률: ";
                context += timetxt; // 계획 달성률
                context += "%";

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, context);
                sharingIntent.setPackage("com.kakao.talk");
                startActivity(sharingIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        KakaoSdk.init(this, "{NATIVE_APP_KEY}");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        ResultAdapter mAdapter = new ResultAdapter(items);
        ListView mListView = (ListView) findViewById(R.id.donelist);
        mListView.setAdapter(mAdapter);

        Intent intent = getIntent();
        timeText = (TextView) findViewById(R.id.time);
        timeText.setText("공부시간 : " + totalstudytime);
        planText = (TextView) findViewById(R.id.plan);
        timetxt = String.valueOf(time);
        planText.setText("계획달성률 : " + timetxt + " %");


        date = getTime();

        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("date", date); //날짜(yyyy년 mm월 nn일)
        values.put("achieve", time); //계획달성률 ****
        values.put("time", (stopwatch.totalhour * 60 + stopwatch.totalmin)); //공부시간
        //(result.stTopTime / 60) + " 시간" + (result.stTopTime % 60)

        db.insert("tableName", null, values);

        db = helper.getReadableDatabase();
        count = 0;
        Value2 = 0;
        Value = 0;

        cursor = db.rawQuery("select date, achieve, time from tableName", null);

        while(cursor.moveToNext()) {
            Value2 += cursor.getInt(cursor.getColumnIndex("achieve"));
            Value += cursor.getInt(cursor.getColumnIndex("time"));
            count++;
        }


        cursor.moveToFirst();

        stTopTime = cursor.getInt(cursor.getColumnIndex("time"));
        stTopDate = cursor.getString(cursor.getColumnIndex("date"));

        while(cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex("time")) > stTopTime) {
                stTopTime = cursor.getInt(cursor.getColumnIndex("time"));
                stTopDate = cursor.getString(cursor.getColumnIndex("date"));
            }
        }
        stTime = Value / count;
        stPlan = Value2 / count;

        comBtn = (Button) findViewById(R.id.goCommunity);
        calBtn = (Button) findViewById(R.id.goCalendar);
        staBtn = (Button) findViewById(R.id.goStatisitics);

        comBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Community.class);
                startActivity(intent);
            }
        });
        calBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Calendar.class);
                startActivity(intent);
            }
        });

        staBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Statistics.class);
                startActivity(intent);
            }
        });
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

}
