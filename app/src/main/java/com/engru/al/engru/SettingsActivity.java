package com.engru.al.engru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.HashMap;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    public final static String ENG_MODE="Eng words";
    public final static String SCROLL_MODE="Scrolls";
    public final static String MASS_ADD_IN_SCROLL="Mass add in Scrolls";
    private final static String GET_KEY_MODE="GET_KEY_MODE";
    boolean order;
    public static String actualMode;
    Spinner chooseMode;
    ListView listDinamic;
    EditText filterView;
    DimanicListCursor adapterForlistDinamic;
    ImageView sortImage;
    FloatingActionButton fab;
    Animation animCrossFadeIn,animCrossFadeOut;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        try {
            chooseMode = (Spinner) findViewById(R.id.chooseMode);
            listDinamic = (ListView) findViewById(R.id.listDinamic);
            filterView = (EditText) findViewById(R.id.filterView);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            sortImage = (ImageView) findViewById(R.id.sortImage);

            animCrossFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_in);
            animCrossFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_out);
            order=true;
            adapterForlistDinamic=new DimanicListCursor(this, null,null);
            listDinamic.setAdapter(adapterForlistDinamic);

            String[] modesList = new String[]{ENG_MODE,SCROLL_MODE,MASS_ADD_IN_SCROLL};
            chooseMode.setAdapter(new CustomSpinnerDropDownView(SettingsActivity.this, R.layout.dinamic_list_item, modesList));

            chooseMode.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    try{
                        filterView.setText("");
                    String item = (String) parent.getItemAtPosition(position);
                    if(item.equals(ENG_MODE)){
                        actualMode=item;
                        changeModeView(null);
                    }else if(item.equals(SCROLL_MODE)){
                        actualMode=item;
                        changeModeView(null);
                    }else if(item.equals(MASS_ADD_IN_SCROLL)){
                        actualMode=item;
                        changeModeView(null);
                    }else{
                        ErrorActivity.throwError(SettingsActivity.this,"Неожиданный mode, который не обрабатывается в onItemSelected.");
                    }
                    }catch(Exception e){
                        ErrorActivity.throwError(SettingsActivity.this,e.toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            listDinamic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(actualMode.equals(ENG_MODE)){
                        EngWordActivity.openEngWordActivity(SettingsActivity.this,(int)id);
                    }else if(actualMode.equals(SCROLL_MODE)){
                        ScrollActivity.openScrollActivity(SettingsActivity.this,(int)id);
                    }else if(actualMode.equals(MASS_ADD_IN_SCROLL)){
                        //ScrollActivity.openScrollActivity(SettingsActivity.this,(int)id);
                        Intent intent = new Intent( SettingsActivity.this,ScrollActivity.class );
                        intent.putExtra("ID",(int)id);
                        intent.putExtra(MASS_ADD_IN_SCROLL,MASS_ADD_IN_SCROLL);
                        SettingsActivity.this.startActivity( intent );
                    }
                }
            });

            Intent x=getIntent();
            String mode=x.getStringExtra(GET_KEY_MODE);
            if(mode!=null){
                switch (mode){
                    case ENG_MODE:
                        actualMode=ENG_MODE;
                        break;
                    case SCROLL_MODE:
                        actualMode=SCROLL_MODE;
                        break;
                }
            }
            for (int position = 0; position < chooseMode.getAdapter().getCount(); position++) {
                if(chooseMode.getAdapter().getItem(position) == actualMode) {
                    chooseMode.setSelection(position);
                    break;
                }
            }
        }catch (Exception e){
            ErrorActivity.throwError(SettingsActivity.this,e.toString());
        }
    }
    @Override protected void onRestart (){
        super.onRestart();
        reloadAllViewAndData();
    }
    public void changeModeView(String filter){
        try{
        if(actualMode.equals(ENG_MODE)){
            setENG_MODE(filter);
            fab.show();
        }else if(actualMode.equals(SCROLL_MODE)){
            setSCROLL_MODE(filter);
            fab.show();
        }else if(actualMode.equals(MASS_ADD_IN_SCROLL)){
            setSCROLL_MODE(filter);
            fab.hide();
        }else{
            ErrorActivity.throwError(SettingsActivity.this,"Неожиданный mode, который не обрабатывается в changeModeView.");
        }
        }catch(Exception e){
            ErrorActivity.throwError(SettingsActivity.this,e.toString());
        }

    }
    private void setENG_MODE(String filter){
        Cursor cursor;
        if(filter ==null){
            String sql="SELECT _id, " + EngWord.Table.ENG + " from " + EngWord.Table.TABLE_NAME + " ORDER BY ";
            sql+=order? (EngWord.Table.ENG + " ; "):(EngWord.Table.DATE_CREATE + " ; ");
            cursor = BaseORM.get_db().rawQuery(sql, null);
        }else{
            filter="%"+filter+"%";
            String sql="SELECT _id, " + EngWord.Table.ENG + " from " + EngWord.Table.TABLE_NAME
                    + " WHERE _id LIKE ? OR " + EngWord.Table.ENG + " LIKE ? " +" ORDER BY ";
            sql+=order?(EngWord.Table.ENG + ";"):(EngWord.Table.DATE_CREATE + " ; ");
            cursor = BaseORM.get_db().rawQuery(sql, new String []{ filter,filter } ) ;
        }
        DimanicListCursor adapter = (DimanicListCursor) listDinamic.getAdapter();
        adapter.columnName=EngWord.Table.ENG;
        adapter.changeCursor(cursor);

    }
    private void setSCROLL_MODE(String filter){
        Cursor cursor;
        if (filter == null){
            cursor = BaseORM.get_db().rawQuery("SELECT _id, " + Scroll.Table.NAME + " from " + Scroll.Table.TABLE_NAME + " ORDER BY "+ Scroll.Table.NAME + ";", null);
        }else{
            filter="%"+filter+"%";
            cursor = BaseORM.get_db().rawQuery("SELECT _id, " + Scroll.Table.NAME + " from " + Scroll.Table.TABLE_NAME
                    + " WHERE _id LIKE ? OR " + Scroll.Table.NAME + " LIKE ? "
                    + " ORDER BY "+ Scroll.Table.NAME + ";", new String []{ filter,filter } );
        }
        DimanicListCursor adapter = (DimanicListCursor) listDinamic.getAdapter();
        adapter.columnName=Scroll.Table.NAME;
        adapter.changeCursor(cursor);
        //listDinamic.setAdapter(new DimanicListCursor(this, cursor,Scroll.Table.NAME ));
    }
    public void useFilter(View view) {
        String filter = filterView.getText().toString();
        if (filter.length() == 0){
            changeModeView(null);
        }else{
            changeModeView(filter);
        }
    }
    public void changeSort(View view) {
        if(actualMode!=ENG_MODE){
            return;
        }
        if(order){
            sortImage.startAnimation(animCrossFadeOut);//исчезновение
            sortImage.setImageResource(R.drawable.baseline_date_range_black_24dp);
            sortImage.startAnimation(animCrossFadeIn);//появление
        }else{
            sortImage.startAnimation(animCrossFadeOut);//исчезновение
            sortImage.setImageResource(R.drawable.baseline_text_rotate_vertical_black_24dp);
            sortImage.startAnimation(animCrossFadeIn);//появление
        }
        order=!order;
        changeModeView(null);
    }
    public static void openSettingsActivity(Context from,String mode){
        Intent intent = new Intent( from,SettingsActivity.class );
        intent.putExtra(GET_KEY_MODE,mode);
        from.startActivity(intent);
    }
    public void fabAddNew(View view){
        if(actualMode.equals(ENG_MODE)){
            EngWordActivity.openEngWordActivity(SettingsActivity.this,-1);
        }else if(actualMode.equals(SCROLL_MODE)){
            ScrollActivity.openScrollActivity(SettingsActivity.this,-1);
        }
    }
    public void reloadAllViewAndData(){
        changeModeView(null);
    }

}
