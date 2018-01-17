package com.celtik_stars.rtbitcoinwidget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.InetAddress;
import java.net.URL;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.PendingIntent.getActivity;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * Implementation of App Widget functionality.
 */
public class SimpleWidgetProvider extends AppWidgetProvider {



    // Construct the url to fetch the bitcoin value data from web

    //private String mCity = "khulna";
    //private String mCountry = "bd"; // Bangladesh
    // mURLString = "http://api.openweathermap.org/data/2.5/weather?q=khulna,bd&APPID=YourAppID";
    //private String mURLRoot = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String mURLRoot = "https://blockchain.info/tobtc?currency=CAD&value=1";
    //private String mAppID = "&APPID=YourAppID";
    //private String mURLString = mURLRoot+mCity+","+mCountry+mAppID;
    private String mURLString = mURLRoot;

    /**
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds)
    {

        final int count = appWidgetIds.length;
        String codeGroup = "";

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            //String siteContent = codeGroup;
            //String.format("%03d", (new Random().nextInt(900) + 100));

            /*
                RemoteViews
                A class that describes a view hierarchy that can be displayed in
                 another process. The hierarchy is inflated from a layout resource
                 file, and this class provides some basic operations for modifying
                 the content of the inflated hierarchy.
            */

            // Inflate layout.
            // Construct the RemoteViews object
            RemoteViews remoteViews = new RemoteViews(
                    context.getPackageName(),
                    R.layout.simple_widget
            );

            // Update UI.
            // UPDATING THE BITCOIN VALUE
            remoteViews.setTextViewText(
                    R.id.textView,
                    codeGroup.toString()
            );

            // Catch the click on widget views
            Intent intent = new Intent(
                    context,
                    SimpleWidgetProvider.class
            );

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            intent.putExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    appWidgetIds
            );

            /*
                PendingIntent
                A description of an Intent and target action to perform with it.

                Instances of this class are created with
                - getActivity(Context, int, Intent, int),
                - getActivities(Context, int, Intent[], int),
                - getBroadcast(Context, int, Intent, int),
                and getService(Context, int, Intent, int);

                the returned object can be handed to other
                applications so that they can perform the action you described on your behalf at a later time.
            */

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            //removed this method as it is only to be done during configuration

            /*
            remoteViews.setOnClickPendingIntent(
                    R.id.actionButton,
                    pendingIntent
            );
            */

            /*
                AppWidgetManager
                Updates AppWidget state; gets information about installed AppWidget
                providers and other AppWidget related state.
            */

            appWidgetManager.updateAppWidget(widgetId, remoteViews);

            System.out.format("onUpdate - for loop ended\n");
        }

        System.out.format("onUpdate - out\n");

    }


