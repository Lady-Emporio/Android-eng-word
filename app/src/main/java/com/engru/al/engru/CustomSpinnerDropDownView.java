package com.engru.al.engru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerDropDownView extends ArrayAdapter<String> {
    Context context;
    public CustomSpinnerDropDownView(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.context=context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        //LayoutInflater inflater=getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View row=inflater.inflate(R.layout.custom_spinner_drop_down_view, parent, false);
        TextView label=(TextView)row.findViewById(R.id.name);
        String item = (String) this.getItem(position);
        label.setText(item);

        return row;
    }
}