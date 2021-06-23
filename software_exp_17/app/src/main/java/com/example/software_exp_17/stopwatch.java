package com.example.software_exp_17;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

public class stopwatch extends AppCompatActivity {

    private Button mStartBtn, mStopBtn, mRecordBtn, mPauseBtn, mHomeBtn;
    private TextView mTimeTextView, mRecordTextView;
    private Thread timeThread = null;
    private Boolean isRunning = true;
    static public String studytime = "00:00:00";
    static int totalhour, totalmin;
    Message msg;
    SharedPreferences sp;
    int s, m;
    @SuppressLint("DefaultLocale") String result;
    int back = 0;

    public void save(int i, String s){
        sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("save", i);
        editor.putString("save2", s);
        editor.apply(); }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.stopwatchmenu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_main2:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        back = 1;
        if(m != 0) {
            save((m+s), result);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#4ea1d3"));
        }

        SharedPreferences sf = getSharedPreferences("sp", MODE_PRIVATE);

        mStartBtn = (Button) findViewById(R.id.btn_start);
        mStopBtn = (Button) findViewById(R.id.btn_stop);
        //mRecordBtn = (Button) findViewById(R.id.btn_record);
        mPauseBtn = (Button) findViewById(R.id.btn_pause);
        mTimeTextView = (TextView) findViewById(R.id.timeView);
        //mRecordTextView = (TextView) findViewById(R.id.recordView);

        mTimeTextView.setText(sf.getString("save2", "00:00:00"));

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mPauseBtn.setVisibility(View.VISIBLE);
                mStopBtn.setVisibility(View.VISIBLE);
                //    mRecordBtn.setVisibility(View.VISIBLE);

                s = sf.getInt("save", 0);
                isRunning = true;
                mPauseBtn.setText("일시정지");
                timeThread = new Thread(new timeThread());
                timeThread.start();
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertBuilder =
                        new AlertDialog.Builder(stopwatch.this);
                //alert의 Message 세팅
                myAlertBuilder.setMessage("공부를 종료하시겠습니까?");
                //버튼
                myAlertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),studytime + "\n공부를 종료합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(getApplicationContext(), result.class);
                        startActivity(intent2);


                        v.setVisibility(View.GONE);
                        //mRecordBtn.setVisibility(View.GONE);
                        mStartBtn.setVisibility(View.VISIBLE);
                        mPauseBtn.setVisibility(View.GONE);
                        //mRecordTextView.setText("");
                        mTimeTextView.setText("00:00:00");
                        timeThread.interrupt();
                    }
                });

                myAlertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //취소 버튼 눌렀을 경우

                    }
                });

                //Alert 생성하고 보여주는 메소드
                myAlertBuilder.show();
                isRunning = false;
                mPauseBtn.setText("시작");
            }
        });

        /*mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordTextView.setText(mRecordTextView.getText() + mTimeTextView.getText().toString() + "\n");
            }
        });*/

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;
                if (isRunning) {
                    mPauseBtn.setText("일시정지");
                } else {
                    mPauseBtn.setText("시작");
                }
            }
        });



    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = (msg.arg1+s) % 100;
            int sec = ((msg.arg1+s) / 100) % 60;
            int min = ((msg.arg1+s) / 100) / 60;
            int hour = ((msg.arg1+s) / 100) / 360;
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            result = String.format("%02d:%02d:%02d", hour, min, sec);
            totalhour = hour;
            totalmin = min;
            studytime = result;
            mTimeTextView.setText(result);
        }
    };

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤

                    msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);
                    m = msg.arg1;

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                mTimeTextView.setText("");
                                mTimeTextView.setText("00:00:00");
                            }
                        });
                        return; // 인터럽트 받을 경우 return
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        if(back == 0) {
            SharedPreferences pref = getSharedPreferences("sp", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
        }
        super.onStop();
    }



    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
