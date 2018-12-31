package com.engru.al.engru;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
public class MainGameActivity extends Activity {
    public static MainGameActivity game;
    HelperSQL helperSql;
    LinearLayout mainLayout;
    LinearLayout secondRow;
    LinearLayout invisibleLayoutHide;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView engWord;
    TextView statusGame;
    CheckBox chooseUniqueCheckBox;
    ImageButton setting;
    ImageButton chooseList;
    FrameLayout frameLayout;
    ListView dinamic;
    static int round;
    ArrayList<HashMap<String, String>> wordsList;
    EngWord trueWord;
    Random random;
    MyArrayIntegerStringListToString chooseScroll;
    boolean block;
    float start_x;
    float start_y;
    boolean isUnique;
    boolean isEng;
    int round_countner_for_status;
    MyArrayIntegerStringListToString needOpenScroll; // keep intex to string ( because remove(<int>index) and remove(<object>integer)
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eng_ru);
        button1=(Button) findViewById(R.id.button1);
        button2=(Button) findViewById(R.id.button2);
        button3=(Button) findViewById(R.id.button3);
        button4=(Button) findViewById(R.id.button4);
        engWord=(TextView) findViewById(R.id.wordView);
        statusGame=(TextView) findViewById(R.id.statusGame);
        setting=(ImageButton) findViewById(R.id.settingsButton);
        chooseList=(ImageButton) findViewById(R.id.chooseList);
        mainLayout=(LinearLayout) findViewById(R.id.mainLayout);
        invisibleLayoutHide=(LinearLayout) findViewById(R.id.invisibleLayoutHide);
        secondRow=(LinearLayout) findViewById(R.id.secondRow);
        frameLayout=(FrameLayout) findViewById(R.id.frameLayout);
        dinamic=(ListView) findViewById(R.id._dynamic);
        chooseUniqueCheckBox=(CheckBox)findViewById(R.id.chooseUniqueCheckBox);
        random = new Random();
        needOpenScroll=new MyArrayIntegerStringListToString();
        isEng=true;
        isUnique=false;
        chooseScroll=new MyArrayIntegerStringListToString();
        try {
            helperSql=new HelperSQL(this);
            BaseORM.db = helperSql.getWritableDatabase();

            newGame();

            game=this;
            Cursor cursor = BaseORM.db.rawQuery(ScrollEngWordsAdapter.Table.QUERY_COUNT_SCROLL_ENG, null);
            dinamic.setAdapter(new ListsListCursor(this, cursor));

            frameLayout.setOnTouchListener (new View.OnTouchListener(){
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
                            if(move_x> 300 && move_y>-150 && move_y<150  ){
                                isEng=!isEng;
                                newGame();
                                return true;
                            }
                            if(chooseScroll.size()!=0 && move_x < -300 && move_y>-150 && move_y<150 ){
                                RepeatActivity.openRepeatActivity(MainGameActivity.this,chooseScroll.toString());
                                return true;
                            }
                            break;
                    }
                    return true;
                }});


            setShowSecondLayout(false);
        }catch (Exception e){
            ErrorActivity.throwError(MainGameActivity.this,e.toString());
        }
    }
    @Override protected void onRestart (){
        super.onRestart();
        this.reloadAllViewAndData();
    }
    public void showList(View view) {
        switch(secondRow.getVisibility()){
            case View.VISIBLE:
                secondRow.setVisibility(View.GONE);
                return;
            case View.GONE:
                secondRow.setVisibility(View.VISIBLE);
                return;
        }
    }
    public void showSettings(View view) {
        SettingsActivity.openSettingsActivity(MainGameActivity.this);
    }
    public static void openMainGameActivity(Context from){
        Intent intent = new Intent( from,MainGameActivity.class );
        from.startActivity(intent);
    }
    public void reloadAllViewAndData(){
        Cursor newCursor = BaseORM.db.rawQuery(ScrollEngWordsAdapter.Table.QUERY_COUNT_SCROLL_ENG, null);
        ((ListsListCursor)dinamic.getAdapter()).changeCursor(newCursor);
    }
    public void beforeGame(){
        round=-1;
        Cursor cursor;
        if(chooseScroll.size()==0) {
            String asId="AlonePassForIdGame";
            cursor = BaseORM.db.rawQuery("SELECT "
                            + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ID + " , "
                            + EngWord.Table.TABLE_NAME + "." + EngWord.Table.RU + " , "
                            + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ENG + " , "
                            + ScrollEngWordsAdapter.Table.TABLE_NAME + "." + ScrollEngWordsAdapter.Table.SCROLL_ID + " AS " + asId + " "
                            + " FROM " + ScrollEngWordsAdapter.Table.TABLE_NAME
                            + " JOIN " + EngWord.Table.TABLE_NAME + " ON "
                            + ScrollEngWordsAdapter.Table.TABLE_NAME + "." + ScrollEngWordsAdapter.Table.ENG_ID + " = " + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ID
                            + " WHERE " + ScrollEngWordsAdapter.Table.TABLE_NAME + "." + ScrollEngWordsAdapter.Table.SCROLL_ID
                            + " = ( SELECT w._id FROM ( " + ScrollEngWordsAdapter.Table.QUERY_COUNT_SCROLL_ENG + " ) AS w "
                            + " WHERE w." + ScrollEngWordsAdapter.Table.SCROLL_COUNT_AS + " >= 4 ORDER BY RANDOM() LIMIT 1 )"
                            + " ; "
                    , null);
             if(cursor.moveToNext()){
                 chooseScroll.add(cursor.getString(cursor.getColumnIndex(asId)));
                 cursor.moveToPosition(-1);
             }
        }else{
            String sql="SELECT "
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ID + " , "
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.RU + " , "
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ENG + "  "
                    + " FROM " + ScrollEngWordsAdapter.Table.TABLE_NAME
                    + " JOIN " + EngWord.Table.TABLE_NAME + " ON "
                    + ScrollEngWordsAdapter.Table.TABLE_NAME + "." + ScrollEngWordsAdapter.Table.ENG_ID + " = " + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ID
                    + " WHERE ";
            for(int i=0;i!=chooseScroll.size();++i){
                sql+=ScrollEngWordsAdapter.Table.TABLE_NAME + "." + ScrollEngWordsAdapter.Table.SCROLL_ID+" = ? ";
                if(i!=chooseScroll.size()-1){
                    sql+=" or ";
                }
            }
            sql+=" ; ";
            String [] question=new String[chooseScroll.size()];
            for(int i=0;i!=chooseScroll.size();++i){
                question[i]=chooseScroll.get(i);
            }
             cursor = BaseORM.db.rawQuery(sql
                    , question);
        }
        wordsList=new ArrayList<HashMap<String, String>>(cursor.getCount());
        ArrayList <String>uniqueList=new ArrayList<String>();
        while (cursor.moveToNext()){
            String engWord=cursor.getString(cursor.getColumnIndex(EngWord.Table.ENG));
            if(isUnique) {
                if (uniqueList.contains(engWord)) {
                    continue;
                } else {
                    uniqueList.add(engWord);
                }
            }
            HashMap<String, String> word=new HashMap<String, String>();
            word.put("eng",engWord);
            word.put("ru",cursor.getString(cursor.getColumnIndex(EngWord.Table.RU)));
            wordsList.add(word);
        }
        cursor.close();
        Collections.shuffle(wordsList);
    }
    public void resetRound(){
        round=0;
        ++round_countner_for_status;
        Collections.shuffle(wordsList);
    }
    public void nextRound(){
        button1.setBackgroundColor(getResources().getColor(R.color.default_button));
        button1.setTextColor(Color.BLACK);
        button2.setBackgroundColor(getResources().getColor(R.color.default_button));
        button2.setTextColor(Color.BLACK);
        button3.setBackgroundColor(getResources().getColor(R.color.default_button));
        button3.setTextColor(Color.BLACK);
        button4.setBackgroundColor(getResources().getColor(R.color.default_button));
        button4.setTextColor(Color.BLACK);
        ++round;
        if(round==wordsList.size()){
            resetRound();
        }
        trueWord=new EngWord();
        trueWord.ru=wordsList.get(round).get("ru");
        trueWord.eng=wordsList.get(round).get("eng");
        if(isEng){
            engWord.setText(trueWord.eng);
        }else{
            engWord.setText(trueWord.ru);
        }
        int [] array=getRandom();
        ArrayList<Integer> finalArray=new ArrayList<Integer>(4);
        finalArray.add(array[0]);
        finalArray.add(array[1]);
        finalArray.add(array[2]);
        finalArray.add(round);
        Collections.shuffle(finalArray);
        String language=isEng?"ru":"eng";
        button1.setText(wordsList.get(finalArray.get(0)).get(language));
        button2.setText(wordsList.get(finalArray.get(1)).get(language));
        button3.setText(wordsList.get(finalArray.get(2)).get(language));
        button4.setText(wordsList.get(finalArray.get(3)).get(language));
        updateStatus();
    }
    public int[] getRandom(){
        int [] array=new int[3];
        array[0]=-1;
        array[1]=-1;
        array[2]=-1;
        int count=0;
        while(count!=3){
            int x=random.nextInt(wordsList.size());
            if(x!=round && x!=array[0] && x!=array[1] && x!=array[2] ){
                array[count]=x;
                ++count;
            }
        }
        return array;
    }
    public void MainGameActivity_pressButton(View button){
        if(block){return;}
        Button chooseButton=(Button)button;
        String trueText=isEng?trueWord.ru:trueWord.eng;;
        String chooseText=chooseButton.getText().toString();
        if(chooseText.equals(trueText)){
            if (chooseButton.getCurrentTextColor() == getResources().getColor(R.color.trueText)) {
                nextRound();
                return;
            }
            chooseButton.setBackgroundColor(getResources().getColor(R.color.trueButton));
            chooseButton.setTextColor(getResources().getColor(R.color.trueText));

        }else{
            chooseButton.setBackgroundColor(Color.RED);
        }

    }
    public void updateStatus(){
        String status=Integer.toString(round+1) + "/" + Integer.toString(round_countner_for_status)+"("+Integer.toString(wordsList.size())+")";
        statusGame.setText(status);
    }
    public void newGame(){
        round_countner_for_status=0;
        block=false;
        beforeGame();
        if(wordsList.size()<4){
            statusGame.setText("----");
            engWord.setText("manual control. Default few size.");
            block = true;
            button1.setText("need sum");
            button2.setText("words");
            button3.setText(">=4");
            button4.setText("We are");
            return;
        }
        nextRound();
    }
    public void MainGameActivity_chooseScrolls(View v){
        isUnique = chooseUniqueCheckBox.isChecked();
        chooseScroll=new MyArrayIntegerStringListToString();
        chooseScroll.addAll(needOpenScroll);
        reloadAllViewAndData();
        setShowSecondLayout(false);
        newGame();
    }
    private void setShowSecondLayout(boolean isShow){
        if(isShow){
            secondRow.setVisibility(View.VISIBLE);
        }else {
            secondRow.setVisibility(View.GONE);
        }
    }
    public void MainGameActivity_GameDrapAndDrop(View v){
        GameDragAndDropActivity.openGameDragAndDropActivity(MainGameActivity.this,chooseScroll.toString());
    }
}
