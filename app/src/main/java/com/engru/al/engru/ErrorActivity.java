package com.engru.al.engru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ErrorActivity extends Activity {
    private static String ERROR_KEY="MYError_open_ErrorActivity";
    public static void throwError(Context from,String message){
        Intent intent = new Intent( from,ErrorActivity.class );
        intent.putExtra(ERROR_KEY,message);
        from.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        Intent intent = getIntent();
        String textErrorMessage=intent.getStringExtra(ERROR_KEY);
        TextView errorView= (TextView) findViewById(R.id.errorView);
        errorView.setText(textErrorMessage);

    }
}
