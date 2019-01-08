package com.engru.al.Dream;

import android.content.ContentValues;
import android.database.Cursor;

public class Scroll extends BaseORM {
    public int id;
    public String name;
    public Scroll(){
        id=-1;
        name="";
    }
    static class Table{
        public static String TABLE_NAME="scrolls";
        public static String ID="_id";
        public static String NAME="name";
        public static String CREATE_TABLES="CREATE TABLE "+ TABLE_NAME + " ( "
                + " _id INTEGER PRIMARY KEY, "
                + NAME + " TEXT "
                +");";
    }
    @Override public String save(){//if good return null else return Error.toString
        ContentValues fields = new ContentValues();
        fields.put(Scroll.Table.NAME, name);
        try{
            if(-1==id){
                id=(int)get_db().insert(Scroll.Table.TABLE_NAME, null, fields);
            }else{
                get_db().update(Scroll.Table.TABLE_NAME, fields, "_id = ? ", new String[]{ Integer.toString(id)});
            }
        }catch (Exception e){
            return e.toString();
        }
        return null;
    }
    @Override public String delete(){//if good return null else return Error.toString
        try{
            get_db().delete(Scroll.Table.TABLE_NAME , "_id = ? ", new String[]{ Integer.toString(id)});
        }
        catch (Exception e){
            return e.toString();
        }
        return null;
    }
    public static Scroll get(int id){//if good return Scroll else return null
        Cursor cursor = get_db().rawQuery("select * from " + Scroll.Table.TABLE_NAME +"  where _id = ?" , new String[]{ Integer.toString(id)});
        if(!cursor.moveToNext()){
            return null;
        }else{
            Scroll gettingObject=new Scroll();
            gettingObject.id=cursor.getInt(cursor.getColumnIndex( Scroll.Table.ID ));
            gettingObject.name=cursor.getString(cursor.getColumnIndex( Scroll.Table.NAME ));
            return gettingObject;
        }
    }
}
