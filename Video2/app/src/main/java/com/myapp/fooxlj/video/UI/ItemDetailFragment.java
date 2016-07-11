package com.myapp.fooxlj.video.UI;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.myapp.fooxlj.video.Connector.YoutubeConnector;
import com.myapp.fooxlj.video.Model.VideoItem;
import com.myapp.fooxlj.video.R;
import com.myapp.fooxlj.video.dummy.DummyContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item";

    /**
     * The dummy content this fragment is presenting.
     */
    private VideoItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Gson gson = new Gson();
            mItem = gson.fromJson(getArguments().getString(ARG_ITEM),VideoItem.class);

            Activity activity = this.getActivity();
            TextView textView = (TextView) activity.findViewById(R.id.details_title);
            if (!(textView==null)){textView.setText(mItem.getTitle());}

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.getDescription());

        }

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        TextView textView = (TextView) getActivity().findViewById(R.id.details_title);
        if (!(textView==null)){textView.setText(mItem.getTitle());}
    }
}
