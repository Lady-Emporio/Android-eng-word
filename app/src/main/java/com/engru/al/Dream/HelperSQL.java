package com.engru.al.Dream;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.engru.al.Dream.Verbs.IrregularVerbs;

public class HelperSQL extends SQLiteOpenHelper {
    private static int version=1;
    private static String My_DB_NAME="My_db";
    private Context context;
    public HelperSQL(Context context) {
        super(context, My_DB_NAME, null, version);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        insertDefaultValues(db);
        MyApplication.downloadFromAssets(context,db);
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
        db.execSQL("DROP TABLE IF EXISTS "+ EngWord.Table.DOUBLE_TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS "+ Scroll.Table.TABLE_NAME + " ;");
        db.execSQL("DROP TABLE IF EXISTS "+ ScrollEngWordsAdapter.Table.TABLE_NAME+" ;");
        db.execSQL("DROP TABLE IF EXISTS "+ IrregularVerbs.Table.TABLE_NAME+" ;");
        db.execSQL("DROP INDEX IF EXISTS "+ IrregularVerbs.Table.INDEX_FOR_SORT_NAME+" ;");
    }
    private void insertDefaultValues(SQLiteDatabase db){
        String [][] engWords={
                {"house","здание","A building where people live, usually one family or group","We went to my aunt's house for dinner."},
                {"list","Список","A series of names, numbers, or items that are written one below the other","Make a list of everything you need."},
                {"coal","Уголь","A hard, black substance that is dug from under the ground and burnt as fuel","a lump of coal"},
                {"plant","Растение / завод","A living thing that grows in the soil or water and has leaves and roots, especially one that is smaller than a tree","Have you watered the plants?"},
                {"east","Восток","The direction that you face to see the sun rise","Which way's east?"},
                {"cancer","Рак / бичь / бедствие","A serious disease that is caused when cells in the body grow in a way that is uncontrolled and not normal","His wife died of cancer."},
                {"delightful","Восхитительный, очаровательный","Very pleasant, attractive, or enjoyable","We had a delightful evening."},
                {"goal","Цель ( as dream )","Something you want to do successfully in the future","Andy's goal is to run in the New York Marathon."},
                {"nearly","Почти, около, чуть не","Almost, or not completely:","It's been nearly three months since my last haircut | I've nearly finished that book you lent me."},
                {"versatility","Разносторонний, универсальный.","Having many different skills | useful for doing a lot of different things","a versatile player/performer"},
                {"design","Конструкция, дизайн, проектирование","The way in which something is planned and made","There was a fault in the design of the aircraft."},
                {"closely","Тесно, близко, вплотную","They are very similar to each other or there is a relationship between them. | you work together a lot.","The two languages are closely related."},
                {"relate","Относиться, связывать (связь)","To be connected, or to find or show the connection between two or more things","How do the two proposals relate?"},
                {"belong","Принадлежать, относиться","To feel happy and comfortable in a place or with a group of people","I think these cups belong in the other cupboard."},
                {"aspect","Точка зрения, сторона","One part of a situation, problem, subject, etc","His illness affects almost every aspect of his life."},
                {"available","Доступный","You can use it or get it | hey are not busy and so are able to do something","This information is available free on the Internet | No one from the company was available to comment on the accident."},
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
            newValues.put(EngWord.Table.ENG_VALUE, word[2]);
            newValues.put(EngWord.Table.EXAMPLE, word[3]);
            db.insert(EngWord.Table.TABLE_NAME,null,newValues);
        }
        for(String scroll: scrolls){
            newValues.clear();
            newValues.put(Scroll.Table.NAME, scroll);
            db.insert(Scroll.Table.TABLE_NAME,null,newValues);
        }
        IrregularVerbs.Table.insertDefaultValues(db);
    }
    private void createTables(SQLiteDatabase db){
        db.execSQL(EngWord.Table.CREATE_TABLES( EngWord.Table.TABLE_NAME ));
        db.execSQL(EngWord.Table.CREATE_TABLES( EngWord.Table.DOUBLE_TABLE_NAME ));
        db.execSQL(Scroll.Table.CREATE_TABLES);
        db.execSQL(ScrollEngWordsAdapter.Table.CREATE_TABLES);
        db.execSQL(IrregularVerbs.Table.CREATE_TABLES);
        db.execSQL(IrregularVerbs.Table.INDEX_FOR_SORT_SQL);
    }
    @Override public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

}
