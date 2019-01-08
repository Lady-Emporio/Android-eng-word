package com.engru.al.Dream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GameDragAndDropActivity extends Activity {
    enum ROUND{ROUND_RU_COLLECT_ENG,ROUND_ENGVALUE_COLLECT_ENG}
    public static final String KEYTOIDS="ids";
    ArrayList<String> ids;
    ArrayList<HashMap<String, String>> wordsList;
    HashMap<Integer,TextViewForGameDrapAndDrop>forFinalGumWord;
    FlexboxLayout flexLayout;
    FlexboxLayout layoutForEdit;
    TextView trueView;
    int position_Index;
    int maxPosition_Number;
    String trueWord;
    ArrayList<TextViewForGameDrapAndDrop>arrayTextView;
    boolean needNewRound;
    boolean isSpaceBetweenInputWord;
    float start_x;
    float start_y;
    ROUND round;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_drag_and_drop);
        trueView=(TextView)findViewById(R.id.trueView);
        flexLayout=(FlexboxLayout)findViewById(R.id.flexLayout);
        layoutForEdit=(FlexboxLayout)findViewById(R.id.layoutForEdit);
        position_Index=0;
        isSpaceBetweenInputWord=false;
        needNewRound=true;
        round=ROUND.ROUND_RU_COLLECT_ENG;
        forFinalGumWord=new HashMap<Integer,TextViewForGameDrapAndDrop>();
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
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ENG + " , "
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.EXAMPLE + " , "
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ENG_VALUE + "  "
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
            Cursor cursor = BaseORM.get_db().rawQuery(sql, id_for_query );
            wordsList=new ArrayList<HashMap<String, String>>(cursor.getCount());
            while (cursor.moveToNext()){
                HashMap<String, String> word=new HashMap<String, String>();
                word.put("eng",cursor.getString(cursor.getColumnIndex(EngWord.Table.ENG)));
                word.put("ru",cursor.getString(cursor.getColumnIndex(EngWord.Table.RU)));
                word.put("example",cursor.getString(cursor.getColumnIndex(EngWord.Table.EXAMPLE)));
                word.put("eng_value",cursor.getString(cursor.getColumnIndex(EngWord.Table.ENG_VALUE)));
                wordsList.add(word);
            }
            cursor.close();
            maxPosition_Number=wordsList.size();
            Collections.shuffle(wordsList);
        }else{
            trueView.setText("Error with scrolls");
            return;
        }
        layoutForEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(needNewRound){
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    nextRound();
                }
            }
        });

        trueView.setOnTouchListener (new View.OnTouchListener(){
            @Override public boolean onTouch(View v, MotionEvent event) {
                float  x = event.getX();
                float  y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        start_x=x;
                        start_y=y;
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        float move_x=start_x-x;
                        float move_y=start_y-y;
                        if( (move_x> 300 || move_x < -300) && move_y>-150 && move_y<150  ){
                            isSpaceBetweenInputWord=!isSpaceBetweenInputWord;
                            nextRound();
                            return true;
                        }else if( (move_y> 300 || move_y < -300) && move_x>-150 && move_x<150  ){
                            switch (round){
                                case ROUND_RU_COLLECT_ENG:
                                    round=ROUND.ROUND_ENGVALUE_COLLECT_ENG;
                                    break;
                                case ROUND_ENGVALUE_COLLECT_ENG:
                                    round=ROUND.ROUND_RU_COLLECT_ENG;
                                    break;
                            }
                            thisRound();
                        }
                        break;
                }
                return true;
            }});

        nextRound();

    }
    public static void openGameDragAndDropActivity(Context from,String array){
        Intent intent = new Intent( from,GameDragAndDropActivity.class );
        intent.putExtra(KEYTOIDS,array);
        from.startActivity(intent);
    }
    public void nextRound(){
        ++position_Index;
        if(maxPosition_Number!=0 && position_Index==maxPosition_Number){
            position_Index=-1;
            Collections.shuffle(wordsList);
            nextRound();
            return;
        }
        needNewRound=false;

        flexLayout.removeAllViewsInLayout();
        layoutForEdit.removeAllViewsInLayout();
        layoutForEdit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        if(round== ROUND.ROUND_RU_COLLECT_ENG){
            trueView.setText(wordsList.get(position_Index).get("ru"));
            trueWord =wordsList.get(position_Index).get("eng");
        }else if(round== ROUND.ROUND_ENGVALUE_COLLECT_ENG){
            trueView.setText(wordsList.get(position_Index).get("eng_value"));
            trueWord =wordsList.get(position_Index).get("eng");
        }

        if(trueWord==null){
            trueView.setText("Error with engWord==null");
            return;
        }
        ArrayList<String> singList=new  ArrayList<String>(trueWord.length());
        for(int i=0;i!=trueWord.length();++i){
            String nextSign=trueWord.substring(i,i+1);
            singList.add(i,nextSign);
        }
        Collections.shuffle(singList);

        arrayTextView=new ArrayList<TextViewForGameDrapAndDrop>(singList.size());

        for (int i=0;i!=singList.size();++i){
            String currentText=singList.get(i);
            ButtonForDrapAndDrop nextButton=new ButtonForDrapAndDrop(this);
            nextButton.relatePosition=i;
            nextButton.setLayoutParams(new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));
            nextButton.setText(currentText);
            nextButton.setTextSize(24.0f);
            nextButton.setOnClickListener(mylistenerButton);
            nextButton.setBackgroundColor(getResources().getColor(R.color.drapAndDropEnable));
            flexLayout.addView(nextButton);


            TextViewForGameDrapAndDrop t=new TextViewForGameDrapAndDrop(this);
            t.relatePosition=i;
            arrayTextView.add(t);
            t.currentText=trueWord.substring(i,i+1);;
            t.setOnClickListener(mylistenerText);
            t.setText("_");
            t.setEnabled(false);
            t.setTextSize(23.0f);
            t.setLayoutParams(new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));
            t.setMinimumWidth(10);
            t.setTextColor(Color.BLACK);
            layoutForEdit.addView(t);
            t.setBackgroundColor(getResources().getColor(R.color.drapAndDropUnavailable));
            forFinalGumWord.put(i,t);


            if(i!=singList.size()-1){
                addDividerInFlexLayout(flexLayout);
                if(!isSpaceBetweenInputWord){
                    addDividerInFlexLayout(layoutForEdit);
                }

            }
        }


    }
    private void thisRound(){
        if(round== ROUND.ROUND_RU_COLLECT_ENG){
            trueView.setText(wordsList.get(position_Index).get("ru"));
            trueWord =wordsList.get(position_Index).get("eng");
        }else if(round== ROUND.ROUND_ENGVALUE_COLLECT_ENG){
            trueView.setText(wordsList.get(position_Index).get("eng_value"));
            trueWord =wordsList.get(position_Index).get("eng");
        }
    }
    private void addDividerInFlexLayout(FlexboxLayout layout){
        TextView divider=new TextView(this);
        divider.setTextColor(Color.TRANSPARENT);
        divider.setText("w");
        divider.setTextSize(24.0f);
        layout.addView(divider);
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
            x.setBackgroundColor(getResources().getColor(R.color.drapAndDropUnavailable));
            t.setEnabled(true);
            t.setBackgroundColor(getResources().getColor(R.color.drapAndDropEnable));

            if(arrayTextView.size()==0){
                String inputWord="";
                boolean status=true;
                for(int i=0;i!=trueWord.length();++i){
                    TextViewForGameDrapAndDrop next_t=forFinalGumWord.get(i);
                    if(!next_t.getText().toString().equals(next_t.currentText)){
                        next_t.setBackgroundColor(Color.RED);
                        status=false;
                    }
                }
                if(status){
                    for(int i=0;i!=trueWord.length();++i){
                        TextViewForGameDrapAndDrop next_t=forFinalGumWord.get(i);
                        next_t.setBackgroundColor(getResources().getColor(R.color.trueLayout));
                    }
                    needNewRound=true;
                    layoutForEdit.setBackgroundColor(getResources().getColor(R.color.trueLayout));
                }
            }
        }
    };

    View.OnClickListener mylistenerText = new View.OnClickListener() {
        @Override public void onClick(View v) {
            needNewRound=false;
            layoutForEdit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            TextViewForGameDrapAndDrop t=(TextViewForGameDrapAndDrop)v;
            t.setText("_");
            arrayTextView.add(t);
            t.setEnabled(false);
            t.button.setEnabled(true);
            t.button.setBackgroundColor(getResources().getColor(R.color.drapAndDropEnable));
            t.setBackgroundColor(getResources().getColor(R.color.drapAndDropUnavailable));
        }
    };
}
