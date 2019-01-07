package com.engru.al.engru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ActivitySqlBrowser extends Activity {
    EditText inputSql;
    LinearLayout layoutToAddRow;
    Spinner chooseDefaultSql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_browser);
        inputSql=(EditText)findViewById(R.id.inputSql);
        layoutToAddRow=(LinearLayout)findViewById(R.id.layoutToAddRow);
        chooseDefaultSql=(Spinner)findViewById(R.id.chooseDefaultSql);
        chooseDefaultSql.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputSql.setText(chooseDefaultSql.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                inputSql.setText("");
            }
        });

    }
    public static void openSqlBrowserActivity(Context from){
        Intent intent = new Intent(from, ActivitySqlBrowser.class);
        from.startActivity(intent);
    }
    public void onClickExecute(View v){
        layoutToAddRow.removeAllViewsInLayout();
        try{
            String sqlText=inputSql.getText().toString();
            Cursor cursor=BaseORM.get_db().rawQuery(sqlText,null);

            EditText columnView=new EditText(this);
            columnView.setTextColor(Color.RED);
            StringBuffer columnRow=new StringBuffer();
            for(int i=0;i!=cursor.getColumnCount();++i){
                columnRow.append(cursor.getColumnName(i));
                columnRow.append(" | ");
            }
            columnRow.append("\n");
            columnView.setText(columnRow);
            layoutToAddRow.addView(columnView);


            while(cursor.moveToNext()){
                EditText nextRow=new EditText(this);
                StringBuffer result=new StringBuffer();
                for(int i=0;i!=cursor.getColumnCount();++i){
                    result.append(cursor.getString(i));
                    result.append(" | ");
                }
                result.append("\n");
                nextRow.setText(result);
                layoutToAddRow.addView(nextRow);
            }
            cursor.close();
        }catch (Exception e){
            EditText errorView=new EditText(this);
            errorView.setText(e.toString());
            layoutToAddRow.addView(errorView);
        }

    }
}
