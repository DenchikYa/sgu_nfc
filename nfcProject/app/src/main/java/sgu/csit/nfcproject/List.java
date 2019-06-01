package sgu.csit.nfcproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class List extends AppCompatActivity {

    private static final String CHECK_ONE = "CHECK_ONE";
    private static final String CHECK_TWO ="CHECK_TWO";

    private static final String MSG = MainActivity.class.getName();
    private static final String PREF_VAL = "PREF_VAL";
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Button buttonOne = findViewById(R.id.one);
        Button buttonTwo = findViewById(R.id.two);
        Button buttonThree = findViewById(R.id.three);
        if(getCheckQuest(CHECK_ONE)){
            buttonTwo.setEnabled(true);
        }else{
            buttonTwo.setEnabled(false);
        }
        if(getCheckQuest(CHECK_TWO)){
            buttonThree.setEnabled(true);
        }else{
            buttonThree.setEnabled(false);
        }
    }
    private boolean getCheckQuest(String key){
        return mPrefs.getBoolean(key,false);
    }
    public void onBackClick(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onOneClick(View view) {
        startActivity(new Intent(this, quest_one.class));
    }

    public void onTwoClick(View view) {
        startActivity(new Intent(this, quest_two.class));
    }

    public void onThreeClick(View view) {
        startActivity(new Intent(this, quest_three.class));
    }
}
