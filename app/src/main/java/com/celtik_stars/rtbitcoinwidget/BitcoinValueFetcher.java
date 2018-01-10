package com.celtik_stars.rtbitcoinwidget;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tech on 17-12-12.
 */

public class BitcoinValueFetcher {

    private static final String OPEN_BITCOIN_API =
            "https://blockchain.info/tobtc?currency=CAD&value=1";

    public static String getBitcoinValue(){
        try {
            URL url = new URL(OPEN_BITCOIN_API);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            //connection.addRequestProperty("x-api-key",context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            //JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
           // if(data.getInt("cod") != 200){
            //    return null;
            //}

            return tmp;
        }catch(Exception e){
            return "";
        }
    }

}
