package com.engru.al.Dream;


import android.content.ContentValues;
import android.database.Cursor;

public class EngWord extends BaseORM {
    int id;
    String eng;
    String ru;
    String eng_value;
    String example;
    public EngWord(){
        id=-1;
        eng="";
        ru="";
        eng_value="";
        example="";
    }
    static class Table{
        public static final String TABLE_NAME="eng_words_tables";
        public static final String ID="_id";
        public static final String DATE_CREATE="date_create";
        public static final String ID_AS="EngWord_id";
        public static final String ENG="eng";
        public static final String RU="ru";
        public static final String ENG_VALUE="eng_value";
        public static final String EXAMPLE="example";;
        public static final String CREATE_TABLES="CREATE TABLE "+ TABLE_NAME + " ( "
                + " _id INTEGER PRIMARY KEY, "
                + DATE_CREATE + " TEXT DEFAULT (DATETIME('now')), " //A date in a format like "YYYY-MM-DD HH:MM:SS.SSS"
                + ENG + " TEXT, "
                + ENG_VALUE + " TEXT, "
                + EXAMPLE + " TEXT, "
                + RU + " TEXT "
                +")";
    }
    @Override public String save(){//if good return null else return Error.toString
        ContentValues fields = new ContentValues();
        fields.put(EngWord.Table.ENG, eng);
        fields.put(EngWord.Table.RU, ru);
        fields.put(EngWord.Table.ENG_VALUE, eng_value);
        fields.put(EngWord.Table.EXAMPLE, example);
        try{
            if(-1==id){
                id=(int)get_db().insert(EngWord.Table.TABLE_NAME, null, fields);
            }else{
                get_db().update(EngWord.Table.TABLE_NAME, fields, "_id = ? ", new String[]{ Integer.toString(id)});
            }
        }catch (Exception e){
            return e.toString();
        }
        return null;
    }
    @Override public String delete(){//if good return null else return Error.toString
        try{
            get_db().delete(EngWord.Table.TABLE_NAME , "_id = ? ", new String[]{ Integer.toString(id)});
        }
        catch (Exception e){
            return e.toString();
        }
        return null;
    }
    public static EngWord get(int id){//if good return EngWord else return null
        Cursor cursor = get_db().rawQuery("select * from " + EngWord.Table.TABLE_NAME +"  where _id = ?" , new String[]{ Integer.toString(id)});
        if(!cursor.moveToNext()){
            return null;
        }else{
            EngWord gettingObject=new EngWord();
            gettingObject.id=cursor.getInt(cursor.getColumnIndex( EngWord.Table.ID ));
            gettingObject.eng=cursor.getString(cursor.getColumnIndex( EngWord.Table.ENG ));
            gettingObject.ru=cursor.getString(cursor.getColumnIndex( EngWord.Table.RU ));
            gettingObject.eng_value=cursor.getString(cursor.getColumnIndex( EngWord.Table.ENG_VALUE ));
            gettingObject.example=cursor.getString(cursor.getColumnIndex( EngWord.Table.EXAMPLE ));
            return gettingObject;
        }
    }
}
