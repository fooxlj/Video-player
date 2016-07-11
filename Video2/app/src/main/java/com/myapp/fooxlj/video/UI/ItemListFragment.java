package com.myapp.fooxlj.video.UI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myapp.fooxlj.video.Connector.YoutubeConnector;
import com.myapp.fooxlj.video.DataBase.DBKeywords;
import com.myapp.fooxlj.video.DataBase.DBVideo;
import com.myapp.fooxlj.video.Model.VideoItem;
import com.myapp.fooxlj.video.R;
import com.myapp.fooxlj.video.dummy.DummyContent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {
    private List<VideoItem> searchResults;


    private Handler handler;
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String item) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setHasOptionsMenu(true);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        ItemListActivity activity = (ItemListActivity) getActivity();
        String keyword = activity.getKeyword();
        String param = activity.getParam();
        if(!(keyword.isEmpty())){
            addKeyword(keyword);
            searchOnYoutube(keyword);
        } else {
                DBVideo dbVideo = new DBVideo(getActivity());
                if (param.equals("history") ) {
                    searchResults = dbVideo.getAllVideos("Video_History");
                    updateVideosFound("Video_History");
                } else {
                    searchResults = dbVideo.getAllVideos("Video_Favorite");
                    updateVideosFound("Video_Favorite");
                }
        }
    }


    private void addKeyword(String keyword){
        DBKeywords dbKeywords = new DBKeywords(getActivity());
        if (!dbKeywords.ifExist(keyword)){
            dbKeywords.addKeyword(keyword);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }


    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
            Gson gson = new Gson();
            mCallbacks.onItemSelected(gson.toJson(searchResults.get(position)));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private void updateVideosFound(final String table){
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getActivity(), R.layout.video_item, searchResults){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.video_item, parent, false);
                }

                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView)convertView.findViewById(R.id.video_title);
                TextView date = (TextView) convertView.findViewById(R.id.date_publication);

                VideoItem searchResult = searchResults.get(position);

                Picasso.with(getActivity()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                if (table.equals("Video_History"))
                {
                    date.setText(searchResult.getDate_watched());
                }else {
                    date.setText(changeDateFormat(searchResult.getDate_publication()));
                }
                return convertView;
            }
        };
        setListAdapter(adapter);
    }

    private String changeDateFormat(String dateStr){
        String datetime="";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateStart = format.parse(dateStr);
            datetime = dateFormat.format(dateStart);
        }catch (Exception e){
            Log.d("exception List View",e.toString());
        }
        return datetime;
    }

    private void searchOnYoutube(final String keywords){
        new Thread(){
            public void run(){
                YoutubeConnector yc = new YoutubeConnector(getActivity());
                searchResults = yc.search(keywords);
                handler.post(new Runnable(){
                    public void run(){
                        updateVideosFound("youtube");
                    }
                });
            }
        }.start();
    }



  // Implement the click on menu item
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      super.onOptionsItemSelected(item);
      DBVideo dbVideo = new DBVideo(getActivity());
      switch (item.getItemId()) {
          case R.id.menu_history:
              searchResults = dbVideo.getAllVideos("Video_History");
              updateVideosFound("Video_History");
              return true;
          case R.id.menu_favorite:
              searchResults = dbVideo.getAllVideos("Video_Favorite");
              updateVideosFound("Video_Favorite");
              return true;
          default:
              break;
      }
      return false;
  }
}
