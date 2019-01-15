package com.engru.al.Dream;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if (BaseORM.get_db() == null) {
                HelperSQL initDB=new HelperSQL(this);
                BaseORM.setDB(initDB.getWritableDatabase());
                EngWord x=EngWord.get(2);
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

    public static void downloadFromAssets(Context context, SQLiteDatabase db){
        AssetManager am = context.getAssets();
        String []path;
        try{
            path=am.list("");
        }catch (Exception e){
            Toast.makeText(context, "При запуске загрузить из файлов данные не получилось - " +
                            "не на что не влияет, просто список слов по умолчанию будет поменьше. " +
                            "Ручками надо будет загрузить. Но ошибка странная - не получилось получить список файлов в Assets директории. Удивительно. Error:"+e.toString(),
                    Toast.LENGTH_LONG).show();
            return;
        }
        for(int n=0;n!=path.length;++n){
            String fileName=path[n];
            if(!fileName.endsWith(".json")){
                continue;
            }
            try {
                InputStream is = am.open(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();
                String result = sb.toString();

                JSONObject object=new JSONObject(result);
                JSONArray jArray = object.getJSONArray(EngWord.Table.TABLE_NAME);
                for (int i=0;i!=jArray.length();++i){
                    JSONObject newWord=(JSONObject)jArray.get(i);
                    String eng="";
                    String ru="";
                    String engValue="";
                    String example="";
                    if(newWord.has(EngWord.Table.ENG)){
                        eng=newWord.getString(EngWord.Table.ENG);
                    }
                    if(newWord.has(EngWord.Table.RU)){
                        ru=newWord.getString(EngWord.Table.RU);
                    }
                    if(newWord.has(EngWord.Table.ENG_VALUE)){
                        engValue=newWord.getString(EngWord.Table.ENG_VALUE);
                    }
                    if(newWord.has(EngWord.Table.EXAMPLE)){
                        example=newWord.getString(EngWord.Table.EXAMPLE);
                    }
                    EngWord word=new EngWord();
                    word.eng=eng;
                    word.ru=ru;
                    word.eng_value=engValue;
                    word.example=example;
                    String status=word.save(db);
                    if(status!=null){
                        Log.d("q1",status);
                        continue;
                    }
                }
            }catch(Exception e){
                Toast.makeText(context, "Не получилось по умолчанию загрузить данные из файла: "+
                                fileName+"| Ни на что не влияет. Просто начальных слов будет меньше. "+e.toString(),
                        Toast.LENGTH_LONG).show();
                continue;
            }


            }


    }

}