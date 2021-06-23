package com.example.software_exp_17;



public class ItemView {
    boolean checked;
    String todo;
    ItemView(boolean b, String t){
        checked = b;
        todo = t;
    }
    public boolean isChecked(){
        return checked;
    }

}
