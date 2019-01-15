package com.engru.al.Dream;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import java.util.Calendar;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.view.MotionEvent;
import android.widget.Toast;

import static com.engru.al.Dream.MainGameActivity.TypeRounds.ENGVALUE_ENG;
import static com.engru.al.Dream.MainGameActivity.TypeRounds.RU_ENG;

public class MainGameActivity extends Activity {
    enum TypeRounds{ENG_RU,RU_ENG,ENGVALUE_ENG,ENG_ENGVALUE}
    enum SAVE_SIZE{WORDVIEW,BUTTONS}
    LinearLayout mainLayout;
    LinearLayout secondRow;
    LinearLayout thirdRow;
    LinearLayout hideThirdRow;
    LinearLayout fourRow;
    TextView settingsEngView;
    TextView settingsRuView;
    TextView settingsEng_valueView;
    TextView settingsExampleView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView wordView;
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
    TypeRounds typeRound;
    int halfWindow;
    int round_countner_for_status;
    SharedPreferences settings;
    boolean needUpdateListWithScroll;
    FullEditGame editGame;
    MyArrayIntegerStringListToString needOpenScroll; // keep intex to string ( because remove(<int>index) and remove(<object>integer)
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eng_ru);
        needUpdateListWithScroll=false;
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        settingsEngView=findViewById(R.id.settingsEngView);
        hideThirdRow=findViewById(R.id.hideThirdRow);
        settingsRuView=findViewById(R.id.settingsRuView);
        settingsEng_valueView=findViewById(R.id.settingsEng_valueView);
        settingsExampleView=findViewById(R.id.settingsExampleView);
        fourRow=findViewById(R.id.fourRow);
        wordView=findViewById(R.id.wordView);
        statusGame=findViewById(R.id.statusGame);
        setting=findViewById(R.id.settingsButton);
        chooseList=findViewById(R.id.chooseList);
        mainLayout=findViewById(R.id.mainLayout);
        secondRow=findViewById(R.id.secondRow);
        thirdRow=findViewById(R.id.thirdRow);
        frameLayout=findViewById(R.id.frameLayout);
        dinamic=findViewById(R.id._dynamic);
        chooseUniqueCheckBox=findViewById(R.id.chooseUniqueCheckBox);
        random = new Random();
        needOpenScroll=new MyArrayIntegerStringListToString();
        typeRound=TypeRounds.ENG_RU;
        isUnique=false;
        chooseScroll=new MyArrayIntegerStringListToString();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        halfWindow=width/2;
        settings=getSharedPreferences("MySettings",MODE_PRIVATE);
        try {
            Cursor cursor = BaseORM.get_db().rawQuery(ScrollEngWordsAdapter.Table.QUERY_COUNT_SCROLL_ENG, null);
            ListsListCursor n=new ListsListCursor(this, cursor);
            n.game=this;
            dinamic.setAdapter(n);

            newGame();

            editGame=new FullEditGame();
            editGame.fullEditWordView=findViewById(R.id.fullEditWordView);
            editGame.fullEditView=findViewById(R.id.fullEditView);
            editGame.mainLayout=fourRow;
            editGame.context=this;
            editGame.setListenerAfterInitialisation();


            frameLayoutSetListener();

            setShowSecondLayout(false);
            setShowThirdLayout(false);
            loadSize(SAVE_SIZE.WORDVIEW);
            loadSize(SAVE_SIZE.BUTTONS);
        }catch (Exception e){
            ErrorActivity.throwError(MainGameActivity.this,e.toString());
        }
    }
    @Override protected void onRestart (){
        super.onRestart();
        this.reloadAllViewAndData();
    }
    private void frameLayoutSetListener(){
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
                            switch(typeRound){
                                case ENG_RU:
                                    typeRound=RU_ENG;
                                    break;
                                case RU_ENG:
                                    typeRound=ENGVALUE_ENG;
                                    break;
                                case ENGVALUE_ENG:
                                    typeRound=TypeRounds.ENG_ENGVALUE;
                                    break;
                                case ENG_ENGVALUE:
                                    typeRound=TypeRounds.ENG_RU;
                                    break;
                            }
                            loadSize(SAVE_SIZE.WORDVIEW);
                            loadSize(SAVE_SIZE.BUTTONS);
                            nextGameWithThisWords();
                            return true;
                        }
                        if(chooseScroll.size()!=0 && move_x < -300 && move_y>-150 && move_y<150 ){
                            RepeatActivity.openRepeatActivity(MainGameActivity.this,chooseScroll.toString());
                            return true;
                        }
                        else if( move_y> 200 && move_x>-150 && move_x<150  ){
                            float nowSize=wordView.getTextSize();
                            if(halfWindow>start_x && halfWindow>x ){//В правой стороне провели вниз или вверх
                                nowSize+=1.0f;
                            }else{//провели по левой стороне.
                                nowSize-=1.0f;
                            }
                            wordView.setTextSize(TypedValue.COMPLEX_UNIT_PX ,nowSize);
                            saveSettings(SAVE_SIZE.WORDVIEW,nowSize);
                        }else if( move_y<-200 && move_x>-150 && move_x<150  ){
                            float nowSize=button1.getTextSize();
                            if(halfWindow>start_x && halfWindow>x ){//В правой стороне провели вверх или вниз
                                nowSize+=1.0f;

                            }else{//провели по левой стороне.
                                nowSize-=1.0f;
                            }
                            button1.setTextSize(TypedValue.COMPLEX_UNIT_PX ,nowSize);
                            button2.setTextSize(TypedValue.COMPLEX_UNIT_PX ,nowSize);
                            button3.setTextSize(TypedValue.COMPLEX_UNIT_PX ,nowSize);
                            button4.setTextSize(TypedValue.COMPLEX_UNIT_PX ,nowSize);
                            saveSettings(SAVE_SIZE.BUTTONS,nowSize);
                        }
                        break;
                }
                return true;
            }});
    }
    public void showList(View view) {
        setShowThirdLayout(false);
        switch(secondRow.getVisibility()){
            case View.VISIBLE:
                setShowSecondLayout(false);
                return;
            case View.GONE:
                if(needUpdateListWithScroll){
                    Cursor cursor = BaseORM.get_db().rawQuery(ScrollEngWordsAdapter.Table.QUERY_COUNT_SCROLL_ENG, null);
                    ((ListsListCursor)dinamic.getAdapter()).changeCursor(cursor);
                }
                setShowSecondLayout(true);
                return;
        }
    }
    public void showSettings(View view) {
        setShowSecondLayout(false);
        if(thirdRow.getVisibility()==View.VISIBLE){
            setShowThirdLayout(false);
            return;
        }else{
            setShowThirdLayout(true);
            return;
        }
    }
    public static void openMainGameActivity(Context from){
        Intent intent = new Intent( from,MainGameActivity.class );
        from.startActivity(intent);
    }
    public void reloadAllViewAndData(){
        Cursor newCursor = BaseORM.get_db().rawQuery(ScrollEngWordsAdapter.Table.QUERY_COUNT_SCROLL_ENG, null);
        ((ListsListCursor)dinamic.getAdapter()).changeCursor(newCursor);
    }
    public void beforeGame(){
        round=-1;
        Cursor cursor;
        if(chooseScroll.size()==0) {
            String asId="AlonePassForIdGame";
            cursor = BaseORM.get_db().rawQuery("SELECT "
                            + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ID + " , "
                            + EngWord.Table.TABLE_NAME + "." + EngWord.Table.RU + " , "
                            + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ENG_VALUE + " , "
                            + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ENG + " , "
                            + EngWord.Table.TABLE_NAME + "." + EngWord.Table.EXAMPLE + " , "
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
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.ENG_VALUE + " , "
                    + EngWord.Table.TABLE_NAME + "." + EngWord.Table.EXAMPLE + " , "
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
             cursor = BaseORM.get_db().rawQuery(sql
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
            word.put("id",cursor.getString(cursor.getColumnIndex(EngWord.Table.ID)));
            word.put("eng",engWord);
            word.put("ru",cursor.getString(cursor.getColumnIndex(EngWord.Table.RU)));
            word.put("eng_value",cursor.getString(cursor.getColumnIndex(EngWord.Table.ENG_VALUE)));
            word.put("example",cursor.getString(cursor.getColumnIndex(EngWord.Table.EXAMPLE)));
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
        if(isWordListLowSize()){
            return;
        }
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
        trueWord.id=Integer.parseInt(wordsList.get(round).get("id"));
        trueWord.example=wordsList.get(round).get("example");
        trueWord.eng=wordsList.get(round).get("eng");
        trueWord.eng_value=wordsList.get(round).get("eng_value");
        String language;
        switch(typeRound){
            case ENG_RU:
                wordView.setText(trueWord.eng);
                language="ru";
                break;
            case ENG_ENGVALUE:
                wordView.setText(trueWord.eng);
                language="eng_value";
                break;
            case RU_ENG:
                wordView.setText(trueWord.ru);
                language="eng";
                break;
            case ENGVALUE_ENG:
                wordView.setText(trueWord.eng_value);
                language="eng";
                break;
            default:
                ErrorActivity.throwError(MainGameActivity.this,"Это typeRound не обрабатыватся. Логическая ошибка программы.");
                return;
        }
        int [] array=getRandom();
        ArrayList<Integer> finalArray=new ArrayList<Integer>(4);
        finalArray.add(array[0]);
        finalArray.add(array[1]);
        finalArray.add(array[2]);
        finalArray.add(round);
        Collections.shuffle(finalArray);

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
        String trueText;
        switch(typeRound){
            case ENG_RU:
                trueText=trueWord.ru;
                break;
            case ENG_ENGVALUE:
                trueText=trueWord.eng_value;
                break;
            case RU_ENG:
                trueText=trueWord.eng;
                break;
            case ENGVALUE_ENG:
                trueText=trueWord.eng;
                break;
            default:
                ErrorActivity.throwError(MainGameActivity.this,"Это typeRound не обрабатыватся. Логическая ошибка программы.");
                return;
        }
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
        trueWord=null;
        round_countner_for_status=0;
        block=false;
        beforeGame();
        if(isWordListLowSize()){
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
            secondRow.setZ(1f);
        }else {
            secondRow.setVisibility(View.GONE);
            secondRow.setZ(0f);
        }
    }
    private void setShowThirdLayout(boolean isShow){
        if(isShow){
            thirdRow.setVisibility(View.VISIBLE);
            thirdRow.setZ(1f);
            if(trueWord!=null && fourRow.getVisibility()==View.GONE){
                settingsEngView.setText(trueWord.eng);
                settingsRuView.setText(trueWord.ru);
                settingsEng_valueView.setText(trueWord.eng_value);
                settingsExampleView.setText(trueWord.example);
            }else if(editGame!=null && editGame.trueWord!=null && fourRow.getVisibility()==View.VISIBLE){
                settingsEngView.setText(editGame.trueWord.eng);
                settingsRuView.setText(editGame.trueWord.ru);
                settingsEng_valueView.setText(editGame.trueWord.eng_value);
                settingsExampleView.setText(editGame.trueWord.example);
            }else{
                settingsEngView.setText("Problems");
                settingsRuView.setText("with");
                settingsEng_valueView.setText("current");
                settingsExampleView.setText("word.");
            }
        }else {
            thirdRow.setVisibility(View.GONE);
            thirdRow.setZ(0f);
        }
    }
    private void setShowFourLayout(boolean isShow){
        if(isShow){
            fourRow.setVisibility(View.VISIBLE);
        }else {
            fourRow.setVisibility(View.GONE);
        }
    }
    private boolean isWordListLowSize(){
        if(wordsList.size()<4){
            statusGame.setText("----");
            wordView.setText("manual control. Default few size.");
            block = true;
            button1.setText("need sum");
            button2.setText("words");
            button3.setText(">=4");
            button4.setText("We are");
            return true;
        }
        return false;
    }
    private void nextGameWithThisWords(){
        round=-1;
        round_countner_for_status=0;
        Collections.shuffle(wordsList);
        nextRound();
    }
    public void MainGameActivity_GameDrapAndDrop(View v){
        GameDragAndDropActivity.openGameDragAndDropActivity(MainGameActivity.this,needOpenScroll.toString());
    }
    public void MainGameActivity_AddToTemporaryScroll(View v){
        if(wordsList.size()<1){
            Toast toast = Toast.makeText(getApplicationContext(),"Not word", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        Calendar rightNow = Calendar.getInstance();
        int year=rightNow.get(Calendar.YEAR);
        int month=rightNow.get(Calendar.MONTH)+1;//index
        int day=rightNow.get(Calendar.DAY_OF_MONTH);//number
        String monthWithZero=addZeroIfDateLessTen(month,false);
        String dayWithZero=addZeroIfDateLessTen(day,true);

        String temporaryName="Temporary:"+Integer.toString(year)+"."+monthWithZero+"."+dayWithZero;
        Cursor cursor =BaseORM.get_db().rawQuery("select _id from " + Scroll.Table.TABLE_NAME
                +"  where "+ Scroll.Table.NAME + " = ?" , new String[]{ temporaryName});
        int scrollId=-1;
        if(cursor.getCount()==0){
            cursor.close();
            Scroll scroll=new Scroll();
            scroll.name=temporaryName;
            scroll.save();
            scrollId=scroll.id;
        }else {
            cursor.moveToNext();
            scrollId=cursor.getInt(cursor.getColumnIndex("_id"));
            needUpdateListWithScroll=true;
        }
        if(scrollId==-1){
            ErrorActivity.throwError(this,"Непредвиженное создание или нахождение Scroll по имени:<"+temporaryName+">; В MainGameActivity_AddToTemporaryScroll почему-то не получилось получить ид.");
        }
        ScrollEngWordsAdapter adapter=new ScrollEngWordsAdapter();

        if(trueWord!=null && fourRow.getVisibility()==View.GONE){
            adapter.eng_id=trueWord.id;
            adapter.scroll_id=scrollId;
        }else if(editGame!=null && editGame.trueWord!=null && fourRow.getVisibility()==View.VISIBLE){
            adapter.eng_id=editGame.trueWord.id;
            adapter.scroll_id=scrollId;
            Log.d("q1","Add in edit game: "+adapter.eng_id);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"Не получается добавить во временный лист.\n" +
                    "или слова нет, или с вложенной игрой проблема, если добавляется в игре.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        adapter.save();
    }
    private String addZeroIfDateLessTen(int date,boolean isDay){
        if(date<10 && date>=1){
            return "0"+Integer.toString(date);
        }else if(date>=10){
            if(isDay && date>=32){
                ErrorActivity.throwError(MainGameActivity.this,"Календарь вернул странную дату в днях<"+date+">;");
                return null;
            }else if( !isDay && date>=13){
                ErrorActivity.throwError(MainGameActivity.this,"Календарь вернул странную дату в днях<"+date+">;");
                return null;
            }
            return Integer.toString(date);
        }else{
            ErrorActivity.throwError(MainGameActivity.this,"Календарь вернул странную дату, не день и не месяц.<"+date+">;");
        }
        return null;
    }
    public void MainGameActivity_DeleteWordFromWordList(View v){
        if(wordsList.size()<=4){
            Toast toast = Toast.makeText(getApplicationContext(),"Can`t delete. Be low size", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        wordsList.remove(round);
        resetRound();
        --round_countner_for_status;
        nextRound();
    }
    private void saveSettings(SAVE_SIZE view,float size){
        SharedPreferences.Editor ed = settings.edit();
        ed.putFloat(getKeyFromSaveLoad(view),size);
        ed.commit();
    }
    private void loadSize(SAVE_SIZE view){
        float oldSize=settings.getFloat(getKeyFromSaveLoad(view),-1);
        switch (view){
            case BUTTONS:
                if(oldSize==-1){
                    saveSettings(SAVE_SIZE.BUTTONS,button1.getTextSize());
                }
                button1.setTextSize(TypedValue.COMPLEX_UNIT_PX ,oldSize);
                button2.setTextSize(TypedValue.COMPLEX_UNIT_PX ,oldSize);
                button3.setTextSize(TypedValue.COMPLEX_UNIT_PX ,oldSize);
                button4.setTextSize(TypedValue.COMPLEX_UNIT_PX ,oldSize);
                break;
            case WORDVIEW:
                if(oldSize==-1){
                    saveSettings(SAVE_SIZE.WORDVIEW,wordView.getTextSize());
                }
                wordView.setTextSize(TypedValue.COMPLEX_UNIT_PX ,oldSize);
                break;
            default:
                ErrorActivity.throwError(MainGameActivity.this,"Неожиданное значение в saveSettings. Такое перечисление я не обрабатываю.");
        }
    }
    private String getKeyFromSaveLoad(SAVE_SIZE view){
        String key=typeRound.toString()+"|";;
        switch (view){
            case BUTTONS:
                key+="BUTTONS";
                break;
            case WORDVIEW:
                key+="WORDVIEW";
                break;
            default:
                ErrorActivity.throwError(MainGameActivity.this,"Неожиданное значение в saveSettings. Такое перечисление я не обрабатываю.");
                return "";
        }
        return key;
    }
    public void MainGameActivity_openFullEditGame(View v){
        round=0;
        round_countner_for_status=0;
        updateStatus();
        if(fourRow.getVisibility()==View.GONE){
            if(wordsList.size()<1){
                Toast toast = Toast.makeText(getApplicationContext(),"Not words", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            editGame.typeRound=typeRound;
            editGame.wordsList=this.wordsList;
            Collections.shuffle(editGame.wordsList);
            setShowFourLayout(true);
            setShowThirdLayout(false);
            editGame.nextRound();
        }else{
            setShowFourLayout(false);
            setShowThirdLayout(false);
            round=-1;
            Collections.shuffle(wordsList);
            nextRound();
        }
    }
    public void MainGameActivity_blockThroughClick(View v){
        //Button in maingame catch click through click on background throught row.
    }
}


class FullEditGame{
    EditText fullEditView;
    TextView fullEditWordView;
    LinearLayout mainLayout;
    ArrayList<HashMap<String, String>> wordsList;
    EngWord trueWord;
    private int position;
    private boolean nextround;
    MainGameActivity.TypeRounds typeRound;
    MainGameActivity context;
    int count;
    FullEditGame(){
        position=0;
        nextround=false;
        count=0;
    }
    public void setListenerAfterInitialisation(){
        fullEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if( isInputTrue() ){
                        if(goodInput()){
                            return true;
                        }
                        nextround=true;
                        fullEditWordView.setBackgroundColor(context.getResources().getColor(R.color.trueLayout));
                    }else{
                        fullEditWordView.setBackgroundColor(context.getResources().getColor(R.color.error_button));
                    }
                }
                return true;
            }
        });
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isInputTrue() ){
                    if(goodInput()){
                        return;
                    }
                    nextround=true;
                    fullEditWordView.setBackgroundColor(context.getResources().getColor(R.color.trueLayout));
                }else{
                    fullEditWordView.setBackgroundColor(context.getResources().getColor(R.color.error_button));
                }
            }
        });
    }
    private boolean goodInput(){
        if(nextround){
            nextround=false;
            fullEditWordView.setBackgroundColor(context.getResources().getColor(R.color.defaultFormBackground));
            ++position;
            updateStatus(1,0,-1,-1);
            nextRound();
            return true;
        }
        return false;
    }
    public void nextRound(){
        if(position>=wordsList.size()){
            position=0;
            Collections.shuffle(wordsList);
            updateStatus(0,1,0,-1);
        }
        HashMap<String, String> word=wordsList.get(position);
        trueWord=new EngWord();
        trueWord.id=Integer.parseInt(word.get("id"));;
        trueWord.eng=word.get("eng");
        trueWord.ru=word.get("ru");
        trueWord.eng_value=word.get("eng_value");
        trueWord.example=word.get("example");

        fullEditView.setText("");
        String trueValue;
        switch (typeRound){
            case ENG_ENGVALUE:
                typeRound=ENGVALUE_ENG;
                Toast toast1 = Toast.makeText(context,"ENG_ENGVALUE переделано в ENGVALUE_ENG: набрать ENGVALUE нереально.", Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.CENTER, 0, 0);
                toast1.show();
                trueValue=trueWord.eng_value;
                break;
            case RU_ENG:
                trueValue=trueWord.ru;
                break;
            case ENG_RU:
                typeRound=RU_ENG;
                Toast toast2 = Toast.makeText(context,"ENG_RU переделано в RU_ENG: набрать RU нереально.", Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.CENTER, 0, 0);
                toast2.show();
                trueValue=trueWord.ru;
                break;
            case ENGVALUE_ENG:
                trueValue=trueWord.eng_value;
                break;
            default:
                ErrorActivity.throwError(context,"Не обрабатываемый typeRound в EditGame:< "+typeRound+" >;");
                return;
        }
        if(trueValue==null || trueValue.length()==0){
            //Log.d("q1","count="+count+"|"+trueWord);
            if(count>=wordsList.size()-1){
                ErrorActivity.throwError(context,"В этом наборе слов нет слов такого типа:< "+typeRound+" > под него нечего отображать.");
                return;
            }
            Toast toast2 = Toast.makeText(context,"Это слово не отображается:"+trueWord, Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.START, 0, count*10);
            toast2.show();
            ++count;
            ++position;
            nextRound();
            return;
        }
        count=0;
        fullEditWordView.setText(trueValue);
    }
    private boolean isInputTrue(){
        String currentMessege = fullEditView.getText().toString();
        String trueValue;
        switch(typeRound){
            case ENGVALUE_ENG:
                trueValue=trueWord.eng;
                break;
            case RU_ENG:
                trueValue=trueWord.eng;
                break;
            default:
                ErrorActivity.throwError(context,"Не обрабатываемый typeRound в isInputTrue EditGame:< "+typeRound+" >;");
                return false;
        }
        if(currentMessege.equals(trueValue)){
            return true;
        }
        return false;
    }
    private void updateStatus(int changeRound,int changeCount,int round,int count){
        MainGameActivity.round+=changeRound;
        context.round_countner_for_status+=changeCount;
        if(round!=-1){
            MainGameActivity.round=round;
        }
        if(count!=-1){
            context.round_countner_for_status=count;
        }
        context.updateStatus();
    }
}
