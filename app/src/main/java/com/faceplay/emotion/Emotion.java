package com.faceplay.emotion;

import java.util.Comparator;

/**
 * Created by Ajay on 11/14/2015.
 */
public class Emotion{

    private String emotion;
    private double score;

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
    public Emotion(String emotion) {
        this.emotion = emotion;
    }

    public Emotion(String emotion,double score) {
        this.emotion = emotion;
        this.score = score;
    }


    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

}
