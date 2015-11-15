package com.faceplay.emotion;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

class ProjectOxfordAsync extends AsyncTask<URL, URL, String> {

    private String response;
    private String APIKey = "b4f6718ebe2145a1b53196824296aff3";

    String responseString = new String();
    @Override
    protected String doInBackground(URL... uri) {

        try {
            URL projectOxfordURL = new URL("https://api.projectoxford.ai/emotion/v1.0/recognize");
            for(URL myurl:uri){
                Log.d("FacePlay", "URL " + projectOxfordURL);
                String url = "https://api.projectoxford.ai/emotion/v1.0/recognize";
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                String path =  "/sdcard/Download/Image.jpg";
                byte[] extractBytes = EmotionUtil.extractBytes(path);
                con.setRequestMethod("POST");
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Ocp-Apim-Subscription-Key", "b4f6718ebe2145a1b53196824296aff3");
                con.setRequestProperty("Content-Type", "application/octet-stream");
                con.setRequestProperty("Content-Length", extractBytes.length+"");

                OutputStream os = con.getOutputStream();
                DataOutputStream writer = new DataOutputStream(os);
                writer.write(extractBytes);
                writer.flush();
                writer.close();
                os.close();
                if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                    BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((response =br.readLine()) != null) {
                        Log.d("FacePlay", "Line " + response);
                        responseString+= response;
                    }
                } else {
                    response = "FAILED"; // See documentation for more info on response handling
                }
            }
        } catch (IOException e) {
            Log.d("",e.getStackTrace()+"");
        }
        Log.d("FacePlay", "ResponseString " + responseString);
        return responseString;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("Face",responseString);
    }
}