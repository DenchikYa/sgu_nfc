package sgu.csit.nfcproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class quest_two extends AppCompatActivity {
    private static final String CHECK_TWO = "CHECK_TWO";
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_two);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ImageView imageView = (ImageView) findViewById(R.id.img_two);
        if(getCheckQuest(CHECK_TWO)){
            imageView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.true_pic));
        }else {
            imageView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.false_pic));
        }
    }
    private boolean getCheckQuest(String key){
        return mPrefs.getBoolean(key,false);
    }
    public void onBackListClick(View view) {
        startActivity(new Intent(this, List.class));
    }
}
