package com.example.android.sunshineapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshineapp.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public DetailActivityFragment() {
    }
    private static final int WEATHER_LOADER = 2;
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
    };
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        // Here we are going to get the URI for now
        if (intent != null){
            String dataURI = intent.getDataString();
            //((TextView)rootView.findViewById(R.id.detail_text)).setText(dataURI);
        }

/*
        This was for project 1 where we got the forecast string that was set in FOrecast fragment
        on item click listener
        Bundle extras = intent.getExtras();
        if (extras != null ){
            String forcastString = extras.getString(Intent.EXTRA_TEXT);
            ((TextView)rootView.findViewById(R.id.detail_text)).setText(forcastString);
        }
*/


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Added the loader to fetch the data from DB.
        getLoaderManager().initLoader(WEATHER_LOADER,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        Intent intent = getActivity().getIntent();
        if (intent == null){
            return null;
        }
        Loader<Cursor> cursorLoader = new CursorLoader(getContext(),intent.getData(),FORECAST_COLUMNS,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        String date = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        String description = data.getString(COL_WEATHER_DESC);
        boolean isMetric = Utility.isMetric(getContext());
        String maxTemp = Utility.formatTemperature(data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String minTemp = Utility.formatTemperature(data.getDouble(COL_WEATHER_MIN_TEMP),isMetric);

        TextView textView = (TextView)getView().findViewById(R.id.detail_text);
        textView.setText(description + "|" +date + "|" +maxTemp+"/"+minTemp);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

