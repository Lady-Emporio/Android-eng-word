package com.engru.al.Dream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class scrollToWordCursorAdapterForMove extends CursorAdapter {
    public boolean mass_add;
    public scrollToWordCursorAdapterForMove(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mass_add=false;
    }

    @Override public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.move_eng_for_scroll_item, parent, false);
    }
    @Override public void bindView(View view, Context context, Cursor cursor) {
        TextView eng = (TextView) view.findViewById(R.id.eng);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(EngWord.Table.ENG));
        if(mass_add){
            String status=cursor.getString(cursor.getColumnIndexOrThrow(ScrollEngWordsAdapter.Table.SCROLL_COUNT_AS));
            if(status==null){
                eng.setTextColor(Color.GREEN);
            }else{
                eng.setTextColor(Color.YELLOW);
            }
        }
        eng.setText(name);
    }
}
