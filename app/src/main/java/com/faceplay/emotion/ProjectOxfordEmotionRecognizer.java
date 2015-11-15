package com.faceplay.emotion;

import android.net.Uri;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Ajay on 11/14/2015.
 */
public class ProjectOxfordEmotionRecognizer implements IEmotion{

    private List<Emotion> emotionClasses ;
    private Emotion mostProbableEmotion;


    public ProjectOxfordEmotionRecognizer() {
        this.emotionClasses = new ArrayList<>();
        this.initializeEmotionClasses();
    }

    private void initializeEmotionClasses(){
        for (String emotion : EmotionUtil.projectOxfordEmotions) {
            this.emotionClasses.add(new Emotion(emotion));
        }
    }

    @Override
    public List<Emotion> getEmotionClasses() {
        return this.emotionClasses;
    }

    @Override
    public Emotion getEmotion() {
        return this.mostProbableEmotion;
    }

    @Override
    public void submitImage(URL uri) {
        ProjectOxfordAsync task = new ProjectOxfordAsync();
        try {
            String response = task.execute(uri).get(10, TimeUnit.SECONDS);
            Log.d("FacePlay", "Response " + response);
            List<Emotion> emotions = EmotionUtil.processProjectOxfordResult(response);

            mostProbableEmotion = EmotionUtil.getMostProbableEmotion(emotions);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
