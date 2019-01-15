package com.engru.al.Dream.Verbs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.engru.al.Dream.BaseORM;
import com.engru.al.Dream.ErrorActivity;
import com.engru.al.Dream.R;
import com.engru.al.Dream.Views.RowEdtView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ActivityIrregularVerbs extends AppCompatActivity {
    TextView engView;
    LinearLayout ifNeedOpenNext;
    LinearLayout leftSettingsView;
    RowEdtView editView1;
    RowEdtView editView2;
    RowEdtView editView3;
    Switch chooseView;
    Spinner changeMode;
    SeekBar seekMin;
    SeekBar seekMax;
    TextView minView;
    TextView maxView;
    TextView v1View;
    TextView v2View;
    TextView v3View;
    TextView engValueView;
    TextView ruView;
    TextView exampleView;

    ArrayList<IrregularVerbs> wordsList;
    EditGameXFromThree gameX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initALl();
        initAndFillwordsList();
        startNewGameX(EditGameXFromThree.Mode.V2_AND_V3);
        updateSeekThroughtGameX();
    }

    public static void openActivityIrregularVerbs(Context from) {
        Intent intent = new Intent(from, ActivityIrregularVerbs.class);
        from.startActivity(intent);
    }

    private void initAndFillwordsList() {
        String sql = "SELECT * FROM " + IrregularVerbs.Table.TABLE_NAME + " ORDER BY "+IrregularVerbs.Table.Infinitive_V1+";";
        Cursor cursor = BaseORM.get_db().rawQuery(sql, null);
        wordsList = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            IrregularVerbs verb = new IrregularVerbs();

            verb.infinitive_v1 = cursor.getString(cursor.getColumnIndex(IrregularVerbs.Table.Infinitive_V1));
            verb.past_simple_v2 = cursor.getString(cursor.getColumnIndex(IrregularVerbs.Table.PAST_SIMPLE_V2));
            verb.past_particple_v3 = cursor.getString(cursor.getColumnIndex(IrregularVerbs.Table.PAST_PARTICPLE_V3));
            verb.ru = cursor.getString(cursor.getColumnIndex(IrregularVerbs.Table.RU));
            verb.eng_value = cursor.getString(cursor.getColumnIndex(IrregularVerbs.Table.ENG_VALUE));
            verb.example = cursor.getString(cursor.getColumnIndex(IrregularVerbs.Table.EXAMPLE));

            wordsList.add(verb);
        }
    }

    private void initALl() {
        setContentView(R.layout.activity_irregular_verbs);
        engView = findViewById(R.id.engView);
        editView1 = findViewById(R.id.editView1);
        editView2 = findViewById(R.id.editView2);
        editView3 = findViewById(R.id.editView3);
        ifNeedOpenNext = findViewById(R.id.ifNeedOpenNext);
        v1View = findViewById(R.id.v1View);
        v2View = findViewById(R.id.v2View);
        v3View = findViewById(R.id.v3View);
        engValueView = findViewById(R.id.engValueView);
        ruView = findViewById(R.id.ruView);
        exampleView = findViewById(R.id.exampleView);
        changeMode = findViewById(R.id.changeMode);
        leftSettingsView = findViewById(R.id.leftSettingsView);
        seekMin = findViewById(R.id.seekMin);
        seekMax = findViewById(R.id.seekMax);
        minView = findViewById(R.id.minView);
        maxView = findViewById(R.id.maxView);
        chooseView = findViewById(R.id.chooseView);

        chooseView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(gameX!=null){
                    gameX.isEngValue=isChecked;
                    gameX.setEngViewText();
                }
            }
        });

        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar.getId()==R.id.seekMin){
                    minView.setText(Integer.toString(progress));
                }else if(seekBar.getId()==R.id.seekMax){
                    maxView.setText(Integer.toString(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(seekBar.getId()==R.id.seekMin){
                    minView.setText(Integer.toString(seekBar.getProgress()));
                }else if(seekBar.getId()==R.id.seekMax){
                    maxView.setText(Integer.toString(seekBar.getProgress()));
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getId()==R.id.seekMin){
                    int max=seekMax.getProgress()-1;
                    int current=seekBar.getProgress();
                    if(current>max){
                        seekBar.setProgress(max);
                        minView.setText(Integer.toString(max));
                    }else{
                        minView.setText(Integer.toString(current));
                    }
                }else if(seekBar.getId()==R.id.seekMax){
                    int min=seekMin.getProgress()+1;
                    int current=seekBar.getProgress();
                    if(current<min){
                        seekBar.setProgress(min);
                        maxView.setText(Integer.toString(min));
                    }else{
                        maxView.setText(Integer.toString(seekBar.getProgress()));
                    }
                }
            }
        };
        seekMin.setOnSeekBarChangeListener(seekBarChangeListener);
        seekMax.setOnSeekBarChangeListener(seekBarChangeListener);

        changeMode.setAdapter(new ArrayAdapter<EditGameXFromThree.Mode>(this, android.R.layout.simple_dropdown_item_1line,EditGameXFromThree.Mode.values() ));
    }

    public void startNewGameX(EditGameXFromThree.Mode mode){
        gameX=new EditGameXFromThree(mode, wordsList, this);
        gameX.engView=engView;
        gameX.editView1=editView1;
        gameX.editView2=editView2;
        gameX.editView3=editView3;
        gameX.ifNeedOpenNext=ifNeedOpenNext;
        gameX.afterInitView();
        gameX.setListener();
        gameX.nextRound();

        chooseView.setChecked(gameX.isEngValue);
    }
    private void updateSeekThroughtGameX(){
    seekMin.setMax(gameX.wordsList.size());
    seekMax.setMax(gameX.wordsList.size());

    seekMin.setProgress(gameX.minPosition);
    seekMax.setProgress(gameX.maxPosition);

    minView.setText(Integer.toString(seekMin.getProgress()));
    maxView.setText(Integer.toString(seekMax.getProgress()));
}


    public void openSettings(View v){
        if(gameX!=null){
            if(leftSettingsView.getVisibility()==View.VISIBLE){
                isShowSettings(false);
            }else{
                isShowSettings(true);
                fillDataViewSettings();
            }
        }
    }
    public void catchClickEmptyFunction(View v){}
    private void isShowSettings(boolean status){
        if(status){
            leftSettingsView.setVisibility(View.VISIBLE);
        }else{
            leftSettingsView.setVisibility(View.GONE);
        }
    }

    private void fillDataViewSettings(){
        IrregularVerbs verb=gameX.wordsList.get(gameX.position);
        v1View.setText(verb.infinitive_v1);
        v2View.setText(verb.past_simple_v2);
        v3View.setText(verb.past_particple_v3);
        engValueView.setText(verb.eng_value);
        ruView.setText(verb.ru);
        exampleView.setText(verb.example);
        updateSeekThroughtGameX();
    }
    public void changeModeEditGameX(View v){
        EditGameXFromThree.Mode mode=(EditGameXFromThree.Mode)changeMode.getSelectedItem();
        startNewGameX(mode);
        fillDataViewSettings();
    }
    public void toBeginWords(View v){
        gameX.position=gameX.minPosition;
        gameX.nextRound();
    }
    public void shuffleWords(View v){
        Collections.shuffle(wordsList);
    }
    public void sortWords(View v){
        Collections.sort(wordsList, new Comparator<IrregularVerbs>() {
            @Override
            public int compare(IrregularVerbs o1, IrregularVerbs o2) {
                return o1.infinitive_v1.compareTo(o2.infinitive_v1);
            }
        });
    }
    public void commitMinMaxPositionGameX(View v){
        if(gameX!=null){
            gameX.minPosition=seekMin.getProgress();
            gameX.maxPosition=seekMax.getProgress();
        }
    }
}







