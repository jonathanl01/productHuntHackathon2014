package com.example.jonathanlei.producthuntandroidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jonathanlei.producthuntandroidapp.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class DetailActivity extends Activity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        private ArrayAdapter mCommentsAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();
            mCommentsAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_comment,
                    R.id.list_item_comment_textView,
                    new ArrayList<String>()
            );
            ListView listView = (ListView) rootView.findViewById(R.id.listView_detailFragmentComment);
            listView.setAdapter(mCommentsAdapter);
            if (intent != null) {
                Post post = (Post) intent.getSerializableExtra("post");

                ImageView prodImgView = (ImageView) rootView.
                        findViewById(R.id.detailFragment_productImageView);
                new ImageDownloader(prodImgView).execute(post.getScreenshot_url_300px());

                TextView nameTextView = (TextView) rootView.
                        findViewById(R.id.detailFragment_productTagLineView);
                nameTextView.setText(post.getTagline());

                ProductHuntApiTask phTask = new ProductHuntApiTask(
                        intent.getStringExtra("access_token"));
                phTask.execute(post.getId());
            }

            return rootView;
        }

        public class ProductHuntApiTask extends AsyncTask<String, Void, JSONArray> {

            private final String LOG_TAG = ProductHuntApiTask.class.getSimpleName();
            private final String PRODUCT_HUNT_API_URL = "https://api.producthunt.com/v1/";
            private final String accessToken;

            public ProductHuntApiTask(String token) {
                accessToken = token;
            }

            private JSONArray getProductHuntPostComments(String postId) {

                String getPostURL = PRODUCT_HUNT_API_URL + "posts/" + postId + "/comments";

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                try {
                    Uri builtUri = Uri.parse(getPostURL).buildUpon()
                            .appendQueryParameter("post_id", postId).build();

                    URL url = new URL(builtUri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();

                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.

                    }

                    JSONObject data = new JSONObject(buffer.toString());
                    return data.getJSONArray("comments");

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attempting
                    // to parse it.
                    //return null;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }
                return null;
            }

            @Override
            protected JSONArray doInBackground(String... params) {
                if (params.length > 0)
                    return getProductHuntPostComments(params[0]);

                return null;
            }

            @Override
            protected void onPostExecute(JSONArray comments) {
                Log.d(LOG_TAG, "# of comments = " + comments.length());
                mCommentsAdapter.clear();
                StringBuilder commentsStrBldr = new StringBuilder();
                for (int i = 0; i < comments.length(); ++i) {
                    commentsStrBldr = new StringBuilder();
                    commentsStrBldr.append("Comment:\n    ");
                    try {
                        commentsStrBldr.append(comments.getJSONObject(i).getString("body"));
                        commentsStrBldr.append("\n\n");
                    } catch (JSONException e) { }

                    mCommentsAdapter.add(commentsStrBldr.toString());
                }

            }

        }
    }

}
