package com.engru.al.engru;

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

        helpView.setText(help);
    }
    public static void openActivityHelp(Context from){
        Intent intent = new Intent( from,ActivityHelp.class );
        from.startActivity( intent );
    }
}
