package com.engru.al.Dream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class ListsListCursor extends CursorAdapter {
    public ListsListCursor(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_listrs_row, parent, false);
    }
    @Override public void bindView(View view, Context context, Cursor cursor) {
        TextView listName = (TextView) view.findViewById(R.id.listName);
        TextView wordsCountIn = (TextView) view.findViewById(R.id.wordsCountIn);
        CheckBox scrollCheck = (CheckBox) view.findViewById(R.id.scrollCheck);
        scrollCheck.setText(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(Scroll.Table.NAME));
        int count = cursor.getInt(cursor.getColumnIndexOrThrow(ScrollEngWordsAdapter.Table.SCROLL_COUNT_AS));

        listName.setText(name);
        if(MainGameActivity.game.chooseScroll.contains(scrollCheck.getText().toString())){
            listName.setTextColor(Color.BLUE);
        }else{
            listName.setTextColor(Color.BLACK);
        }
        wordsCountIn.setText(Integer.toString(count));

        scrollCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox button=(CheckBox)v;
                if(button.isChecked()){
                    MainGameActivity.game.needOpenScroll.add(button.getText().toString());
                }else{
                    MainGameActivity.game.needOpenScroll.remove(button.getText().toString());
                }
            }});
    }
}