/*

https://prativas.wordpress.com/category/appwidgets-in-android/part-1-creating-a-simple-app-widget-with-configuration-actvitiy-appwidgets-in-android/
    private void initializeAppWidget(){

        int mAppWidgetId = INVALID_APPWIDGET_ID;

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    EXTRA_APPWIDGET_ID,
                    INVALID_APPWIDGET_ID
            );

            showProgressDialog();

            saveTheUserValueInPref(
                    selectedCategory,
                    sourceAndLanguage,
                    mAppWidgetId
            );

            getDataToLoadInWidget = new GetDataToLoadInWidget(
                    ConfigurationActivity.this,
                    selectedSource,
                    selectedLanguage,
                    selectedCategory
            );

        }

        if (mAppWidgetId == INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    private void configureYourResult(){

        //configure your activity
        //after successful do as

        Intent resultValue = new Intent();

        resultValue.putExtra(
                EXTRA_APPWIDGET_ID,
                mAppWidgetId
        );

        setResult(RESULT_OK, resultValue);

        finish();
    }
*/

    /**
     *
     * @param context
     * @param action
     * @return
     */
    // Catch the click on widget views
    protected PendingIntent getPendingSelfIntent(Context context, String action) {

        Intent intent = new Intent(context, getClass());

        intent.setAction(action);

        System.out.format("getPendingSelfIntent - returning Broadcast\n");

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    /**
     *
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        // Allow the network operation on main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();

        StrictMode.setThreadPolicy(policy);


        AppWidgetManager appWidgetManager= AppWidgetManager.getInstance(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.simple_widget);

        ComponentName watchWidget = new ComponentName(context, SimpleWidgetProvider.class);

        Toast.makeText(context, "Requested", Toast.LENGTH_SHORT).show();

        // Check the internet connection availability
        if(isInternetConnected()){

            Toast.makeText(context, "Fetching Data", Toast.LENGTH_SHORT).show();

            // Update the widget weather data
            // Execute the AsyncTask
            new ProcessJSONData( appWidgetManager, watchWidget, remoteViews).execute(mURLString);
            //remoteViews.setInt(R.id.tv_temperature, "setBackgroundResource", R.drawable.bg_green);

            //remoteViews.setTextViewText(R.id.textView, codeGroup);
            remoteViews.setInt(R.id.textView, "setBackgroundResource", R.drawable.bg_green);

        }else {

            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
            //remoteViews.setInt(R.id.tv_temperature, "setBackgroundResource", R.drawable.bg_red);
            //remoteViews.setTextViewText(R.id.textView, codeGroup.toString());
            remoteViews.setInt(R.id.textView, "setBackgroundResource", R.drawable.bg_red);
        }

        /*
        // If the temperature text clicked
        if (TEMPERATURE_CLICKED.equals(intent.getAction())) {
            // Do something
        }

        // If the humidity text clicked
        if(HUMIDITY_CLICKED.equals(intent.getAction())){
            // Do something
        }
        */

        // Apply the changes
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        Toast.makeText(context, "Done updating", Toast.LENGTH_SHORT).show();

    }

    /**
     *
     */
    public static class UpdateWidgetService extends IntentService {



        /**
         *
         */
        public UpdateWidgetService() {
            // only for debug purpose
            super("UpdateWidgetService");

            System.out.format("UpdateWidgetService - Constructor\n");

        }

        /**
         *
         * @param intent
         */
        @Override
        protected void onHandleIntent(Intent intent) {

            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(UpdateWidgetService.this);

            int incomingAppWidgetId = intent.getIntExtra(
                    EXTRA_APPWIDGET_ID,
                    INVALID_APPWIDGET_ID);

            if (incomingAppWidgetId != INVALID_APPWIDGET_ID) {

                try {

                    updateNewsAppWidget(
                            appWidgetManager,
                            incomingAppWidgetId,
                            intent);

                } catch (NullPointerException e) {

                }

            }

            System.out.format("UpdateWidgetService - onHandleIntent\n");

        }

        /**
         *
         * @param appWidgetManager
         * @param appWidgetId
         * @param intent
         */
        public void updateNewsAppWidget(
                AppWidgetManager appWidgetManager,
                int appWidgetId,
                Intent intent) {

            Log.v("String package name", this.getPackageName());

            RemoteViews views = new RemoteViews(
                    this.getPackageName(),
                    R.layout.layout_config_activity);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            System.out.format("UpdateWidgetService - updateNewsAppWidget\n");
        }
    }

    /**
     *
     */
    // AsyncTask to fetch, process and display weather data
    private class ProcessJSONData extends AsyncTask<String, Void, String> {
        private AppWidgetManager appWidgetManager;
        private ComponentName watchWidget;
        private RemoteViews remoteViews;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        /**
         *
         * @param appWidgetManager
         * @param watchWidget
         * @param remoteViews
         */
        public ProcessJSONData(
                AppWidgetManager appWidgetManager,
                ComponentName watchWidget,
                RemoteViews remoteViews){

            // Do something
            this.appWidgetManager = appWidgetManager;
            this.watchWidget = watchWidget;
            this.remoteViews = remoteViews;
        }

        /*
         *
         *
         */
        @Override
        protected String doInBackground(String... strings){
            String dataString;
            String urlString = strings[0];
            String urlStringConcatened;

            // Get jason data from web
            HTTPDataHandler dataHandler = new HTTPDataHandler();


            //Concatenate the urlString with the shared preference
            //"https://blockchain.info/tobtc?currency=CAD&value=1"
            String protoBlockchain = "https://";
            String urlBlockchain = "blockchain.info/";
            String pageBlockchain = "tobtc?";
            String currencyBlockchainTag = "currency=";
            String currencyBlockchainString = ConfigurationActivity.PREFERENCE_CURRENCY;
            String valueBitcoinBlockchainTag = "&value=";
            String valueBitcoinBlockchainString = "1";



            urlStringConcatened = protoBlockchain
                    +urlBlockchain
                    +pageBlockchain
                    +currencyBlockchainTag
                    +currencyBlockchainString
                    +valueBitcoinBlockchainTag
                    +valueBitcoinBlockchainString;

            System.out.format("ProcessJSONData - doInBackground urlStringConcatened is: '%s'\n", urlStringConcatened);

            //The web site is returning the value of the bitcoin
            dataString = dataHandler.GetHTTPData(urlStringConcatened);

            System.out.format("ProcessJSONData - doInBackground dataString returned: '%s'\n", dataString);


            //convert the bitcoin value of the string into the decimal value
            double streamIntVal = Double.valueOf(dataString);

            //Convert the bitcoin value into the currency value
            double bitcoinValue = 1/streamIntVal;

            //Convert the decimal value into the nearest upper integer value
            Integer integer = Integer.valueOf( (int) Math.round(bitcoinValue) );

            //Convert back the integer value into a string
            String bitcoinValueString = String.valueOf(integer);

            //Update the view with the string
            remoteViews.setTextViewText(R.id.textView, bitcoinValueString);
            remoteViews.setTextViewText(R.id.textView5, currencyBlockchainString);

            appWidgetManager.updateAppWidget(watchWidget , remoteViews);

            // Return the data from specified url to nowhere it seems...
            return dataString;
        }

        /**
         *
         * @param stream
         */

        /*
        protected void onPostExecute(String stream){
            super.onPostExecute(stream);


            //
            // EXTRACTION OF THE TAG VALUE ASSOCIATED TO THE PRE TAG
            //
            // see https://alvinalexander.com/blog/post/java/how-extract-html-tag-string-regex-pattern-matcher-group

            Pattern p = Pattern.compile("<pre>(\\S+)</pre>");

            //stream: String to search pattern from
            Matcher m = p.matcher(stream);

            System.out.format("Post execute: '%s'\n", stream);

            // if we find a match, get the group
            if (m.find()) {

                // get the matching group into string codeGroup
                String codeGroup = m.group(1);

               // Toast.makeText(, codeGroup, Toast.LENGTH_SHORT).show();
                Log.d("value ",codeGroup);

                // print the group
                System.out.format("ProcessJSONData - onPostExecute - Bitcoin value: '%s'\n", codeGroup);

                remoteViews.setTextViewText(R.id.textView, codeGroup);

                appWidgetManager.updateAppWidget(watchWidget , remoteViews);

            }



            if(stream !=null){
                try{
                    // Process JSON data

                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONObject "coord"...........................
                    JSONObject coord = reader.getJSONObject("main");

                    // Get the value of key "temp" under JSONObject "main"
                    String temperature = coord.getString("temp");

                    // Get the value of key "humidity" under JSONObject "main"
                    String humidity = coord.getString("humidity");
                    String city = reader.getString("name");
                    Double celsius = getCelsiusFromKelvin(temperature);
                    temperature ="" + celsius + " " + (char) 0x00B0+"C";
                    //Log.d("Temp",temperature);

                    // Display weather data on widget
                   // remoteViews.setTextViewText(R.id.tv_temperature, temperature);

                    remoteViews.setTextViewText(R.id.textView, temperature);
                    //remoteViews.setTextViewText(R.id.tv_humidity, "H: " + humidity + " %");

                    // Apply the changes
                    //appWidgetManager.updateAppWidget(watchWidget, remoteViews);

                    appWidgetManager.updateAppWidget(watchWidget , remoteViews);

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

        } // onPostExecute() end
     */

    } // ProcessJSON class end
/*
    // Method to get celsius value from kelvin
    public Double getCelsiusFromKelvin(String kelvinString){
        Double kelvin = Double.parseDouble(kelvinString);
        Double numberToMinus = 273.15;
        Double celsius = kelvin - numberToMinus;
        // Rounding up the double value
        // Each zero (0) return 1 more precision
        // Precision means number of digits after dot
        celsius = (double)Math.round(celsius * 10) / 10;
        return celsius;
    }
*/

    /**
     *
     * @return
     */
    // Custom method to check internet connection
    public Boolean isInternetConnected(){
        boolean status = false;
        try{
            //todo: replace with bitcoin web site
            InetAddress address = InetAddress.getByName("google.com");

            if(address!=null)
            {
                status = true;
            }
        }catch (Exception e) // Catch the exception
        {
            e.printStackTrace();
            //todo: site is down, print message in configuration screen
        }
        return status;
    }

/*
    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final String bitcoinValue = BitcoinValueFetcher.getBitcoinValue();
                if(bitcoinValue == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
    */

}




