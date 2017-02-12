package app.z0nen.slidemenu;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Z0NEN on 10/22/2014.
 */
public class menu3_Fragment extends ListFragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu3_layout, container, false);
        //String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
         //       "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
        //        "Linux", "OS/2" };
        //   ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        //          android.R.layout.simple_list_item_1, values);
        //    setListAdapter(adapter);
        return rootview;
    }
    // Progress Dialog
    private ProgressDialog pDialog;

    //php read comments script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String READ_COMMENTS_URL = "http://xxx.xxx.x.x:1234/webservice/comments.php";

    //testing on Emulator:
    private static final String READ_COMMENTS_URL = "http://foodster.bplaced.net/webservice/rezepte.php";

    //testing from a real server:
    //private static final String READ_COMMENTS_URL = "http://www.mybringback.com/webservice/comments.php";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TITLE = "gname";
    private static final String TAG_POSTS = "rezepte";
    private static final String TAG_POST_ID = "gid";
    private static final String TAG_MESSAGE = "beschreibung";
    //it's important to note that the message is both in the parent branch of
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.


    //An array of all of our comments
    private JSONArray mComments = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mCommentList;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadComments().execute();
    }

    /**
     * Retrieves recent post data from the server.
     */
    public void updateJSONdata() {

        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..

        mCommentList = new ArrayList<HashMap<String, String>>();




        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {



            // looping through all posts according to the json object returned
            for (int i = 0; i < mComments.length(); i++) {
                JSONObject c = mComments.getJSONObject(i);

                //gets the content of each tag
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_MESSAGE);


                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_TITLE, title);
                map.put(TAG_MESSAGE, content);

                // adding HashList to ArrayList
                mCommentList.add(map);

                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into our listview
     */
    private void updateList() {
        // For a ListActivity we need to set the List Adapter, and in order to do
        //that, we need to create a ListAdapter.  This SimpleAdapter,
        //will utilize our updated Hashmapped ArrayList,
        //use our single_post xml template for each item in our list,
        //and place the appropriate info from the list to the
        //correct GUI id.  Order is important here.
        ListAdapter adapter = new SimpleAdapter(getActivity(), mCommentList,
                R.layout.single_rezept, new String[] { TAG_TITLE, TAG_MESSAGE}, new int[] { R.id.Rezept, R.id.Beschreibung});

        // I shouldn't have to comment on this one:
        setListAdapter(adapter);

        // Optional: when the user clicks a list item we
        //could do something.  However, we will choose
        //to do nothing...
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // This method is triggered if an item is click within our
                // list. For our example we won't be using this, but
                // it is useful to know in real life applications.

            }
        });
    }

    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Comments...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... arg0) {
            //we will develop this method in version 2
            updateJSONdata();
            return null;

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            //we will develop this method in version 2
            updateList();
        }
    }
}
