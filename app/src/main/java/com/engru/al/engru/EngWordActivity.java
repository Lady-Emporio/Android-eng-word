package com.engru.al.engru;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EngWordActivity extends Activity {
    int id;
    TextView idView;
    EditText engEdit;
    EditText ruEdit;
    ListView listScrolls;
    Button saveButton;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eng_word);
        idView=(TextView)findViewById(R.id.idView);
        engEdit=(EditText)findViewById(R.id.engEdit);
        ruEdit=(EditText)findViewById(R.id.ruEdit);
        listScrolls=(ListView)findViewById(R.id.listScrolls);
        saveButton=(Button)findViewById(R.id.saveButton);
        this.id=getIntent().getIntExtra("ID",-1);
        this.reloadAllViewAndData();
    }
    @Override protected void onRestart (){//Не должно вызываться, но вдруг. only Scroll neeed restart
        super.onRestart();
        this.reloadAllViewAndData();
    }
    public static void openEngWordActivity(Context from, int id){// if id=-1 then new object
        Intent intent = new Intent( from,EngWordActivity.class );
        intent.putExtra("ID",id);
        from.startActivity( intent );
    }
    private void fillView(EngWord value){
        if(value!=null){
            idView.setText("№ "+Integer.toString(value.id)+"!");
            engEdit.setText(value.eng);
            ruEdit.setText(value.ru);
            saveButton.setText("save");
        }else{
            idView.setText("New!!!");
            engEdit.setText("");
            ruEdit.setText("");
            saveButton.setText("create");
        }
    }
    public void reloadAllViewAndData(){
        if(id==-1){
            fillView(null);
            return;
        }else{
            EngWord act=EngWord.get(id);
            fillView(act);
            Cursor cursor=getNewCursorForList(id);
            listScrolls.setAdapter(new EngWordCursorChooseList(this,cursor));
            listScrolls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    TextView t=(TextView)view.findViewById(R.id.viewFielnd);

                    int color=t.getCurrentTextColor();
                    //IF WORD IN LIST viewText.setBackgroundColor(Color.YELLOW)
                    //IF NOT viewText.setBackgroundColor(Color.BLUE)
                    Cursor cursor=(Cursor)parent.getAdapter().getItem(position);
                    int eng_id=cursor.getInt(cursor.getColumnIndex(EngWord.Table.ID_AS));
                    if(color==Color.BLUE){
                        ScrollEngWordsAdapter newObject = new ScrollEngWordsAdapter();
                        newObject.eng_id=eng_id;
                        newObject.scroll_id=(int)id;
                        String status=newObject.save();
                        if (status!=null){
                            ErrorActivity.throwError(EngWordActivity.this,status);
                        }
                        //t.setTextColor(Color.YELLOW);
                        Cursor newCursor=getNewCursorForList(eng_id);
                        ((EngWordCursorChooseList)parent.getAdapter()).changeCursor( newCursor );


                    }else if(color==Color.YELLOW){
                        int oldAdapter_id = cursor.getInt(cursor.getColumnIndex(ScrollEngWordsAdapter.Table.ID_AS));
                        ScrollEngWordsAdapter oldForDelete = ScrollEngWordsAdapter.get(oldAdapter_id);
                        if (oldForDelete==null){
                            ErrorActivity.throwError(EngWordActivity.this,"Не получилось получить ScrollEngWordsAdapter по id");
                            return;
                        }
                        String status=oldForDelete.delete();
                        if (status!=null){
                            ErrorActivity.throwError(EngWordActivity.this,status);
                        }
                        //t.setTextColor(Color.BLUE);
                        Cursor newCursor=getNewCursorForList(eng_id);
                        ((EngWordCursorChooseList)parent.getAdapter()).changeCursor( newCursor );
                    }
                }
            });
            return;
        }
    }
    public static Cursor getNewCursorForList(int id){
        Cursor cursor=BaseORM.get_db().rawQuery("SELECT "
                        + Scroll.Table.TABLE_NAME+"."+Scroll.Table.ID + " , "
                        + Scroll.Table.NAME + " , " + " ifnull( "
                        + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+ScrollEngWordsAdapter.Table.ID  + ",-1) AS "+ ScrollEngWordsAdapter.Table.ID_AS + " , "
                        + " ? AS " + EngWord.Table.ID_AS
                        + " FROM "+ Scroll.Table.TABLE_NAME
                        + " LEFT JOIN "+ ScrollEngWordsAdapter.Table.TABLE_NAME
                        + " ON " + ScrollEngWordsAdapter.Table.TABLE_NAME+"." +ScrollEngWordsAdapter.Table.SCROLL_ID
                        + " = " + Scroll.Table.TABLE_NAME+"."+Scroll.Table.ID
                        + " AND " + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+ScrollEngWordsAdapter.Table.ENG_ID + " = ? ;"
                ,new String[]{Integer.toString(id),Integer.toString(id)});
        return cursor;
    }
    public void EngWordActivity_save(View view){
        EngWord newObject=new EngWord();
        newObject.eng=engEdit.getText().toString();
        newObject.ru=ruEdit.getText().toString();
        newObject.id=id;
        String status=newObject.save();
        if(status!=null){
            ErrorActivity.throwError(EngWordActivity.this,status);
            return;
        }
        this.id=newObject.id;
        reloadAllViewAndData();

    }
    public void EngWordActivity_reload(View view){
        reloadAllViewAndData();
    }
    public void EngWordActivity_delete(View view){
        if(id==-1){
            this.finish();
        }else{
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            EngWord needDelete=new EngWord();
                            needDelete.id=id;
                            String status=needDelete.delete();
                            if(status!=null){
                                ErrorActivity.throwError(EngWordActivity.this,status);
                                return;
                            }
                            EngWordActivity.this.finish();
                            return;
                        //break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                        //break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?").setPositiveButton("DELETE", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }
}
