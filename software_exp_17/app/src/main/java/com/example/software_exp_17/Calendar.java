package com.example.software_exp_17;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.text.SimpleDateFormat;

import static com.example.software_exp_17.MainActivity.db;
import static com.example.software_exp_17.MainActivity.helper;


public class Calendar extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {

    // 캘린더 화면 표시
    @BindView(R.id.calendarView)
    MaterialCalendarView materialCalendarView;

    @BindView(R.id.textView)
    TextView textView;

    static String searchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        ButterKnife.bind(this);

        materialCalendarView.setOnDateChangedListener(this);
        textView.setText("날짜를 선택하세요");

    }

    // 날짜 클릭 이벤트
    @Override
    public void onDateSelected(
            @NonNull MaterialCalendarView widget,
            @NonNull CalendarDay date,
            boolean selected) {

        // 캘린더 아래에 띄울 날짜 형식 지정 (yyyy-MM-dd)
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd");
        // 검색하기 위한 형식 지정 (yyyy월 MM일 dd일)
        SimpleDateFormat format2 = new SimpleDateFormat( "yyyy년 MM월 dd일");

        String selectDate = format1.format(date.getDate());
        // 검색을 위한 날짜 변수
        searchData = format2.format(date.getDate());

        textView.setText(selectDate);

        // 팝업 메시지
        new SimpleCalendarDialogFragment().show(getSupportFragmentManager(), "test-DB-data");
    }

    // 팝업창 내부
    public static class SimpleCalendarDialogFragment<selectDate> extends AppCompatDialogFragment{
        // SQL data를 담는 textView
        private TextView textView;
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog, null);
            textView = view.findViewById(R.id.dialogtxt);
            db = helper.getReadableDatabase();

            // SQL내의 데이터를 textview에 저장함
            String sql = "select * FROM tableName;";

            Cursor c = db.rawQuery(sql, null);
            String result_string ="empty";


            // 총 시간, 총 평균 계획 달성률을 저장하기 위함.
            String date = "";
            int achiveCnt = 0;
            int totalAchive = 0;
            int totalTime = 0;

            // 검색
            while(c.moveToNext()){
                if(c.getString(c.getColumnIndex("date")).equals(searchData)){
                    date = c.getString(c.getColumnIndex("date")); // 날짜 갱신 후
                    achiveCnt++;
                    totalAchive += c.getInt(c.getColumnIndex("achieve"));
                    totalTime += c.getInt((c.getColumnIndex("time")));
                    result_string = String.format("날짜 : %s\n달성률 : %s \n공부 시간: %d 시간 %d 분", date, totalAchive / achiveCnt, totalTime/60, totalTime%60);

                    while(c.moveToNext()){
                        if(!c.getString(c.getColumnIndex("date")).equals(searchData)){
                            break;
                        }
                        achiveCnt++;
                        totalAchive += c.getInt(c.getColumnIndex("achieve"));
                        totalTime += c.getInt((c.getColumnIndex("time")));

                        result_string = String.format("날짜 : %s\n달성률 : %s \n공부 시간: %d 시간 %d 분", date, totalAchive / achiveCnt, totalTime/60, totalTime%60);
                    }
                } // end of if-statement
            } // end of outer-loop

            textView.setText(result_string);

            return new AlertDialog.Builder(getActivity())
                    .setTitle("이날의 공부 내용")
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }
    }

    // 액션바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendarmenu, menu);
        getSupportActionBar().setTitle("캘린더");
        return true;
    }

    // 액션바 버튼 화면 전환
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.communityBtn) {
            Intent communityIndent = new Intent(this, Community.class);
            startActivity(communityIndent);
            return true;
        }
        if (id == R.id.planBtn) {
            Intent plannerIndent = new Intent(this, MainPlan.class);
            startActivity(plannerIndent);
            return true;
        }
        if (id == R.id.mapBtn) {
            Intent plannerIndent = new Intent(this, MapActivity.class);
            startActivity(plannerIndent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd");
        final String text = format1.format(date.getDate());
        getSupportActionBar().setTitle(text);
    }

}