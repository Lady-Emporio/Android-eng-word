package com.engru.al.engru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GameDragAndDropActivity extends Activity {
    public static final String KEYTOIDS="ids";
    ArrayList<String> ids;
    ArrayList<HashMap<String, String>> wordsList;
    FlexboxLayout flexLayout;
    LinearLayout layoutForEdit;
    TextView trueView;
    int position_Index;
    int maxPosition_Number;
    ArrayList<TextViewForGameDrapAndDrop>arrayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_drag_and_drop);
        trueView=(TextView)findViewById(R.id.trueView);
        flexLayout=(FlexboxLayout)findViewById(R.id.flexLayout);
        layoutForEdit=(LinearLayout)findViewById(R.id.layoutForEdit);
        position_Index=0;
        Intent intent = getIntent();
        String id_for_split= intent.getStringExtra(KEYTOIDS);
        ids=MyArrayIntegerStringListToString.backwardFromString(id_for_split);
        if(ids!=null && ids.size()!=0){
            String [] id_for_query=new String [ids.size()];
            for (int i=0;i!=ids.size();++i){
                id_for_query[i]=ids.get(i);
            }
            String sql="SELECT DISTINCT "
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ID + " , "
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.RU + " , "
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ENG + "  "
                    + " FROM " + ScrollEngWordsAdapter.Table.TABLE_NAME
                    + " JOIN " + EngWord.Table.TABLE_NAME + " ON "
                    + ScrollEngWordsAdapter.Table.TABLE_NAME + "." + ScrollEngWordsAdapter.Table.ENG_ID + " = " + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ID
                    + " WHERE ";
            for (int i=0;i!=ids.size();++i){
                sql+=" "+ScrollEngWordsAdapter.Table.TABLE_NAME + "." + ScrollEngWordsAdapter.Table.SCROLL_ID + " = ? ";
                if(i!=ids.size()-1){
                    sql+= " or ";
                }
            }
            sql+=" ORDER BY "+ EngWord.Table.TABLE_NAME + "." + EngWord.Table.ENG +"; ";
            Cursor cursor = BaseORM.db.rawQuery(sql, id_for_query );
            wordsList=new ArrayList<HashMap<String, String>>(cursor.getCount());
            while (cursor.moveToNext()){
                HashMap<String, String> word=new HashMap<String, String>();
                word.put("eng",cursor.getString(cursor.getColumnIndex(EngWord.Table.ENG)));
                word.put("ru",cursor.getString(cursor.getColumnIndex(EngWord.Table.RU)));
                wordsList.add(word);
            }
            cursor.close();
            maxPosition_Number=wordsList.size();
        }else{
            trueView.setText("Error with scrolls");
            return;
        }
        nextRound();

    }
    public static void openGameDragAndDropActivity(Context from,String array){
        Intent intent = new Intent( from,GameDragAndDropActivity.class );
        intent.putExtra(KEYTOIDS,array);
        from.startActivity(intent);
    }
    public void nextRound(){
        flexLayout.removeAllViewsInLayout();
        //layoutForEdit.removeAllViewsInLayout();
        ++position_Index;
        if(maxPosition_Number!=0 && position_Index==maxPosition_Number){
            position_Index=-1;
            nextRound();
            return;
        }
        trueView.setText(wordsList.get(position_Index).get("ru"));
        String engWord=wordsList.get(position_Index).get("eng");
        if(engWord==null){
            trueView.setText("Error with engWord==null");
            return;
        }
        ArrayList<String> singList=new  ArrayList<String>(engWord.length());
        for(int i=0;i!=engWord.length();++i){
            String nextSign=engWord.substring(i,i+1);
            singList.add(nextSign);
        }

        Collections.shuffle(singList);

        arrayTextView=new ArrayList<TextViewForGameDrapAndDrop>(singList.size());

        View.OnClickListener mylistenerButton = new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(arrayTextView.size()==0){
                    return;
                }
                ButtonForDrapAndDrop x=(ButtonForDrapAndDrop)v;
                TextViewForGameDrapAndDrop t=getMin();
                t.button=x;
                arrayTextView.remove(t);
                t.setText(x.getText().toString());
                x.setEnabled(false);
                x.setBackgroundColor(getResources().getColor(R.color.drapAndDropEnable));
                t.setEnabled(true);
                t.setBackgroundColor(getResources().getColor(R.color.drapAndDropUnavailable));
            }
        };
        View.OnClickListener mylistenerText = new View.OnClickListener() {
            @Override public void onClick(View v) {
                TextViewForGameDrapAndDrop t=(TextViewForGameDrapAndDrop)v;
                t.setText("_");
                arrayTextView.add(t);
                t.setEnabled(false);
                t.button.setEnabled(true);
                t.button.setBackgroundColor(getResources().getColor(R.color.default_button));
                t.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        };
        for (int i=0;i!=singList.size();++i){
            String currentText=singList.get(i);
            ButtonForDrapAndDrop nextButton=new ButtonForDrapAndDrop(this);
            nextButton.relatePosition=i;
            nextButton.setText(currentText);
            nextButton.setOnClickListener(mylistenerButton);
            nextButton.setBackgroundColor(getResources().getColor(R.color.default_button));
            flexLayout.addView(nextButton,new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));


            TextViewForGameDrapAndDrop t=new TextViewForGameDrapAndDrop(this);
            t.relatePosition=i;
            arrayTextView.add(t);
            t.currentText=currentText;
            t.setOnClickListener(mylistenerText);
            t.setText("_");
            t.setEnabled(false);
            t.setTextSize(23.0f);
            layoutForEdit.addView(t,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,0));
            t.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }


    }
    public TextViewForGameDrapAndDrop getMin(){
        TextViewForGameDrapAndDrop min=null;
        for(TextViewForGameDrapAndDrop i : arrayTextView){
            if(min==null || i.relatePosition<min.relatePosition){
                min=i;
            }
        }
        return min;
    }
}
