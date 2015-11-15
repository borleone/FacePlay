package com.faceplay.emotion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
            JSONObject jObject = new JSONObject(result);
            JSONObject scores = jObject.getJSONObject("scores");
            for(String emotion:projectOxfordEmotions){
                double aDouble = scores.getDouble(emotion);
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
        return emotions.get(0);
    }
}
