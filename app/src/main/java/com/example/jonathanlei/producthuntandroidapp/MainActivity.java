package com.example.jonathanlei.producthuntandroidapp;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import com.google.gson.JsonObject;


public class MainActivity extends Activity {

    public static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        public static String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        private TextView textview;

        private String access_token = null;

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ProductHunt_API test = new ProductHunt_API();

            textview = (TextView) rootView.findViewById(R.id.frag_body);

            //String response = test.execute();
            test.execute();

            return rootView;
        }


        public class ProductHunt_API extends AsyncTask<Void, Void, Void> {
            private String response;
            private String postData;

            //URL builder
            final String PRODUCTHUNT_BASE_URL = "https://api.producthunt.com/v1/";
            String client_api_key = "f4e6e5c4813a1b4542fce5d24dab367727825497ef4ec02afcfbec0518a46fa5";
            String client_secret = "c0f49bf9d0148ae323651d00e21fe1289b7dc1b954364aa5be9f66e7ca048521";
            String grant_type = "client_credentials";

            //Json Parser
            private String getToken(String json) throws JSONException {

                JSONObject oauthInfo = new JSONObject(json);

                return oauthInfo.getString("access_token");
            }


            @Override
            protected void onPostExecute(Void voids) {

                ProductHunt_getPost postGetter = new ProductHunt_getPost();
                postGetter.execute();

            }

            @Override
            protected Void doInBackground(Void... voids) {

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                String client_only_url = PRODUCTHUNT_BASE_URL + "oauth/token";


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
                    response = buffer.toString();
                    //return response;
                    //return response.toString();//this.getWeatherDataFromJson(forecastJsonStr, numDays);

                    access_token = getToken(response);


                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attempting
                    // to parse it.
                    //return null;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                    //return null;
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
                    //return null;
                }

                return null;
            }

        }

        public class ProductHunt_getPost extends AsyncTask<Void, Void, Void> {

            final String PRODUCTHUNT_BASE_URL = "https://api.producthunt.com/v1/";

            String postData;

            private String getProductHuntPost(String accessToken) {

                String getPostURL = PRODUCTHUNT_BASE_URL + "posts";

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                try {


                    Uri builtUri = Uri.parse(getPostURL).buildUpon().build();

                    URL url = new URL(builtUri.toString());

                    Log.v(LOG_TAG, "url is: " + url.toString());

                    // Create the request to OpenWeatherMap, and open the connection

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
                    return buffer.toString();

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attempting
                    // to parse it.
                    //return null;
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
                    //return null;
                }


                return "";
            }


            @Override
            protected void onPostExecute(Void voids) {
                textview.setText(postData);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                postData = getProductHuntPost(access_token);
                return null;
            }
        }

    }

}


