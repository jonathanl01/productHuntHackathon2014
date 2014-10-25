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

            private String getToken(String json){

                

                return "";
            }

            @Override
            protected void onPostExecute(Void voids){


                textview.setText(response);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                JSONObject clientOAuth = new JSONObject();

                final String PRODUCTHUNT_BASE_URL = "https://api.producthunt.com/v1/oauth/token";
                String client_api_key = "f4e6e5c4813a1b4542fce5d24dab367727825497ef4ec02afcfbec0518a46fa5";
                String client_secret = "c0f49bf9d0148ae323651d00e21fe1289b7dc1b954364aa5be9f66e7ca048521";
//      String redirect_uri = null;
//      String response_type = "code";
//      String scope = "public private";
                String grant_type = "client_credentials";


                try {


                    clientOAuth.put("client_api_key", client_api_key);
                    clientOAuth.put("client_secret", client_secret);

                    //https://api.producthunt.com/v1/oauth/authorize?client_api_key=[clientid]&redirect_uri=[where shall we redirect to?]&response_type=code&scope=public%private
                    Uri builtUri = Uri.parse(PRODUCTHUNT_BASE_URL).buildUpon()
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
    }


//    public class ProductHunt_API extends AsyncTask<String, Void, String[]>{
//
//        private final String LOG_TAG = ProductHunt_API.class.getSimpleName();
//
//        @Override
//        protected void onPostExecute(String[] strings) {
//            //mForecastAdapter.clear();
//            //mForecastAdapter.addAll(new ArrayList<String>(Arrays.asList(strings)));
//            //ArrayList<String> stringArrayList = new ArrayList(Arrays.asList(strings));
//            //for (String str : stringArrayList) {
//            //    mForecastAdapter.add(str);
//            //}
//
//        }
//
//        /* The date/time conversion code is going to be moved outside the asynctask later,
//        * so for convenience we're breaking it out into its own method now.
//        */
//        private String getReadableDateString(long time){
//            // Because the API returns a unix timestamp (measured in seconds),
//            // it must be converted to milliseconds in order to be converted to valid date.
//            //Date date = new Date(time * 1000);
//            //SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
//            //return format.format(date).toString();
//            return "hello";
//        }
//
//        /**
//         * Prepare the weather high/lows for presentation.
//         */
//        private String formatHighLows(double high, double low) {
//            // For presentation, assume the user doesn't care about tenths of a degree.
//            long roundedHigh = Math.round(high);
//            long roundedLow = Math.round(low);
//
//            String highLowStr = roundedHigh + "/" + roundedLow;
//            return highLowStr;
//        }
//
//        /**
//         * Take the String representing the complete forecast in JSON Format and
//         * pull out the data we need to construct the Strings needed for the wire frames.
//         *
//         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
//         * into an Object hierarchy for us.
//         */
//        private String getWeatherDataFromJson(String forecastJsonStr, int numDays)
//                throws JSONException {
//
//            // These are the names of the JSON objects that need to be extracted.
//            final String OWM_LIST = "list";
//            final String OWM_WEATHER = "weather";
//            final String OWM_TEMPERATURE = "temp";
//            final String OWM_MAX = "max";
//            final String OWM_MIN = "min";
//            final String OWM_DATETIME = "dt";
//            final String OWM_DESCRIPTION = "main";
//
//            JSONObject forecastJson = new JSONObject(forecastJsonStr);
//            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
//
//            //get city info (name, country)
//            JSONObject city = forecastJson.getJSONObject("city");
//            StringBuilder name_city_str = new StringBuilder(city.getString("name"));
//            name_city_str.append(", ");
//            name_city_str.append(city.getString("country"));
//
//            String[] resultStrList = new String[numDays + 1];
//            resultStrList[0] = name_city_str.toString();
//
//            for(int i = 0; i < weatherArray.length(); i++) {
//                // For now, using the format "Day, description, hi/low"
//                String day;
//                String description;
//                String highAndLow;
//
//                // Get the JSON object representing the day
//                JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//                // The date/time is returned as a long.  We need to convert that
//                // into something human-readable, since most people won't read "1400356800" as
//                // "this saturday".
//                long dateTime = dayForecast.getLong(OWM_DATETIME);
//                day = getReadableDateString(dateTime);
//
//                // description is in a child array called "weather", which is 1 element long.
//                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//                description = weatherObject.getString(OWM_DESCRIPTION);
//
//                // Temperatures are in a child object called "temp".  Try not to name variables
//                // "temp" when working with temperature.  It confuses everybody.
//                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//                double high = temperatureObject.getDouble(OWM_MAX);
//                double low = temperatureObject.getDouble(OWM_MIN);
//
//                highAndLow = formatHighLows(high, low);
//                resultStrList[i + 1] = day + " - " + description + " - " + highAndLow;
//            }
//
//            return resultStrList;
//        }
//
//        protected String doInBackground(String...params) {
//            //check for zip code
//            if (params.length == 0) {
//                return null;
//            }
//
//            // These two need to be declared outside the try/catch
//            // so that they can be closed in the finally block.
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//            // Will contain the raw JSON response as a string.
//            String forecastJsonStr = null;
//
//            try {
//                // Construct the URL for the OpenWeatherMap query
//                // Possible parameters are available at OWM's forecast API page, at
//                // http://openweathermap.org/API#forecast
//                final String PRODUCTHUNT_BASE_URL = "https://api.producthunt.com/v1/oauth/token";
////                        QUERY_PARAM = "q",
////                        FORMAT_PARAM = "mode",
////                        UNITS_PARAM = "units",
////                        DAYS_PARAM = "cnt";
//
//                String format = "json",
//                        units = params[1];
//
//                int numDays = 7;
//
//
//                JSONObject clientOAuth = new JSONObject();
//
//                String client_id = "eb43b40e7c53c3891f259ea0d94f61f94051bbfb114e892664ebc7d32bf233eb";
//                String client_secret = "cf2bab45e78cdfe7bf3db7b593ee3e0918f7e60807eafa73450beb26a36dcf99";
////                String redirect_uri = null;
////                String response_type = "code";
////                String scope = "public private";
//                String grant_type = "client_credentials";
//
//
//                clientOAuth.put("client_id", client_id);
//                clientOAuth.put("client_secret", client_secret);
//
//                //https://api.producthunt.com/v1/oauth/authorize?client_id=[clientid]&redirect_uri=[where shall we redirect to?]&response_type=code&scope=public%private
//                Uri builtUri = Uri.parse(PRODUCTHUNT_BASE_URL).buildUpon()
//                        .appendQueryParameter("client_id",client_id )
//                        .appendQueryParameter("client_secret", client_secret)
//                        .appendQueryParameter("grant_type", grant_type)
//                        .build();
//
//                URL url = new URL(builtUri.toString());
//
//                // Create the request to OpenWeatherMap, and open the connection
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                // Read the input stream into a String
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuilder buffer = new StringBuilder();
//                if (inputStream == null) {
//                    // Nothing to do.
//                    return null;
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                    // But it does make debugging a *lot* easier if you print out the completed
//                    // buffer for debugging.
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//                    // Stream was empty.  No point in parsing.
//                    return null;
//                }
//                forecastJsonStr = buffer.toString();
//                return forecastJsonStr.toString();//this.getWeatherDataFromJson(forecastJsonStr, numDays);
//
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error ", e);
//                // If the code didn't successfully get the weather data, there's no point in attempting
//                // to parse it.
//                return null;
//            } catch (JSONException e) {
//                Log.e(LOG_TAG, e.getMessage(), e);
//                e.printStackTrace();
//            } finally{
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e(LOG_TAG, "Error closing stream", e);
//                    }
//                }
//            }
//
//            return null;
//        } //end doInBackground()
//
//
//
//
//    }
//
//
}


