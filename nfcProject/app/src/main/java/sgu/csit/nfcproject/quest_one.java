package sgu.csit.nfcproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class quest_one extends AppCompatActivity {
    private static final String CHECK_ONE = "CHECK_ONE";
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_one);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ImageView imageView = (ImageView) findViewById(R.id.img_one);
        if(getCheckQuest(CHECK_ONE)){
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
    protected void onResume() {
        super.onResume();

    }
}
