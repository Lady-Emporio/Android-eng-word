package com.engru.al.Dream.Verbs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.engru.al.Dream.BaseORM;
import com.engru.al.Dream.ErrorActivity;

class HelperIrregularVerbsToString{
    int infinitive_v1;
    int past_simple_v2;
    int past_particple_v3;
    int ru;
    int eng_value;
    int example;
    private static HelperIrregularVerbsToString helper;
    private HelperIrregularVerbsToString(){
        SQLiteDatabase db=BaseORM.get_db();

        infinitive_v1=runSqlForLenRow(db,IrregularVerbs.Table.Infinitive_V1);
        past_simple_v2=runSqlForLenRow(db,IrregularVerbs.Table.PAST_SIMPLE_V2);
        past_particple_v3=runSqlForLenRow(db,IrregularVerbs.Table.PAST_PARTICPLE_V3);
        ru=runSqlForLenRow(db,IrregularVerbs.Table.RU);
        eng_value=runSqlForLenRow(db,IrregularVerbs.Table.ENG_VALUE);
        example=runSqlForLenRow(db,IrregularVerbs.Table.EXAMPLE);
    }
    public static HelperIrregularVerbsToString getHelper(){
        if(helper==null){
            helper=new HelperIrregularVerbsToString();
        }
        return helper;
    }
    private int runSqlForLenRow(SQLiteDatabase db,String name){
        String sql="SELECT max(length( "+ name + " )) FROM "+IrregularVerbs.Table.TABLE_NAME+";";
        Cursor c=db.rawQuery(sql,null);
        c.moveToNext();
        int len=c.getInt(0);
        c.close();
        return len;
    }
    public String appendLeftSpace(String word,int len){
        if (len<word.length()){
            throw new RuntimeException("Проблема в appendLeftSpace с runSqlForLenRow. Длина слова почему-то больше максимальной рассчитанной длины");
        }
        StringBuffer newWord=new StringBuffer();
        int needSpace=len-word.length();
        for(int i=0;i<needSpace;++i){
            newWord.append(" ");
        }
        newWord.append(word);
        return newWord.toString();
    }
}
