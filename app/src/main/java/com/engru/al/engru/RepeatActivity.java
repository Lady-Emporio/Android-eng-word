package com.engru.al.engru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RepeatActivity extends Activity {
    ArrayList<String> ids;
    Cursor cursor;
    int position;
    int maxWord;
    Button knowButton;
    TextView engRepeat;
    TextView ruRepeat;
    TextView repeatStatus;
    ArrayList<HashMap<String, String>> wordsList;
    float start_x;
    float start_y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat);
        Intent intent = getIntent();
        String id_for_split= intent.getStringExtra("id");
        ids=MyArrayIntegerStringListToString.backwardFromString(id_for_split);
        engRepeat=findViewById(R.id.engRepeat);
        ruRepeat=findViewById(R.id.ruRepeat);
        knowButton=findViewById(R.id.knowButton);
        repeatStatus=findViewById(R.id.repeatStatus);
        if ( ids!=null && ids.size() !=0){
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
            cursor = BaseORM.db.rawQuery(sql, id_for_query );
            wordsList=new ArrayList<HashMap<String, String>>(cursor.getCount());
            while (cursor.moveToNext()){
                HashMap<String, String> word=new HashMap<String, String>();
                word.put("eng",cursor.getString(cursor.getColumnIndex(EngWord.Table.ENG)));
                word.put("ru",cursor.getString(cursor.getColumnIndex(EngWord.Table.RU)));
                wordsList.add(word);
            }
            maxWord=wordsList.size();
        }else{
            engRepeat.setText("Не выбран Scroll");
            ruRepeat.setText("");
            return;
        }
        position=0;
        viewNewWord();

        ConstraintLayout mainLayoutRepeat=findViewById(R.id.mainLayoutRepeat);
        mainLayoutRepeat.setOnTouchListener (new View.OnTouchListener(){
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
                        if(move_x> 200 && move_y>-150 && move_y<150  ){
                            --position;
                            if(position<0){
                                position=wordsList.size()-1;
                            }
                            viewNewWord();
                            return true;
                        }
                        ++position;
                        if(position>=wordsList.size()){
                            position=0;
                        }
                        viewNewWord();
                        return true;
                        }


                return true;
            }});

    }
    public static void openRepeatActivity(Context from,String idScroll){
        Intent intent = new Intent( from,RepeatActivity.class );
        intent.putExtra("id",idScroll);
        from.startActivity(intent);
    }
    public void knowButtonRepeatActivity(View view){
        if(wordsList.size()==0){
            engRepeat.setText("All know");
            ruRepeat.setText("We are");
            return;
        }
        wordsList.remove(position);
        if (position!=0){
            --position;
        }
        viewNewWord();
    }
    public void viewNewWord(){
        if(wordsList.size()==0){
            engRepeat.setText("All know");
            ruRepeat.setText("We are");
            position =-1;//for updateStatus
            updateStatus();
            return;
        }
        HashMap<String, String> word=wordsList.get(position);
        engRepeat.setText(word.get("eng"));
        ruRepeat.setText(word.get("ru"));
        updateStatus();
    }
    public void updateStatus(){
        String status=Integer.toString(position+1) //because index start with 0, this is Nomber - not index
                + "/"
                + Integer.toString(wordsList.size())
                + "("
                + Integer.toString(maxWord)
                + ")";
        repeatStatus.setText(status);
    }
}
