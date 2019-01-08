package com.engru.al.Dream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActivityIrregularVerbs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irregular_verbs);
    }
    public static void openActivityIrregularVerbs(Context from){
        Intent intent = new Intent( from,ActivityIrregularVerbs.class );
        from.startActivity( intent );
    }
}
