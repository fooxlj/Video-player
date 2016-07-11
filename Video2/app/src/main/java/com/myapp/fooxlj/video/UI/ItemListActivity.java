package com.myapp.fooxlj.video.UI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.myapp.fooxlj.video.Connector.InternetConnector;
import com.myapp.fooxlj.video.DataBase.DBKeywords;
import com.myapp.fooxlj.video.DataBase.DBVideo;
import com.myapp.fooxlj.video.Model.VideoItem;
import com.myapp.fooxlj.video.R;

import java.util.List;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details
 * (if present) is a {@link ItemDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ItemListActivity extends AppCompatActivity
        implements ItemListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private String param = "";
    private String keyword = "";
    private boolean mTwoPane;
    InternetConnector internetConnector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_app_bar);
        internetConnector = new InternetConnector(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            param = extras.getString("Param");
            if (param != null) {
                if (!(param.equals("favorite")) && !(param.equals("history"))) {
                    keyword = param;
                }
            }
        }

     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        handleIntent(getIntent());
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }
        // TODO: If exposing deep links into your app, handle intents here.
    }



    /**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String item) {
        if (internetConnector.isNetworkAvailable()) {
            if (mTwoPane) {
                // In two-pane mode, show the detail view in this activity by
                // adding or replacing the detail fragment using a
                // fragment transaction.
                Bundle arguments = new Bundle();
                arguments.putString(ItemDetailFragment.ARG_ITEM, item);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();


            } else {
                Gson gson = new Gson();
                VideoItem mItem = gson.fromJson(item,VideoItem.class);
                DBVideo dbVideo = new DBVideo(this);
                dbVideo.addVideo("Video_History",mItem);
                Intent detailIntent = new Intent(this, ItemDetailActivity.class);
                detailIntent.putExtra(ItemDetailFragment.ARG_ITEM, item);
                startActivity(detailIntent);
            }
        }else {
            internetConnector.alertInternetConnection(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        DBKeywords dbKeywords = new DBKeywords(this);
        List<String> keywordLst = dbKeywords.selectAllKeywords();

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        final ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) MenuItemCompat.getActionView(searchItem);

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
     //   searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,keywordLst);

        searchView.setAdapter(adapter);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setText(adapter.getItem(position).toString());
            }
        });
        searchView.setQuery(keyword, false);
      //  searchView.setSuggestionsAdapter(getCursorAdapter());
        return true;
    }




    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    public String getParam() {
        return param;
    }

    public String getKeyword() {
        return keyword;
    }

    public void onResume(){
        super.onResume();
    }
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            keyword = query;
            //use the query to search
        }
    }

}

