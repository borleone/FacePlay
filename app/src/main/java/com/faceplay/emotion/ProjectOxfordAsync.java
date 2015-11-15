package com.faceplay.emotion;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

class ProjectOxfordAsync extends AsyncTask<URL, String, String> {

    private String response;
    private String APIKey = "b4f6718ebe2145a1b53196824296aff3";


    @Override
    protected String doInBackground(URL... uri) {
        String responseString = null;
        try {
            URL projectOxfordURL = new URL("https://api.projectoxford.ai/emotion/v1.0/recognize");
            for(URL myurl:uri){

                HttpURLConnection conn = (HttpURLConnection) projectOxfordURL.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                List<Pair> params = new ArrayList<Pair>();
                params.add(new Pair("Ocp-Apim-Subscription-Key", APIKey));
                params.add(new Pair("url", myurl.getPath()));
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((responseString =br.readLine()) != null) {
                        response+= responseString;
                    }
                } else {
                    response = "FAILED"; // See documentation for more info on response handling
                }
            }
        } catch (IOException e) {
            Log.d("",e.getStackTrace()+"");
        }
        return responseString;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }

    private String getQuery(List<Pair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode((String) pair.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) pair.second, "UTF-8"));
        }

        return result.toString();
    }

}