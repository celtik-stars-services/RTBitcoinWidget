package com.celtik_stars.rtbitcoinwidget;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.Arrays;

/**
 * Created by tech on 17-12-14.
 */

public class ConfigurationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        System.out.format("ConfigurationActivity - onCreate\n");

        setContentView(R.layout.layout_config_activity);

        // Set the result to CANCELED. This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.

        setResult(RESULT_CANCELED);

        initListViews();
    }

    public void initListViews() {

        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                handleOkButton();
            }
        });

    }

    private void handleOkButton() {

        showAppWidget();

    }

    int mAppWidgetId;

    private void showAppWidget() {

        System.out.format("ConfigurationActivity - showAppWidget\n");

        mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

        // Find the widget id from the intent.

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();

        if (extras != null) {

            mAppWidgetId = extras.getInt(EXTRA_APPWIDGET_ID,
                    INVALID_APPWIDGET_ID);

            AppWidgetProviderInfo providerInfo = AppWidgetManager.getInstance(
                    getBaseContext()).getAppWidgetInfo(mAppWidgetId);

            String appWidgetLabel = providerInfo.label;

            //Intent startService = new Intent(ConfigurationActivity.this,
                    //UpdateWidgetService.class);

            Intent startService = new Intent(
                    ConfigurationActivity.this,
                    SimpleWidgetProvider.class);

            startService.putExtra(EXTRA_APPWIDGET_ID, mAppWidgetId);

            startService.setAction("FROM CONFIGURATION ACTIVITY");

            setResult(RESULT_OK, startService);

            startService(startService);

            finish();
        }

        // If this activity was started with an intent without an app widget ID,
        // finish with an error.

        if (mAppWidgetId == INVALID_APPWIDGET_ID) {
            Log.i("I am invalid", "I am invalid");
            finish();
            return;
        }

    }

    //https://www.appsrox.com/android/tutorials/dailyvocab/3/
/*
    private int getSelectedPosition(String interval) {

        String[] values = getResources().getStringArray(R.array.refresh_interval_values);

        for (int i=0; i<values.length; i++) {
            if (values[i].equals(interval)) return i;
        }

        return 0;
    }
*/
    private int getCurrency(int currency) {

        String[] values = getResources().getStringArray(R.array.currencies);

        //final int a = Arrays.binarySearch(languageAlias, languageData);

        for (int i=0; i<values.length; i++) {
            if (values[i].equals(R.array.currencies))
                return i;
        }

        return 0;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        public void onClick(View v) {

        final Context context = ConfigurationActivity.this;

        // When the button is clicked, store the string locally

        String[] values = getResources().getStringArray(R.array.frequencies);

        //this is for a spinner: todo : change it to radio group

        //String widgetText = values[mRefreshIntervalSpn.getSelectedItemPosition()];
        //saveIntervalPref(context, mAppWidgetId, widgetText);

        // Make sure we pass back the original appWidgetId

        Intent resultValue = new Intent();

        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        setResult(RESULT_OK, resultValue);

        finish();
        }

    };

    // Write the prefix to the SharedPreferences object for this widget

    static void saveIntervalPref(Context context, int appWidgetId, String text) {

        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();

        //todo: find the good name instead of "settingActivity"
       // prefs.putString(SettingsActivity.INTERVAL_PREF, text);

        prefs.commit();

    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource

    static String loadIntervalPref(Context context, int appWidgetId) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        //todo: ind the good name instead of "settingActivity"
        //String interval = prefs.getString(SettingsActivity.INTERVAL_PREF, SettingsActivity.DEFAULT_INTERVAL);

        //return interval;

        return "60";
    }
}
