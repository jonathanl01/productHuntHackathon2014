package com.example.jonathanlei.producthuntandroidapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jonathanlei on 10/25/14.
 */
public class ProductHunt_OAuthToken extends AsyncTask<Void, Void, Void> {

    public static String LOG_TAG = ProductHunt_OAuthToken.class.getSimpleName();


    private String access_token = null;

    private String response;
    //private String postData;

    //URL builder
    final String PRODUCTHUNT_BASE_URL = "https://api.producthunt.com/v1/";
    String client_api_key = "f4e6e5c4813a1b4542fce5d24dab367727825497ef4ec02afcfbec0518a46fa5";
    String client_secret = "c0f49bf9d0148ae323651d00e21fe1289b7dc1b954364aa5be9f66e7ca048521";
    String grant_type = "client_credentials";

    ProductHunt_OAuthToken(String token){
        super();
        access_token = token;
    }

    //Json Parser
    private String getToken(String json) throws JSONException {

        JSONObject oauthInfo = new JSONObject(json);

        return oauthInfo.getString("access_token");
    }


    @Override
    protected void onPostExecute(Void voids) {

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
