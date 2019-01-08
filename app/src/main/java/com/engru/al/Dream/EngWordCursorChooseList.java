package com.engru.al.Dream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class EngWordCursorChooseList extends CursorAdapter {
    public EngWordCursorChooseList(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.dinamic_list_item, parent, false);
    }
    //IF WORD IN LIST viewText.setBackgroundColor(Color.YELLOW)
    //IF NOT viewText.setBackgroundColor(Color.BLUE)
    @Override public void bindView(View view, Context context, Cursor cursor) {
        TextView viewText = (TextView) view.findViewById(R.id.viewFielnd);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(Scroll.Table.NAME));
        int idBindTable=cursor.getInt(cursor.getColumnIndexOrThrow(ScrollEngWordsAdapter.Table.ID_AS));
        if(idBindTable!=-1){
            viewText.setTextColor(Color.YELLOW);
        }else{
            viewText.setTextColor(Color.BLUE);
        }

        viewText.setText(name);
    }
}