package com.borleone.faceplay;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Pushkar Borle on 11/14/2015.
 */
public class MusicList extends Activity {
    // All static variables
    static final String URL = "http://api.androidhive.info/music/music.xml";
    // XML node keys
    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";

    ListView list;
    LazyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        // creating new HashMap
        // adding each child node to HashMap key  value

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            //add songs to list
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisDuration = musicCursor.getString(durationColumn);
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
                String duration = sdf.format(new Date(Long.parseLong(thisDuration) - TimeZone.getDefault().getRawOffset()));
                //songList.add(new Song(thisId, thisTitle, thisArtist));
                System.out.println(thisTitle);

                map.put(KEY_ID, Long.toString(thisId));
                map.put(KEY_TITLE, thisTitle);
                map.put(KEY_ARTIST, thisArtist);
                map.put(KEY_DURATION, duration);
                //map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

                // adding HashList to ArrayList
                songsList.add(map);

            }
            while (musicCursor.moveToNext());
        }

        list = (ListView) findViewById(R.id.list);

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(this, songsList);
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
