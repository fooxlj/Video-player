package com.myapp.fooxlj.video.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.myapp.fooxlj.video.Connector.InternetConnector;
import com.myapp.fooxlj.video.DataBase.DBKeywords;
import com.myapp.fooxlj.video.DataBase.DBVideo;
import com.myapp.fooxlj.video.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView;
    InternetConnector internetConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internetConnector = new InternetConnector(this);
        DBKeywords dbKeywords = new DBKeywords(this);
        autoCompleteTextView=(AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        List<String> keywordLst = dbKeywords.selectAllKeywords();

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,keywordLst);

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!event.isShiftPressed()) {
                        String input = autoCompleteTextView.getText().toString();
                        finishSearch(input);
                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        menu.removeItem(R.id.menu_search);
        return true;
    }

    private void finishSearch(String input)
    {
        checkInternetBeforeList("Param",input);
        addKeyword(input);
    }

    private void checkInternetBeforeList(String extrea,String value){
        if (internetConnector.isNetworkAvailable())
        {
            Intent detailIntent = new Intent(this, ItemListActivity.class);
            detailIntent.putExtra(extrea, value);
            startActivity(detailIntent);
        }else {
            internetConnector.alertInternetConnection(this);
        }
    }

    private void addKeyword(String input)
    {
        DBKeywords dbKeywords = new DBKeywords(this);
        if(!dbKeywords.ifExist(input)){
            dbKeywords.addKeyword(input);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_history:
                checkInternetBeforeList("Param","history");
                return true;
            case R.id.menu_favorite:
                checkInternetBeforeList("Param","favorite");
                return true;
            default:
                break;
        }
        return false;
    }
}
