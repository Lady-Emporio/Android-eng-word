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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class ScrollActivity extends Activity {
    TextView idView;
    EditText editName;
    ListView engIn;
    Button saveButton;
    int id;
    String massAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        idView=(TextView)findViewById(R.id.idView);
        editName=(EditText)findViewById(R.id.editName);
        engIn=(ListView)findViewById(R.id.engIn);
        saveButton=(Button)findViewById(R.id.saveButton);
        this.id=getIntent().getIntExtra("ID",-1);
        massAdd=getIntent().getStringExtra(SettingsActivity.MASS_ADD_IN_SCROLL);
        reloadAllViewAndData();
    }
    @Override protected void onRestart (){
        super.onRestart();
        reloadAllViewAndData();
    }
    public static void openScrollActivity(Context from, int id){// if id=-1 then new object
        Intent intent = new Intent( from,ScrollActivity.class );
        intent.putExtra("ID",id);
        from.startActivity( intent );
    }
    private void fillView(Scroll value){
        if(value!=null){
            idView.setText("№ "+Integer.toString(value.id)+"!");
            editName.setText(value.name);
            saveButton.setText("Save");
        }else{
            idView.setText("New object");
            saveButton.setText("Create");
        }
    }
    public void reloadAllViewAndData(){
        if(id==-1){
            fillView(null);
            return;
        }else{
            fillView(Scroll.get(id));
            if(massAdd==null){
            Cursor cursor = BaseORM.db.rawQuery("SELECT "
                    + ScrollEngWordsAdapter.Table.TABLE_NAME+"._id, "
                    + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+ScrollEngWordsAdapter.Table.ENG_ID +" , "
                    + EngWord.Table.TABLE_NAME+"."+EngWord.Table.ENG
                    + " from " + ScrollEngWordsAdapter.Table.TABLE_NAME
                    + " LEFT JOIN " + EngWord.Table.TABLE_NAME + " ON "
                    + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+ScrollEngWordsAdapter.Table.ENG_ID + " = " + EngWord.Table.TABLE_NAME+"."+EngWord.Table.ID
                    + " WHERE " + ScrollEngWordsAdapter.Table.SCROLL_ID + " = ? ;", new String[]{Integer.toString(id)});
            engIn.setAdapter(new scrollToWordCursorAdapterForMove(this,cursor));
            engIn.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Cursor cursor = ((scrollToWordCursorAdapterForMove)parent.getAdapter()).getCursor();
                        int eng_id = cursor.getInt(cursor.getColumnIndex(ScrollEngWordsAdapter.Table.ENG_ID));
                        EngWordActivity.openEngWordActivity(ScrollActivity.this,eng_id);
                 }
                });
            return;
            }else{
                Cursor cursor=getMassAddCursor(id);
                scrollToWordCursorAdapterForMove adapter=new scrollToWordCursorAdapterForMove(this,cursor);
                adapter.mass_add=true;
                engIn.setAdapter(adapter);
                engIn.setOnItemClickListener(new OnItemClickListener() {
                    //if have:Color.YELLOW
                    //иначе Color.GREEN
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        TextView v= ((TextView)((LinearLayout)view).findViewById(R.id.eng));
                        if (v.getCurrentTextColor()==Color.YELLOW){
                            Cursor cursor=((scrollToWordCursorAdapterForMove)parent.getAdapter()).getCursor();
                            int oldAdapter_id = cursor.getInt(cursor.getColumnIndex(ScrollEngWordsAdapter.Table.SCROLL_COUNT_AS));
                            ScrollEngWordsAdapter oldForDelete = ScrollEngWordsAdapter.get(oldAdapter_id);
                            if (oldForDelete==null){
                                ErrorActivity.throwError(ScrollActivity.this,"Не получилось получить ScrollEngWordsAdapter по id");
                                return;
                            }
                            String status=oldForDelete.delete();
                            if (status!=null){
                                ErrorActivity.throwError(ScrollActivity.this,"Попытка удалить\n"+status);
                            }
                            Cursor newCursor=getMassAddCursor(ScrollActivity.this.id);
                            ((scrollToWordCursorAdapterForMove)parent.getAdapter()).changeCursor( newCursor );
                        }else if (v.getCurrentTextColor()==Color.GREEN){
                            ScrollEngWordsAdapter newObject = new ScrollEngWordsAdapter();
                            newObject.eng_id=(int)id;
                            newObject.scroll_id=ScrollActivity.this.id;
                            String status=newObject.save();
                            if (status!=null){
                                ErrorActivity.throwError(ScrollActivity.this,status);
                            }
                            Cursor newCursor=getMassAddCursor(ScrollActivity.this.id);
                            ((scrollToWordCursorAdapterForMove)parent.getAdapter()).changeCursor( newCursor );
                        }
                    }
                });
                return;
            }
        }
    }
    public void ScrollActivity_reload(View view){
        reloadAllViewAndData();
    }
    public static Cursor getMassAddCursor(int id){
        Cursor cursor = BaseORM.db.rawQuery("SELECT "
                        + EngWord.Table.TABLE_NAME+"."+EngWord.Table.ID+","
                        + EngWord.Table.TABLE_NAME+"."+EngWord.Table.ENG + " , "
                        + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+ScrollEngWordsAdapter.Table.ID + " AS "+ScrollEngWordsAdapter.Table.SCROLL_COUNT_AS
                        + " from " + EngWord.Table.TABLE_NAME
                        + " LEFT JOIN " + ScrollEngWordsAdapter.Table.TABLE_NAME + " ON "
                        + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+ScrollEngWordsAdapter.Table.ENG_ID + " = " + EngWord.Table.TABLE_NAME+"."+EngWord.Table.ID
                        + " AND " + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+ScrollEngWordsAdapter.Table.SCROLL_ID + " = ? "
                        + " ORDER BY "+ EngWord.Table.TABLE_NAME+"."+EngWord.Table.ENG +";"

                ,new String[]{Integer.toString(id)});
        return cursor;
    }
    public void ScrollActivity_save(View view){
        Scroll newObject=new Scroll();
        newObject.name=editName.getText().toString();
        newObject.id=id;
        String status=newObject.save();
        if(status!=null){
            ErrorActivity.throwError(ScrollActivity.this,status);
            return;
        }
        this.id=newObject.id;
        reloadAllViewAndData();
    }
    public void ScrollActivity_delete(View view){
        if(id==-1){
            this.finish();
        }else{
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Scroll needDelete=new Scroll();
                            needDelete.id=id;
                            String status=needDelete.delete();
                            if(status!=null){
                                ErrorActivity.throwError(ScrollActivity.this,status);
                                return;
                            }
                            ScrollActivity.this.finish();
                            return;
                            //break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                            //break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }
}
