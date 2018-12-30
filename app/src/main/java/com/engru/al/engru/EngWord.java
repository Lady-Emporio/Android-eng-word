package com.engru.al.engru;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class EngWord extends BaseORM {
    int id;
    String eng;
    String ru;
    public EngWord(){
        id=-1;
        eng="";
        ru="";
    }
    static class Table{
        public static String TABLE_NAME="engWordsTables";
        public static String ID="_id";
        public static String ID_AS="EngWord_id";
        public static String ENG="eng";
        public static String RU="ru";
        public static String CREATE_TABLES="CREATE TABLE "+ TABLE_NAME + " ( "
                + " _id INTEGER PRIMARY KEY, "
                + ENG + " TEXT, "
                + RU + " TEXT "
                +")";
    }
    @Override public String save(){//if good return null else return Error.toString
        ContentValues fields = new ContentValues();
        fields.put(EngWord.Table.ENG, eng);
        fields.put(EngWord.Table.RU, ru);
        try{
            if(-1==id){
                id=(int)db.insert(EngWord.Table.TABLE_NAME, null, fields);
            }else{
                db.update(EngWord.Table.TABLE_NAME, fields, "_id = ? ", new String[]{ Integer.toString(id)});
            }
        }catch (Exception e){
            return e.toString();
        }
        return null;
    }
    @Override public String delete(){//if good return null else return Error.toString
        try{
            db.delete(EngWord.Table.TABLE_NAME , "_id = ? ", new String[]{ Integer.toString(id)});
        }
        catch (Exception e){
            return e.toString();
        }
        return null;
    }
    public static EngWord get(int id){//if good return EngWord else return null
        Cursor cursor = db.rawQuery("select * from " + EngWord.Table.TABLE_NAME +"  where _id = ?" , new String[]{ Integer.toString(id)});
        if(!cursor.moveToNext()){
            return null;
        }else{
            EngWord gettingObject=new EngWord();
            gettingObject.id=cursor.getInt(cursor.getColumnIndex( EngWord.Table.ID ));
            gettingObject.eng=cursor.getString(cursor.getColumnIndex( EngWord.Table.ENG ));
            gettingObject.ru=cursor.getString(cursor.getColumnIndex( EngWord.Table.RU ));
            return gettingObject;
        }
    }
}
