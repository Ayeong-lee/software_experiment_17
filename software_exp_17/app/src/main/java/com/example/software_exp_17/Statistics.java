package com.example.software_exp_17;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.software_exp_17.DBHelper.DATABASE_VERSION;
import static com.example.software_exp_17.MainActivity.helper;
import static com.example.software_exp_17.MainActivity.db;
import static com.example.software_exp_17.MainPlan.items;


public class Statistics extends AppCompatActivity{

    private LineChart lineChart;

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.statisticmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_main3:
                Intent intent = new Intent(getApplicationContext(), MainPlan.class);
                items.clear();
                startActivity(intent);
                return true;
            case R.id.action_result:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        TextView stPlan = (TextView) findViewById(R.id.stPlan);
        TextView stTime = (TextView) findViewById(R.id.stTime);
        TextView stTopTime = (TextView) findViewById(R.id.stTopTime);
        stTime.setText("평균 공부 시간:" + (result.stTime / 60) + " 시간" + (result.stTime % 60) +" 분");
        stPlan.setText("평균 계획 달성률: "+ result.stPlan + "%");
        stTopTime.setText("최고 공부 시간: \n"+ result.stTopDate + "\n" + (result.stTopTime / 60) + " 시간" + (result.stTopTime % 60) +" 분");


        lineChart = (LineChart)findViewById(R.id.chart);

        db = helper.getReadableDatabase();

        List<Entry> entries = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();
        List<Entry> entries4 = new ArrayList<>();

        int x = 1, y = 0, y1 = 0, y2 = 0;
        int count = 1, y3 = 0, y4 = 0;

        Cursor cursor = db.rawQuery("select time, date, achieve from tableName", null);
        String beforedate = "임시";

        cursor.moveToLast();
        y2 = cursor.getInt(cursor.getColumnIndex("time"));
        y3 = cursor.getInt(cursor.getColumnIndex("achieve"));
        beforedate = cursor.getString(cursor.getColumnIndex("date"));


        while(cursor.moveToPrevious() && x < 6){
            y1 = cursor.getInt(cursor.getColumnIndex("time"));
            y4 = cursor.getInt(cursor.getColumnIndex("achieve"));
            if (cursor.getString(cursor.getColumnIndex("date")).equals(beforedate)) {
                y2 += y1;
                y3 += y4;
                count++;
                if(x == 5){
                    entries.add(new Entry(x, y2));
                    entries3.add(new Entry(x, y3 / count));
                    count = 1;
                }
            }else{
                entries.add(new Entry(x, y2));
                entries3.add(new Entry(x, y3 / count));
                count = 1;
                y2 = y1;
                y3 = y4;
                x++;
            }
            beforedate = cursor.getString(cursor.getColumnIndex("date"));
        }

        entries.add(new Entry(x, y2));
        entries3.add(new Entry(x, y3 / count));
        x++;

        while(x < 6){
            entries.add(new Entry(x, 0));
            entries3.add(new Entry(x, 0));
            x++;
        }


        int a = 4;
        for(int i = 1; i < 6; i++){
            entries2.add(new Entry(i, entries.get(a).getY()));
            entries4.add(new Entry(i, entries3.get(a).getY()));
//            entries4.add(new Entry(i, a));
            a--;
        }




        LineDataSet lineDataSet = new LineDataSet(entries2, "공부시간(분)");
        LineDataSet lineDataSet1 = new LineDataSet(entries4, "계획달성률(%)");
        lineDataSet.setLineWidth(6);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        lineDataSet1.setLineWidth(6);
        lineDataSet1.setCircleRadius(6);
        lineDataSet1.setCircleColor(Color.parseColor("#FFFFCCFF"));
        lineDataSet1.setCircleColorHole(Color.RED);
        lineDataSet1.setColor(Color.parseColor("#FFFFCCFF"));
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawHorizontalHighlightIndicator(false);
        lineDataSet1.setDrawHighlightIndicators(false);
        lineDataSet1.setDrawValues(false);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        lineData.addDataSet(lineDataSet1);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

        cursor.close();

    }
}