class EditGameXFromThree {
    TextView engView;
    RowEdtView editView1;
    RowEdtView editView2;
    RowEdtView editView3;
    LinearLayout ifNeedOpenNext;
    enum Mode {RANDOM_ONE, RANDOM_TWO, ALL, ONLI_infinitive_v1, ONLI_past_simple_v2, ONLI_past_particple_v3, V1_AND_V2, V1_AND_V3, V2_AND_V3}
    enum Status{TRUE,FALSE,DEFAULT}
    private Mode mode;
    private Context context;
    boolean isUseEdit1;
    boolean isUseEdit2;
    boolean isUseEdit3;
    ArrayList<IrregularVerbs> wordsList;
    int position;
    boolean needNextRound;
    int minPosition;
    int maxPosition;
    boolean isEngValue;
    EditGameXFromThree(Mode mode, ArrayList<IrregularVerbs> wordsList, Context context) {
        this.mode = mode;
        this.context = context;
        this.wordsList = wordsList;
        minPosition=0;
        position = minPosition;
        needNextRound=true;
        maxPosition=wordsList.size();
        isEngValue=true;
    }

    public void afterInitView() {
        switch (mode) {
            case RANDOM_ONE:
                ArrayList<Boolean> array1 = new ArrayList<>(3);
                array1.add(true);
                array1.add(false);
                array1.add(false);
                Collections.shuffle(array1);
                isUseEdit1 = array1.get(0);
                isUseEdit2 = array1.get(1);
                isUseEdit3 = array1.get(2);
                break;
            case RANDOM_TWO:
                    ArrayList<Boolean> array2 = new ArrayList<>(3);
                    array2.add(true);
                    array2.add(true);
                    array2.add(false);
                    Collections.shuffle(array2);
                    isUseEdit1 = array2.get(0);
                    isUseEdit2 = array2.get(1);
                    isUseEdit3 = array2.get(2);
                    break;
            case ALL:
                    isUseEdit1 = true;
                    isUseEdit2 = true;
                    isUseEdit3 = true;
                    break;
            case ONLI_infinitive_v1:
                    isUseEdit1 = true;
                    isUseEdit2 = false;
                    isUseEdit3 = false;
                    break;
            case ONLI_past_simple_v2:
                    isUseEdit1 = false;
                    isUseEdit2 = true;
                    isUseEdit3 = false;
                    break;
            case ONLI_past_particple_v3:
                    isUseEdit1 = false;
                    isUseEdit2 = false;
                    isUseEdit3 = true;
                    break;
            case V1_AND_V2:
                    isUseEdit1 = true;
                    isUseEdit2 = true;
                    isUseEdit3 = false;
                    break;
            case V1_AND_V3:
                    isUseEdit1 = true;
                    isUseEdit2 = false;
                    isUseEdit3 = true;
                    break;
            case V2_AND_V3:
                    isUseEdit1 = false;
                    isUseEdit2 = true;
                    isUseEdit3 = true;
                    break;
            default:
                    ErrorActivity.throwError(context, "Непредвиженный mode в EditGameXFromThree:<" + mode + ">;");
                    return;
    }
        editView1.setBackgroundColor(context.getResources().getColor(R.color.defaultFormBackground));
        editView2.setBackgroundColor(context.getResources().getColor(R.color.defaultFormBackground));
        editView3.setBackgroundColor(context.getResources().getColor(R.color.defaultFormBackground));
}

