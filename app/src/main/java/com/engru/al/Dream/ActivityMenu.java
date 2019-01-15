package com.engru.al.Dream;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.engru.al.Dream.Verbs.ActivityIrregularVerbs;

public class ActivityMenu extends Activity {
    Button open;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    private void setEnable(){
        if(open!=null){
            open.setEnabled(true);
            open=null;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        setEnable();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setEnable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEnable();
    }

    public void onClickChoose(View v){
        if(open!=null){
            Toast toast = Toast.makeText(getApplicationContext(),"Сейчас уже открывается: "+open.getText().toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        open=(Button)v;
        v.setEnabled(false);
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
                SettingsActivity.openSettingsActivity(ActivityMenu.this,SettingsActivity.MODES.ENG_MODE.toString());
                break;
            case R.id.openScrolls:
                SettingsActivity.openSettingsActivity(ActivityMenu.this,SettingsActivity.MODES.SCROLL_MODE.toString());
                break;
            case R.id.functionalButton:
                ActivityHelp.openActivityHelp(ActivityMenu.this);
                break;
            case R.id.downloadFromFile:
                ActivityFromFile.openActivityFromFile(ActivityMenu.this);
                break;
            case R.id.massAdd:
                SettingsActivity.openSettingsActivity(ActivityMenu.this,SettingsActivity.MODES.MASS_ADD_IN_SCROLL.toString());
                break;
            default:
                ErrorActivity.throwError(ActivityMenu.this,"Нет обработки нажатия этой клавищи. id="+v.getId());
        }
    }

}
