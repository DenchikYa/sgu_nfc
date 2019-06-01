package sgu.csit.nfcproject;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private  static final  String PREF_VAL = "PREF_VAL";
    private static final String CHECK_ONE = "CHECK_ONE";
    private static final String CHECK_TWO ="CHECK_TWO";
    private static final String CHECK_THREE = "CHECK_THREE";
    private static final String TAG = MainActivity.class.getName();
    NfcAdapter nfcAdapter;

    Button tglReadWrite;
    TextView txtTagContent;
    public SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(sgu.csit.nfcproject.R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tglReadWrite = (Button)findViewById(R.id.tglReadWrite);
        txtTagContent = (TextView) findViewById(R.id.txtTagContent);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        updateCounter(getCounter());
    }


    private void updateQuest(String key, boolean check){
        mPrefs.edit().putBoolean(key,check).apply();
    }
    private void updateCounter(int newValue){
        mPrefs.edit().putInt(PREF_VAL,newValue).apply();
        String s = String.valueOf(newValue) + "/3";
        txtTagContent.setText(s);
    }
    private  int getCounter(){
        return mPrefs.getInt(PREF_VAL,0);
    }
    private boolean getCheckQuest(String key){
        return mPrefs.getBoolean(key,false);
    }
    public void onMyButtonClick(View view)
    {
        Log.d(TAG, "asdAD");
        startActivity(new Intent(this, List.class));
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(nfcAdapter == null){
            Log.d(TAG, "Not NFC");
        }else {
            enableForegroundDispatchSystem();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfcAdapter == null){
            Log.d(TAG, "Not NFC");
        }else {
            disableForegroundDispatchSystem();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "NfcIntent!", Toast.LENGTH_SHORT).show();

                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if(parcelables != null && parcelables.length > 0)
                {
                    readTextFromMessage((NdefMessage) parcelables[0]);
                }else{
                    Toast.makeText(this, "No NDEF messages found!", Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length>0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            if(tagContent.equals("Red") ){
                if(!getCheckQuest(CHECK_ONE)){
                    updateQuest(CHECK_ONE,true);
                    updateCounter(getCounter()+1);
                }else {
                    Toast.makeText(this, "Вы уже находили эту метку!", Toast.LENGTH_SHORT).show();
                }
            }
            if(tagContent.equals("Green") && getCheckQuest(CHECK_ONE)){
                if(!getCheckQuest(CHECK_TWO)){
                    updateQuest(CHECK_TWO,true);
                    updateCounter(getCounter()+1);
                }else {
                    Toast.makeText(this, "Вы уже находили эту метку!", Toast.LENGTH_SHORT).show();
                }
            }
            if(tagContent.equals("Blue") && getCheckQuest(CHECK_TWO)){
                if(!getCheckQuest(CHECK_THREE)){
                    updateQuest(CHECK_THREE,true);
                    updateCounter(getCounter()+1);
                }else {
                    Toast.makeText(this, "Вы уже находили эту метку!", Toast.LENGTH_SHORT).show();
                }
            }

        }else
        {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }

    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(sgu.csit.nfcproject.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == sgu.csit.nfcproject.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

*/
    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_SHORT).show();
                return;
            }


            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag writen!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }

    }

    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }


    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }
    public String getTextFromNdefRecord(NdefRecord ndefRecord)
    {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    public void onClickReset(View view) {
        updateCounter(0);
        updateQuest(CHECK_ONE,false);
        updateQuest(CHECK_TWO,false);
        updateQuest(CHECK_THREE,false);
    }
}