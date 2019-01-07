package com.engru.al.engru;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ActivityMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void onClickChoose(View v){
        switch(v.getId()){
            case R.id.randomGame:
                MainGameActivity.openMainGameActivity(ActivityMenu.this);
                break;
            case R.id.verbs:
                ActivityIrregularVerbs.openActivityIrregularVerbs(ActivityMenu.this);
                break;
            case R.id.openBrowser:
                ActivitySqlBrowser.openSqlBrowserActivity(ActivityMenu.this);
                break;
            case R.id.openWords:
                SettingsActivity.openSettingsActivity(ActivityMenu.this,SettingsActivity.ENG_MODE);
                break;
            case R.id.openScrolls:
                SettingsActivity.openSettingsActivity(ActivityMenu.this,SettingsActivity.SCROLL_MODE);
                break;
            case R.id.functionalButton:
                ActivityHelp.openActivityHelp(ActivityMenu.this);
                break;
            case R.id.downloadFromFile:
                ActivityFromFile.openActivityFromFile(ActivityMenu.this);
                break;
            default:
                ErrorActivity.throwError(ActivityMenu.this,"Нет обработки нажатия этой клавищи. id="+v.getId());
        }

    }

}
