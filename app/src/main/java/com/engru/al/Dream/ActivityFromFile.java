package com.engru.al.Dream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ActivityFromFile extends Activity {
    TextView pathToDir;
    Spinner fileSpinner;
    String absolutePathDir;
    TableLayout tableFromFile;
    ArrayList<EngWord> wordsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_file);
        pathToDir=(TextView)findViewById(R.id.pathToDir);
        fileSpinner=(Spinner)findViewById(R.id.fileSpinner);
        tableFromFile=(TableLayout)findViewById(R.id.tableFromFile);
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            ErrorActivity.throwError(ActivityFromFile.this,"Нет разрешения на создание файла. Непредвиденная ошибка");
            return;
        }
        absolutePathDir=this.getExternalFilesDir(null).getAbsolutePath();
        String helpForView=absolutePathDir+"\n"+getString(R.string.findsJson);
        pathToDir.setText( helpForView );
        wordsList=new ArrayList<EngWord>();
        refreshFilesAndDownloadInSpinner();
        tableLayputAddHeaders();

    }

    public static void openActivityFromFile(Context from){
        Intent intent = new Intent( from,ActivityFromFile.class );
        from.startActivity( intent );
    }
    public void ActivityFromFileChooseFile(View v){
        wordsList.clear();
        String fileName=absolutePathDir+File.separator+fileSpinner.getSelectedItem().toString();
        File file=new File(fileName);
        String result;
        try{
            FileInputStream fin = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            result= sb.toString();;
            fin.close();
        }catch (Exception e){
            ErrorActivity.throwError(ActivityFromFile.this,"Не получается прочитать выбранный файл:\n"+e.toString());
            return;
        }
        JSONObject object;
        try{
            object=new JSONObject(result);
            JSONArray jArray = object.getJSONArray(EngWord.Table.TABLE_NAME);
            tableFromFile.removeAllViewsInLayout();
            tableLayputAddHeaders();
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

                TableRow row=new TableRow(this);
                TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
                llp.setMargins(0, 0, 5, 5);//2px right-margin

                row.setBackgroundColor(Color.BLACK);//border color


                TextView engView=new TextView(this);
                TextView ruView=new TextView(this);
                TextView engValueView=new TextView(this);
                TextView exampleView=new TextView(this);

                engView.setLayoutParams(llp);
                ruView.setLayoutParams(llp);
                engValueView.setLayoutParams(llp);
                exampleView.setLayoutParams(llp);

                engView.setTextColor(Color.BLACK);
                ruView.setTextColor(Color.BLACK);
                engValueView.setTextColor(Color.BLACK);
                exampleView.setTextColor(Color.BLACK);

                engView.setBackground(getResources().getDrawable(R.color.defaultFormBackground));
                ruView.setBackground(getResources().getDrawable(R.color.defaultFormBackground));
                engValueView.setBackground(getResources().getDrawable(R.color.defaultFormBackground));
                exampleView.setBackground(getResources().getDrawable(R.color.defaultFormBackground));

                engView.setText(eng);
                ruView.setText(ru);
                engValueView.setText(engValue);
                exampleView.setText(example);

                row.addView(engView);
                row.addView(ruView);
                row.addView(engValueView);
                row.addView(exampleView);

                EngWord word=new EngWord();
                word.eng=eng;
                word.ru=ru;
                word.eng_value=engValue;
                word.example=example;
                wordsList.add(word);
                tableFromFile.addView(row);

            }
        }catch (Exception e){
            ErrorActivity.throwError(ActivityFromFile.this,"Не получается разобрать файл в JSONObject:\n"+e.toString());
        }




    }
    public void ActivityFromFileRefreshFiles(View v){
        refreshFilesAndDownloadInSpinner();
        tableFromFile.removeAllViewsInLayout();
        tableLayputAddHeaders();
    }
    private void refreshFilesAndDownloadInSpinner(){
        wordsList.clear();
        File dir=new File(absolutePathDir);
        if(!dir.exists()){
            try{
                boolean status=dir.mkdirs();
                if(!status){
                    String errorMessage="Не получается создать папку:"+dir.getCanonicalPath()+"\nor:"+
                            dir.getAbsolutePath();
                    Toast toast = Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }catch (Exception e){
                ErrorActivity.throwError(ActivityFromFile.this,e.toString());
                return;
            }

        }
        FilenameFilter jsonFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".json");
            }
        };
        String[] files=dir.list(jsonFilter);
        if(files.length==0){
            String [] notFound={"Not found files. Create file and refresh"};
            fileSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,notFound));
        }else{
            fileSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,files));
        }
    }
    private void tableLayputAddHeaders(){
        TableRow row=new TableRow(this);
        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 5, 5);//2px right-margin

        TextView engView=new TextView(this);
        TextView ruView=new TextView(this);
        TextView engValueView=new TextView(this);
        TextView exampleView=new TextView(this);

        engView.setLayoutParams(llp);
        ruView.setLayoutParams(llp);
        engValueView.setLayoutParams(llp);
        exampleView.setLayoutParams(llp);

        engView.setBackground(getResources().getDrawable(R.color.defaultFormBackground));
        ruView.setBackground(getResources().getDrawable(R.color.defaultFormBackground));
        engValueView.setBackground(getResources().getDrawable(R.color.defaultFormBackground));
        exampleView.setBackground(getResources().getDrawable(R.color.defaultFormBackground));

        engView.setText(EngWord.Table.ENG);
        ruView.setText(EngWord.Table.RU);
        engValueView.setText(EngWord.Table.ENG_VALUE);
        exampleView.setText(EngWord.Table.EXAMPLE);

        engView.setTextSize(30.0f);
        ruView.setTextSize(30.0f);
        engValueView.setTextSize(30.0f);
        exampleView.setTextSize(30.0f);

        engView.setTypeface(null, Typeface.BOLD);
        ruView.setTypeface(null, Typeface.BOLD);
        engValueView.setTypeface(null, Typeface.BOLD);
        exampleView.setTypeface(null, Typeface.BOLD);

        engView.setTextColor(Color.RED);
        ruView.setTextColor(Color.RED);
        engValueView.setTextColor(Color.RED);
        exampleView.setTextColor(Color.RED);

        row.addView(engView);
        row.addView(ruView);
        row.addView(engValueView);
        row.addView(exampleView);

        tableFromFile.addView(row);
    }
    public void addWordsFromFile(View v){
        int countner=0;
        for(EngWord i:wordsList){
            String result=i.save();
            ++countner;
        }
        Toast toast = Toast.makeText(this,"Add: "+countner, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}