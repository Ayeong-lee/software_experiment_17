package com.example.software_exp_17;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;

import static com.example.software_exp_17.MainPlan.items;
import static com.example.software_exp_17.MainPlan.mProgressBar;
import static com.example.software_exp_17.MainPlan.mProgressText;
import static com.example.software_exp_17.MainPlan.value;
import static com.example.software_exp_17.MainPlan.progress;
import static com.example.software_exp_17.MainPlan.count;

public class ItemAdapter extends BaseAdapter {
    private ArrayList<ItemView> list;
    ItemAdapter(ArrayList<ItemView> i){
        list = i;
    }


    @Override
    public int getCount(){
        return list.size();
    }
    @Override
    public Object getItem(int position){
        return list.get(position);
    }
    @Override
    public long getItemId(int position){
        return 0;
    }
    public boolean isChecked(int position){
        return list.get(position).checked;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemview, parent, false);
        }
        TextView td_item = convertView.findViewById(R.id.todo);
        CheckBox checkBox = convertView.findViewById(R.id.mainCheckbox);
        checkBox.setChecked(list.get(position).checked);
        td_item.setText(list.get(position).todo);
        LinearLayout listitem = (LinearLayout)convertView.findViewById(R.id.linearLayout);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newState = !list.get(position).isChecked();
                list.get(position).checked = newState;

                // 체크박스가 선택될 때마다 이벤트 발생하도록
                int nonChecked = 0;
                for (ItemView w : items){
                    if(!w.isChecked()){
                        nonChecked++;
                    }
                }

                value = count - nonChecked;
                mProgressText.setText("계획달성 " + value + "/" + count);
                progress = (int)((100*value)/count);
                mProgressBar.setProgress(progress);
            }
        });

        listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context) // TestActivity 부분에는 현재 Activity의 이름 입력.
                        .setMessage("삭제하시겠습니까?")     // 제목 부분 (직접 작성)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                            public void onClick(DialogInterface dialog, int which){
                                list.remove(list.get(position));
                                notifyDataSetChanged();
                                count--;

                                int nonChecked = 0;
                                for (ItemView w : items) {
                                    if (!w.isChecked()) {
                                        nonChecked++;
                                    }
                                }
                                value = count - nonChecked;
                                mProgressText.setText("계획달성 " + value + "/" + count);
                                if(count == 0) {
                                    progress = 0;
                                }else{
                                    progress = (int)((100*value)/count);
                                }

                                mProgressBar.setProgress(progress);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
                            public void onClick(DialogInterface dialog, int which){

                            }
                        })
                        .show();
            }
        });

        checkBox.setChecked(isChecked(position));
        return convertView;
    }
}