package com.engru.al.engru;

import android.database.sqlite.SQLiteDatabase;

public class BaseORM {
    private static SQLiteDatabase db;
    public String save(){return null;}
    public String delete(){return null;}
    public static SQLiteDatabase get_db(){
        return db;
    }
    public static void setDB(SQLiteDatabase new_db){
        db=new_db;
    }

}
