package com.example.software_exp_17;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainPlan extends AppCompatActivity {
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    public static TextView mTextView, go1, go2;
    RealtimeBlurView blur;
    public static ListView mListView;
    EditText mEditText;
    public static TextView mProgressText;
    public static ProgressBar mProgressBar;
    Button inputButton, mStartbtn, mCalendarbtn, mCommunitybtn, mMenubtn;

    static final ArrayList<ItemView> items = new ArrayList<ItemView>();
    static int count = 0;
    static int value = 0;
    int touch = 0;
    static int progress;
    int first = 0;

    static String search = "createdAt";
    static int searchInt = 0;

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainplan);
        //bind view

        mTextView = (TextView) findViewById(R.id.today_date);
        mTextView.setText(getTime());


        ItemAdapter mAdapter = new ItemAdapter(items);
        mListView = (ListView) findViewById(R.id.todoList);
        mListView.setAdapter(mAdapter);
        inputButton = (Button) findViewById(R.id.todoButton);
        mEditText = (EditText) findViewById(R.id.todoInput);
        mStartbtn = (Button) findViewById(R.id.studyStartButton);
        mCalendarbtn = (Button) findViewById(R.id.goCalendar2);
        mCommunitybtn = (Button) findViewById(R.id.goCommunity2);

        value = 0;
        mEditText.setText("");

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressText = (TextView) findViewById(R.id.progress_text);

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.length() == 0) {
                    Toast.makeText(getApplicationContext(), "계획을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String a = mEditText.getText().toString();
                    ItemView item = new ItemView(false, a);
                    items.add(item);
                    mAdapter.notifyDataSetChanged();
                    count = items.size();
                    mEditText.setText("");
                }

                mProgressText.setText("계획달성 " + value + "/" + count);

                if(count == 0){
                    progress = 0;
                }else{
                    progress = (int) ((100 * value) / count);
                }
                mProgressBar.setProgress(progress);
            }


        });

        // start 버튼을 입력했을 때
        mStartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.size() == 0){
                    Toast.makeText(getApplicationContext(), "계획을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainPlan.this, stopwatch.class);
                    startActivity(intent);
                }
            }
        });

        // 커뮤니티 이동
        mCommunitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPlan.this, Community.class);
                startActivity(intent);
            }
        });

        // 캘린더 이동
        mCalendarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPlan.this, Calendar.class);
                startActivity(intent);
            }
        });

    }


    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}