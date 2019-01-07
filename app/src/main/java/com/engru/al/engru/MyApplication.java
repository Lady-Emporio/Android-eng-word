package com.engru.al.engru;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
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
                    "\t\"engWordsTables\":[\n" +
                    "\t\t{\"eng\":\"horn\",\"ru\":\"рог\",\"eng_value\"=\"one of the two hard, pointed growths on the heads of cows, goats, and some other animals\",\"EXAMPLE\"=\"\"},\n" +
                    "\t\t{\"eng\":\"flutter\",\"ru\":\"колыхаться, развеваться\",\"eng_value\"=\"to move quickly and gently up and down or from side to side in the air, or to make something move in this way\",\"EXAMPLE\"=\"The flag was fluttering in the breeze.\"},\n" +
                    "\t\t{\"eng\":\"shy\",\"ru\":\"застенчивый, стеснительный\",\"eng_value\"=\"not confident, especially about meeting or talking to new people\",\"EXAMPLE\"=\"He was too shy to say anything to her.\"}\n" +
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