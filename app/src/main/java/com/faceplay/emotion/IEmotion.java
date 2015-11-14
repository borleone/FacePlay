package com.faceplay.emotion;

import android.net.Uri;

import java.net.URL;
import java.util.List;

/**
 * Created by Ajay on 11/14/2015.
 */
public interface IEmotion {

    public List<Emotion> getEmotionClasses();
    public Emotion getEmotion();
    public void submitImage(URL uri);

}
