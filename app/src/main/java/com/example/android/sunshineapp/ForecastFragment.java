package com.example.android.sunshineapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.sunshineapp.data.WeatherContract;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    ArrayAdapter<String> arrayAdapter;
    private ForecastAdapter mForecastAdapeter;
    public ForecastFragment() {

    }
    private static final int WEATHER_LOADER = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = R.id.action_refresh;
        if (id == item.getItemId()){
            Log.v("OUTPUT", "Pressed refresh button");
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeather(){
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity(),arrayAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.location_key),getString(R.string.location_value));
        fetchWeatherTask.execute(location);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("Oncreateview","oncreateview");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList<String> forecastArray = new ArrayList<>();
/*
        forecastArray.add("Today-Sunday-63");
        forecastArray.add("Tomorrow-Foggy-70/45");
        forecastArray.add("Weds-Cloudy-72/86");
        forecastArray.add("Thurs-Rainy-45/85");
        forecastArray.add("Fri-Rainy-45/85");
        forecastArray.add("Sat-Rainy-45/85");
*/
//        arrayAdapter = new ArrayAdapter<String>
//                (getActivity(),R.layout.list_item_forecast,forecastArray);

        ListView listview = (ListView)rootView.findViewById(R.id.listview_forecast);

        // Defining on click event listener for list view
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.v("OUTPUT","View which recieved the click"+view.toString());
//                Log.v("OUTPUT","Position/which list item recieved the click"+position);
//                Log.v("OUTPUT","Id of list item recieved the click"+id);
//                Log.v("OUTPUT", "Parent viww recieved the click" + parent.toString());
//                //To initialize a Toast maketext function should be used instead of the ctor.
//                String forecast = arrayAdapter.getItem(position);
//                /*Toast toast = Toast.makeText(getActivity().getApplicationContext(),forecast, Toast.LENGTH_SHORT);
//                toast.show();*/
//                // Here we use explicit intent to call the Detail Activity.
//                // Pass the forecast data to the activity.
//                Intent invokeDetailActivity = new Intent(getActivity(),DetailActivity.class).putExtra(Intent.EXTRA_TEXT, forecast);
//
//                startActivity(invokeDetailActivity);
//
//            }
//        });

    /*    String locationSetting = Utility.getPreferredLocation(getContext());

        Uri getWeatherLocationuri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Cursor cur = getContext().getContentResolver().query(getWeatherLocationuri,null,null,null,sortOrder);
    */    mForecastAdapeter = new ForecastAdapter(getContext(),null,0);
          listview.setAdapter(mForecastAdapeter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v("Onactivitycreated","activity created");


        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(WEATHER_LOADER,null,this);
    }
    // Async task 3 parameters : 1.Params, the type of the parameters sent to the task upon execution.
    //2.Progress, the type of the progress units published during the background computation.
    //3.Result, the type of the result of the background computation.
 /*   private  class FetchWeatherTask extends AsyncTask <String,Void,String []>
    {
        private  static final String URL_PARAMETER_Q = "q";
        private static final String URL_PARAMETER_MODE = "mode";
        private static final String URL_PARAMETER_UNITS = "units";
        private static final String URL_PARAMETER_COUNT = "cnt";
        private static final String URL_PARAMETER_APPID = "APPID";
        String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily";
        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String postCode = "94043";
            Log.v("OUTPUT", "ENTERING doINbackground method");
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                if (params[0] != null ){
                        postCode = params[0];
                }

                Uri builtURL = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(URL_PARAMETER_Q, postCode).
                        appendQueryParameter(URL_PARAMETER_MODE, "json").
                        appendQueryParameter(URL_PARAMETER_UNITS, "metric")
                       .appendQueryParameter(URL_PARAMETER_COUNT, "7").
                       appendQueryParameter(URL_PARAMETER_APPID, "53fba4e2b0f7dc4545f6dd3afc61ec9b").build() ;
                Log.v("OUTPUT",builtURL.toString());
                // Create the request to OpenWeatherMap, and open the connection
                URL url = new URL(builtURL.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.v("OUTPUT","CONNECTED !!!");
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                Log.v("OUTPUT","READING");
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                SunshineHelper helper = new SunshineHelper();
                try {
                    String [] answer = helper.getWeatherDataFromJson(forecastJsonStr,7);
                    return answer;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return null;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            arrayAdapter.clear();
            for (int i=0;i<strings.length;i++){
                arrayAdapter.add(strings[i]);
            }
        }
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Here the loader gets created with the loader id and the other URI parameters we use for content resolver
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_MAX_TEMP + " ASC";
        String locationSetting = Utility.getPreferredLocation(getContext());
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());
        Log.v("Loader created!","Loader created");

        Loader<Cursor> cursorLoader = new CursorLoader(getContext(),weatherForLocationUri,null,null,null,sortOrder);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("Info DG ", "cursor data");
        data.moveToFirst();
            int colIndex = data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
            Log.v("Date value ",data.getString(colIndex));

        mForecastAdapeter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapeter.swapCursor(null);
    }
}
