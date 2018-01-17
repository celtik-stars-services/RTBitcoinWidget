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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Arrays;

/**
 * Created by tech on 17-12-14.
 */

public class ConfigurationActivity extends Activity {

    //todo: make it private with public method
    public static String PREFERENCE_CURRENCY = "CAD";
    public String PREFERENCE_FREQUENCY = "60";

    private RadioGroup radioCurrencyGroup;
    private RadioButton radioCurrencyButton;

    private RadioGroup radioFrequencyGroup;
    private RadioButton radioFrequencyButton;

    private Button btnSaveConfig;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        System.out.format("ConfigurationActivity - onCreate\n");

        setContentView(R.layout.layout_config_activity);

        // Set the result to CANCELED. This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.

        setResult(RESULT_CANCELED);

        addListenerOnButton();
    }

    /**
     *
     */
    public void addListenerOnButton() {

        btnSaveConfig = (Button) findViewById(R.id.saveConfigButton);

        radioCurrencyGroup = (RadioGroup) findViewById(R.id.rbg_cur);
        radioFrequencyGroup = (RadioGroup) findViewById(R.id.rbg_fre);

        //btnDisplay = (Button) findViewById(R.id.btnDisplay);

        btnSaveConfig.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // get Currencies group
                int selectedCurrencyId = radioCurrencyGroup.getCheckedRadioButtonId();

                // get selected currency
                radioCurrencyButton = (RadioButton) findViewById(selectedCurrencyId);

                PREFERENCE_CURRENCY = radioCurrencyButton.getText().toString();

                // get Frequencies group
                int selectedFrequencyId = radioFrequencyGroup.getCheckedRadioButtonId();

                // get selected currency
                radioFrequencyButton = (RadioButton) findViewById(selectedFrequencyId);

                PREFERENCE_FREQUENCY = radioFrequencyButton.toString();
/*
                Toast.makeText(MyAndroidAppActivity.this,
                        radioSexButton.getText(), Toast.LENGTH_SHORT).show();
*/

                showAppWidget();
            }

        });


    }

    int mAppWidgetId;

    /**
     * give hand to main function
     */
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

    /**
     * useless method
     * todo: remove it
     * @param currency
     * @return
     */
    /*
    private int getCurrency(int currency) {

        String[] values = getResources().getStringArray(R.array.currencies);

        //final int a = Arrays.binarySearch(languageAlias, languageData);

        for (int i=0; i<values.length; i++) {
            if (values[i].equals(R.array.currencies))
                return i;
        }

        return 0;
    }
*/
    /**
     * Useless method
     * todo: remove it
     */

    /*
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
*/

    // Write the prefix to the SharedPreferences object for this widget

    /**
     * useless method
     * todo: remove it
     * @param context
     * @param text
     */
    /*
    void saveCurrency(Context context, String text) {

        SharedPreferences.Editor sharedPrefCurrency = PreferenceManager.getDefaultSharedPreferences(context).edit();

        //todo: find the good name instead of "settingActivity"
        sharedPrefCurrency.putString(this.PREFERENCE_CURRENCY, text);

        sharedPrefCurrency.commit();

    }
*/
    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource

    /**
     * useless method
     * todo: remove it
     * @param context
     * @param appWidgetId
     * @return
     */
    /*
    static String loadCurrency(Context context, int appWidgetId) {

        SharedPreferences preferedCurrency = PreferenceManager.getDefaultSharedPreferences(context);

        //todo: ind the good name instead of "settingActivity"
        String currency = preferedCurrency.getString(SettingsActivity.INTERVAL_PREF, SettingsActivity.DEFAULT_INTERVAL);

        return currency;


    }
    */
}
