package com.faceplay.emotion;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Ajay on 11/14/2015.
 */
public class EmotionUtil {

    public static String ANGER = "anger";
    public static String CONTEMPT = "CONTEMPT";
    public static String DISGUST = "disgust";
    public static String FEAR = "fear";
    public static String SURPRISE = "surprise";
    public static String HAPPINESS = "happiness";
    public static String NEUTRAL = "neutral";
    public static String SADNESS = "sadness";
    public static List<String> projectOxfordEmotions = new ArrayList<>(Arrays.asList(ANGER,CONTEMPT,DISGUST,FEAR,SURPRISE,HAPPINESS,NEUTRAL,SADNESS));


    public static List<Emotion> processProjectOxfordResult(String result){
        List<Emotion> emotions = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(result);
            JSONObject jObject = array.getJSONObject(0);
            JSONObject scores = jObject.getJSONObject("scores");
            for(String emotion:projectOxfordEmotions){
                double aDouble = scores.getDouble(emotion.toLowerCase());
                emotions.add(new Emotion(emotion,aDouble));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return emotions;
    }

    public static Emotion getMostProbableEmotion(List<Emotion> emotions){
        Collections.sort(emotions, new Comparator<Emotion>() {
            @Override
            public int compare(Emotion lhs, Emotion rhs) {
                if(lhs.getScore()<rhs.getScore()) return -1;
                if(lhs.getScore()>rhs.getScore()) return 1;
                return 0;
            }
        });
        return emotions.get(emotions.size()-1);
    }

    public static byte[] extractBytes (String ImageName) {
        try {
            Log.d("Face", "Image" + ImageName);
            InputStream image = new FileInputStream(ImageName);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int bytesRead;
            byte[] bytes = new byte[1024];
            while ((bytesRead = image.read(bytes)) > 0) {
                byteArrayOutputStream.write(bytes, 0, bytesRead);
            }
            byte[] data = byteArrayOutputStream.toByteArray();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
