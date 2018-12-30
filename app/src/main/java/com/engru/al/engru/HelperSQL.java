package com.engru.al.engru;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HelperSQL extends SQLiteOpenHelper {
    private static int version=1;

    public HelperSQL(Context context) {
        super(context, "My_db", null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        insertDefaultValues(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }
    @Override public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }
    private void dropAllTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+ EngWord.Table.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS "+ Scroll.Table.TABLE_NAME + " ;");
        db.execSQL("DROP TABLE IF EXISTS "+ ScrollEngWordsAdapter.Table.TABLE_NAME+" ;");
    }
    private void insertDefaultValues(SQLiteDatabase db){
        String [][] engWords={
                {"house","здание"},
                {"list","Список"},
                {"coal ","уголь"},
                {"plant","завод / растение"},
                {"east ","восток"},
                {"orient","восток"},
                {"cancer ","рак / бичь / бедствие"},
                {"delightful","восхитительный / очаровательный"},
                {"purpose","цель ( причина )"},
                {"aim","цель ( для достижения )"},
                {"goal","цель ( as dream )"},
                {"target","цель ( количество )"},
                {"nearly","около"},
                {"versatility","многосторонность, гибкость."},
                {"design","конструкция / дизайн / проектирование"},
                {"closely","тесно / близко / вплотную"},
                {"relate","относиться / связывать (связь)"},
                {"belong","принадлежать / относиться"},
                {"understanding","понимание"},
                {"understand","понимаю / уяснить"},
                {"aspect","точка зрения / сторона"},
                {"available ","доступный"}
        };
        String [] scrolls={
                "Избранное",
                "Основное",
                "Любимое",
                "Самое важное",
                "Повторить"
        };
        ContentValues newValues = new ContentValues();
        for(String [] word: engWords){
            newValues.clear();
            newValues.put(EngWord.Table.ENG, word[0]);
            newValues.put(EngWord.Table.RU, word[1]);
            db.insert(EngWord.Table.TABLE_NAME,null,newValues);
        }
        for(String scroll: scrolls){
            newValues.clear();
            newValues.put(Scroll.Table.NAME, scroll);
            db.insert(Scroll.Table.TABLE_NAME,null,newValues);
        }
    }
    private void createTables(SQLiteDatabase db){
        db.execSQL(EngWord.Table.CREATE_TABLES);
        db.execSQL(Scroll.Table.CREATE_TABLES);
        db.execSQL(ScrollEngWordsAdapter.Table.CREATE_TABLES);
    }
    @Override public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }




}
