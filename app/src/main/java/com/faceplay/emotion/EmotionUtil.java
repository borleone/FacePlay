package com.faceplay.emotion;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Ajay on 11/14/2015.
 */
public class EmotionUtil {

    public static String ANGER = "anger"; //
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

    public static List<FacePlaylist> getPlayListForMood(final String mood){
        /*final List<FacePlaylist> playlist = new ArrayList<>();
        SpotifyApi api = new SpotifyApi();

        final SpotifyService spotify = api.getService();

        final Boolean[] done = {false};

        final CountDownLatch loginLatch = new CountDownLatch (1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {



                *//*spotify.searchPlaylists(mood, new Callback<PlaylistsPager>() {
                    @Override
                    public void success(PlaylistsPager playlistsPager, Response response) {

                        try {
                            loginLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("Face", playlistsPager.playlists.items.get(0).uri + "");
                        List<PlaylistSimple> items = playlistsPager.playlists.items;
                        for (PlaylistSimple item : items) {
                            FacePlaylist list = new FacePlaylist(item.images.get(0).url, item.name, item.tracks.total, item.uri);
                            playlist.add(list);
                            Log.d("Face", list.toString());

                        }

                        loginLatch.countDown();
                *//**//*Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                intent.setData(Uri.parse(
                        playlistsPager.playlists.items.get(0).uri));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);*//**//*
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });*//*
            }
        });

        thread.run();
        try {
            loginLatch.await (10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e("playList", "" + playlist.size());
//        while(!done[0]);

        return playlist;*/
        SpotifyFetch fetch = new SpotifyFetch();
        try {
            List<FacePlaylist> playlists= (List<FacePlaylist>) fetch.execute(mood).get(10, TimeUnit.SECONDS);
            return playlists;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

}
