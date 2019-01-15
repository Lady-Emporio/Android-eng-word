package com.engru.al.Dream.Views;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.EditText;


//public class RowEdtView extends EditText {
public class RowEdtView extends AppCompatEditText {
    public String value;
    public RowEdtView(Context context){
        super(context);
    }
    public RowEdtView(Context context, String value){
        super(context);
        this.value=value;
        this.setText(value);
        this.setEnabled(false);
        this.setTextColor(Color.BLACK);
    }
    public RowEdtView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
