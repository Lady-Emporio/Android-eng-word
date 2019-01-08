package com.engru.al.Dream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityHelp extends Activity {
    TextView helpView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        helpView=(TextView)findViewById(R.id.helpView);
        StringBuffer help=new StringBuffer();
        help.append(getString(R.string.help1));help.append("\n---------------\n");
        help.append(getString(R.string.help2));help.append("\n---------------\n");
        help.append(getString(R.string.help3));help.append("\n---------------\n");
        help.append(getString(R.string.help4));help.append("\n---------------\n");
        help.append(getString(R.string.help5));help.append("\n---------------\n");
        String TablesFromDownloadFromFile="В download from file структура файла.json\n" +
                "!Осторожно в LowerCase и UpperCase!. !Регистр имеет значение!:\n" +
                "{\""+EngWord.Table.TABLE_NAME+"\":[\n"+
                "\t\t\""+EngWord.Table.ENG+"\":\"some text\",\n"+
                "\t\t\""+EngWord.Table.RU+"\":\"some text\",\n"+
                "\t\t\""+EngWord.Table.EXAMPLE+"\":\"some text\",\n"+
                "\t\t\""+EngWord.Table.ENG_VALUE+"\":\"some text\"\n"+
                "]}\n" +
                "Если структура файла не верна, то должна появиться информация об ошибке.";
        help.append(TablesFromDownloadFromFile);help.append("\n---------------\n");


        helpView.setText(help);
    }
    public static void openActivityHelp(Context from){
        Intent intent = new Intent( from,ActivityHelp.class );
        from.startActivity( intent );
    }
}
