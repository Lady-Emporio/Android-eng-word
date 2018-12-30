package com.engru.al.engru;

import android.content.ContentValues;
import android.database.Cursor;

public class ScrollEngWordsAdapter extends BaseORM {
    int id;
    int eng_id;
    int scroll_id;
    public ScrollEngWordsAdapter(){
        id=-1;
        eng_id=-1;
        scroll_id=-1;
    }
    static class Table{
        public final static String TABLE_NAME="scrolls_words_adapter";
        public final static String ID="_id";
        public final static String ID_AS="scroll_adapter_id";
        public final static String SCROLL_COUNT_AS="scroll_count";
        public final static String ENG_ID="eng_word";
        public final static String SCROLL_ID="scroll";
        public final static String CREATE_TABLES="CREATE TABLE "+ TABLE_NAME + " ( "
                + " _id INTEGER PRIMARY KEY, "
                + ENG_ID + " INTEGER REFERENCES "+EngWord.Table.TABLE_NAME + " ( " +EngWord.Table.ID+ " ) ON DELETE CASCADE,"
                + SCROLL_ID + " INTEGER REFERENCES "+Scroll.Table.TABLE_NAME + " ( " +Scroll.Table.ID+ " ) ON DELETE CASCADE"
                +");";
        public final static String QUERY_COUNT_SCROLL_ENG="SELECT " + Scroll.Table.TABLE_NAME+"."+Scroll.Table.ID+ " , "
                + Scroll.Table.TABLE_NAME+"."+Scroll.Table.NAME+ " , "
                + " COUNT ( " + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+ScrollEngWordsAdapter.Table.ID +") AS " +ScrollEngWordsAdapter.Table.SCROLL_COUNT_AS
                + " FROM " + Scroll.Table.TABLE_NAME
                + " LEFT JOIN " + ScrollEngWordsAdapter.Table.TABLE_NAME + " ON "
                + Scroll.Table.TABLE_NAME+"."+Scroll.Table.ID + " = " + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+Table.SCROLL_ID
                + " GROUP BY "+ Scroll.Table.TABLE_NAME+"."+Scroll.Table.NAME + " , "+ Scroll.Table.TABLE_NAME+"."+Scroll.Table.ID +
                " ORDER BY "+  " COUNT ( " + ScrollEngWordsAdapter.Table.TABLE_NAME+"."+ScrollEngWordsAdapter.Table.ID + ") DESC ";
    }
    @Override public String save(){//if good return null else return Error.toString
        ContentValues fields = new ContentValues();
        fields.put(ScrollEngWordsAdapter.Table.ENG_ID, eng_id);
        fields.put(ScrollEngWordsAdapter.Table.SCROLL_ID, scroll_id);
        try{
            if(-1==id){
                id=(int)db.insert(ScrollEngWordsAdapter.Table.TABLE_NAME, null, fields);
            }else{
                db.update(ScrollEngWordsAdapter.Table.TABLE_NAME, fields, " _id = ? ", new String[]{ Integer.toString(id)});
            }
        }catch (Exception e){
            return e.toString();
        }
        return null;
    }
    @Override public String delete(){//if good return null else return Error.toString
        try{
            db.delete(ScrollEngWordsAdapter.Table.TABLE_NAME , "_id = ? ", new String[]{ Integer.toString(id)});
        }
        catch (Exception e){
            return e.toString();
        }
        return null;
    }
    public static ScrollEngWordsAdapter get(int id){//if good return ScrollEngWordsAdapter else return null
        Cursor cursor = db.rawQuery("select * from " + ScrollEngWordsAdapter.Table.TABLE_NAME +"  where _id = ?" , new String[]{ Integer.toString(id)});
        if(!cursor.moveToNext()){
            return null;
        }else{
            ScrollEngWordsAdapter gettingObject=new ScrollEngWordsAdapter();
            gettingObject.id=cursor.getInt(cursor.getColumnIndex( ScrollEngWordsAdapter.Table.ID ));
            gettingObject.eng_id=cursor.getInt(cursor.getColumnIndex( ScrollEngWordsAdapter.Table.ENG_ID ));
            gettingObject.scroll_id=cursor.getInt(cursor.getColumnIndex( ScrollEngWordsAdapter.Table.SCROLL_ID ));
            return gettingObject;
        }
    }
}
