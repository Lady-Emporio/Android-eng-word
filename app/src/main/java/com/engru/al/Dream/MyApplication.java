package com.engru.al.Dream;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if (BaseORM.get_db() == null) {
                BaseORM.setDB(new HelperSQL(this).getWritableDatabase());
            }
        }catch (Exception e){
            ErrorActivity.throwError(this,"Не получается получить базу в BaseORM.setDB(new HelperSQL(this).getWritableDatabase(). Невозможно дальше продолжать"+e.toString());
        }
        LaunchExampleFileJson();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    public void LaunchExampleFileJson(){
        try{
            String exampleText="{\n" +
                    "\t\""+EngWord.Table.TABLE_NAME+"\":[\n" +
                    "\t\t{\""+EngWord.Table.ENG+"\":\"horn\",\""+EngWord.Table.RU+"\":\"рог\",\""+EngWord.Table.ENG_VALUE+"\":\"one of the two hard, pointed growths on the heads of cows, goats, and some other animals\",\""+EngWord.Table.EXAMPLE+"\":\"And then as they grow older, the horns grow forward.\"},\n" +
                    "\t\t{\""+EngWord.Table.ENG+"\":\"flutter\",\""+EngWord.Table.RU+"\":\"колыхаться, развеваться\",\""+EngWord.Table.ENG_VALUE+"\":\"to move quickly and gently up and down or from side to side in the air, or to make something move in this way\",\""+EngWord.Table.EXAMPLE+"\":\"The flag was fluttering in the breeze.\"},\n" +
                    "\t\t{\""+EngWord.Table.ENG+"\":\"shy\",\""+EngWord.Table.RU+"\":\"застенчивый, стеснительный\",\""+EngWord.Table.ENG_VALUE+"\":\"not confident, especially about meeting or talking to new people\",\""+EngWord.Table.EXAMPLE+"\":\"He was too shy to say anything to her.\"}\n" +
                    "\t]\n" +
                    "}";
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                ErrorActivity.throwError(this,"Нет разрешения на создание файла. Непредвиденная ошибка");
                return;
            }
            String absolutePathDir=this.getExternalFilesDir(null).getAbsolutePath();
            String fileName=absolutePathDir+File.separator+"example.json";
            File file=new File(fileName);
            if(!file.exists()){
                if(!file.createNewFile()){
                    ErrorActivity.throwError(this,"Не получается создать example.json для примера загрузки из файла. Разрешение есть, а file.createNewFile() выдает ошибку");
                }
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write(exampleText);
                fileWriter.close();
            }
        }catch(Exception e){
            ErrorActivity.throwError(this,"Не получается создать example.json для примера загрузки из файла:\n"+e.toString());
        }
    }
}