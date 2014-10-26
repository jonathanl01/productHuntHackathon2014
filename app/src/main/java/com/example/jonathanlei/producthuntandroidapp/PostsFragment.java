package com.example.jonathanlei.producthuntandroidapp;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

/**
 * A fragment containing an array adapter to display Product Hunt Posts' data.
 */
public class PostsFragment extends Fragment {

    private static String LOG_TAG = PostsFragment.class.getSimpleName();

    private static String GET_OAUTH_TOKEN = "getOauthToken";

    private static String GET_DAILY_POSTS = "getDailyPosts";

    private String access_token;

    private ArrayAdapter<String> mPostsAdapter;

    public PostsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ProductHunt_getOauth productHunt_getOauth = new ProductHunt_getOauth();
        productHunt_getOauth.execute(GET_OAUTH_TOKEN);

        Button searchButton = (Button) rootView.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                ProductHunt_getOauth postGetter = new ProductHunt_getOauth();
                postGetter.execute(GET_DAILY_POSTS);

            }
        });

        mPostsAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_posts,
                R.id.list_item_posts_textView,
                new ArrayList<String>());
        ListView listView = (ListView) rootView.findViewById(R.id.listView_posts);
        listView.setAdapter(mPostsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return rootView;
    }


    public class ProductHunt_getOauth extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = ProductHunt_getOauth.class.getSimpleName();
        private final String PRODUCT_HUNT_API_URL = "https://api.producthunt.com/v1/";
        private final String client_api_key = "f4e6e5c4813a1b4542fce5d24dab367727825497ef4ec02afcfbec0518a46fa5";
        private final String client_secret = "c0f49bf9d0148ae323651d00e21fe1289b7dc1b954364aa5be9f66e7ca048521";
        private final String grant_type = "client_credentials";

        //Json Parse oauth token
        private String getTokenFromJsonString(String json) throws JSONException {
            JSONObject oauthInfo = new JSONObject(json);
            return oauthInfo.getString("access_token");
        }

        //get oauth token from Product Hunt API
        private String getOauthToken() {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String client_only_url = PRODUCT_HUNT_API_URL + "oauth/token";
            try {

                Uri builtUri = Uri.parse(client_only_url).buildUpon()
                        .appendQueryParameter("client_id", client_api_key)
                        .appendQueryParameter("client_secret", client_secret)
                        .appendQueryParameter("grant_type", grant_type)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "url is: " + url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    // Nothing to do.
                    //return null;
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

                return getTokenFromJsonString(buffer.toString());


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return null;
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
        }

        private ArrayList<String> getProductNamesFromJsonString(String json)throws JSONException{

            JSONObject postResponse = new JSONObject(json);
            JSONArray posts = postResponse.getJSONArray("posts");

            ArrayList<String> nameList = new ArrayList<String>();
            for(int i=0; i < posts.length(); i++ ){
                JSONObject post = posts.getJSONObject(i);
                nameList.add(post.getString("name"));
            }

            return nameList;
        }

        private ArrayList<String> getProductHuntDailyPosts(String accessToken) {

            String getPostURL = PRODUCT_HUNT_API_URL + "posts";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                Uri builtUri = Uri.parse(getPostURL).buildUpon().build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    // Nothing to do.
                    //return null;
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
                return getProductNamesFromJsonString(buffer.toString());

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                //return null;
            } catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();

            }finally {
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
                //return null;
            }


            return null;
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            if (params.length > 0) {
                if (params[0].compareTo(GET_OAUTH_TOKEN) == 0) {
                    access_token = getOauthToken();
                    return null;
                    
                } else if (params[0].compareTo(GET_DAILY_POSTS) == 0) {
                    return getProductHuntDailyPosts(access_token);
                }
                
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> data) {
            if (data != null) {
                mPostsAdapter.clear();
                for (String str : data) {
                    mPostsAdapter.add(str);
                }
            }
        }

    }
/*
    public class ProductHunt_getPost extends AsyncTask<Void, Void, Void> {

        final String PRODUCT_HUNT_API_URL = "https://api.producthunt.com/v1/";

        ArrayList<String> postData;


        private ArrayList<String> getProductNamesFromJsonString(String json)throws JSONException{

            JSONObject postResponse = new JSONObject(json);
            JSONArray posts = postResponse.getJSONArray("posts");

            ArrayList<String> nameList = new ArrayList<String>();
            for(int i=0; i < posts.length(); i++ ){
                JSONObject post = posts.getJSONObject(i);
                nameList.add(post.getString("name"));
            }

            return nameList;
        }

        private ArrayList<String> getProductHuntDailyPosts(String accessToken) {

            String getPostURL = PRODUCT_HUNT_API_URL + "posts";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                Uri builtUri = Uri.parse(getPostURL).buildUpon().build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    // Nothing to do.
                    //return null;
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
                return getProductNamesFromJsonString(buffer.toString());

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                //return null;
            } catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();

            }finally {
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
                //return null;
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void voids) {
            mPostsAdapter.clear();
            for(String str: postData){
                mPostsAdapter.add(str);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            postData = getProductHuntDailyPosts(access_token);
            return null;
        }
    }
*/

}
