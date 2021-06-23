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

public class ResultAdapter extends BaseAdapter {
    private ArrayList<ItemView> list;
    //  int value = 0;
    TextView mProgressText;

    ResultAdapter(ArrayList<ItemView> i){
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
        return convertView;
    }
}

