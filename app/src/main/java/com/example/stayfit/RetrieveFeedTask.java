package com.example.stayfit;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class RetrieveFeedTask extends AsyncTask<String, Void, String[]> {
    public AsyncResponse delegate = null;


    @Override
    protected String[] doInBackground(String... strings) {
        String inputLine = "";
        try {
            System.out.println("Zacetek111: "+System.nanoTime());
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("X-Api-Key", "XDEg86C4Xr9hcgG1JNXFGw==U4G1XOeAnVHEXKmg");

            int status = con.getResponseCode();
            System.out.println(status);

            String[] ret = new String[2];

            if(status == HttpURLConnection.HTTP_OK) {


                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();

                ret[0] = content.toString();

            }

            ret[1] = strings[1];

            return ret;
        }catch (Exception a){
            System.out.println(a.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] s) {
        delegate.processFinish(s);
        //super.onPostExecute(s);
    }
}