    public void nextRound() {
        isTrueBackgroud(Status.DEFAULT);
        if(isUseEdit1){
            editView1.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editView1, InputMethodManager.SHOW_IMPLICIT);
        }else if(isUseEdit2){
            editView2.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editView2, InputMethodManager.SHOW_IMPLICIT);
        }else if(isUseEdit3){
            editView3.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editView3, InputMethodManager.SHOW_IMPLICIT);
        }



        needNextRound=false;
        if (position >= maxPosition) {
            position = minPosition;
        }
        IrregularVerbs verb=wordsList.get(position);

        setEngViewText();

        editView1.setEnabled(isUseEdit1);
        editView2.setEnabled(isUseEdit2);
        editView3.setEnabled(isUseEdit3);

        editView1.setText(    (!isUseEdit1) ? verb.infinitive_v1 : "" );
        editView2.setText(    (!isUseEdit2) ? verb.past_simple_v2 : "" );
        editView3.setText(    (!isUseEdit3) ? verb.past_particple_v3 : "" );

        editView1.value= verb.infinitive_v1;
        editView2.value= verb.past_simple_v2;
        editView3.value= verb.past_particple_v3;
}
    public void setEngViewText(){
        IrregularVerbs verb=wordsList.get(position);
        if(isEngValue){
            engView.setText(verb.eng_value);
        }else{
            engView.setText(verb.ru);
        }

    }

    public void setListener(){
        View.OnClickListener click=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(needNextRound && allInputTrue()){
                    ++position;
                    nextRound();
                    return;
                }
                if(allInputTrue()){
                    needNextRound=true;
                    isTrueBackgroud(Status.TRUE);
                }else{
                    needNextRound=false;
                    isTrueBackgroud(Status.FALSE);
                }
            }
        };

        editView1.setOnClickListener(click);
        editView2.setOnClickListener(click);
        editView3.setOnClickListener(click);
        ifNeedOpenNext.setOnClickListener(click);
        TextView.OnEditorActionListener inpit=new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(needNextRound && allInputTrue()){
                        ++position;
                        isTrueBackgroud(Status.DEFAULT);
                        nextRound();
                        return true;
                    }
                    if(allInputTrue()){
                        needNextRound=true;
                        isTrueBackgroud(Status.TRUE);
                    }else{
                        needNextRound=false;
                        isTrueBackgroud(Status.FALSE);
                    }
                }
                return false;
            }
        };
        editView1.setOnEditorActionListener(inpit);
        editView2.setOnEditorActionListener(inpit);
        editView3.setOnEditorActionListener(inpit);
    }
    private void isTrueBackgroud(Status status){
        if(status==Status.DEFAULT){
            if(isUseEdit1){
                editView1.setBackgroundColor(Color.WHITE);
            }
            if(isUseEdit2){
                editView2.setBackgroundColor(Color.WHITE);
            }
            if(isUseEdit3){
                editView3.setBackgroundColor(Color.WHITE);
            }
        }else if (status==Status.FALSE){
            if(isUseEdit1){
                if(!editView1.getText().toString().equals(editView1.value)){
                    editView1.setBackgroundColor(context.getResources().getColor(R.color.error_button));
                }else{
                    editView1.setBackgroundColor(context.getResources().getColor(R.color.trueLayout));
                }
            }
            if(isUseEdit2){
                if(!editView2.getText().toString().equals(editView2.value)){
                    editView2.setBackgroundColor(context.getResources().getColor(R.color.error_button));
                }else{
                    editView2.setBackgroundColor(context.getResources().getColor(R.color.trueLayout));
                }
            }
            if(isUseEdit3){
                if(!editView3.getText().toString().equals(editView3.value)){
                    editView3.setBackgroundColor(context.getResources().getColor(R.color.error_button));
                }else{
                    editView3.setBackgroundColor(context.getResources().getColor(R.color.trueLayout));
                }
            }
        }else if (status==Status.TRUE){
            if(isUseEdit1){
                editView1.setBackgroundColor(context.getResources().getColor(R.color.trueLayout));
            }
            if(isUseEdit2){
                editView2.setBackgroundColor(context.getResources().getColor(R.color.trueLayout));
            }
            if(isUseEdit3){
                editView3.setBackgroundColor(context.getResources().getColor(R.color.trueLayout));
            }
        }else{
            ErrorActivity.throwError(context,"Непредвиденный status в установке фона в EditGameXFromThree:<"+status+">;");
        }
    }

    private boolean allInputTrue(){
        if(editView1.getText().toString()!=null
        &&editView2.getText().toString()!=null
        &&editView3.getText().toString()!=null
        && editView1.getText().toString().equals(editView1.value)
        && editView2.getText().toString().equals(editView2.value)
        && editView3.getText().toString().equals(editView3.value) ){
            return true;
        }
        return  false;
    }


}
