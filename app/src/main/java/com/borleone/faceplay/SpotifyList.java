package com.borleone.faceplay;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.faceplay.emotion.FacePlaylist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pushkar Borle on 11/15/2015.
 */
public class SpotifyList extends Activity {

    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";

    ListView list;
    LazyAdapter adapter;
    //private ArrayList<FacePlaylist> playlist = new ArrayList<FacePlaylist>();
    ArrayList<HashMap<String, String>> playlist = new ArrayList<HashMap<String, String>>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        ArrayList<FacePlaylist> al=getIntent().getSerializableExtra(KEY_TITLE);
        List<FacePlaylist> l = new ArrayList<FacePlaylist>();
        Bundle b = this.getIntent().getExtras();
        if (b != null)
            l = (List<FacePlaylist>) b.getSerializable("List");

        Log.e("list", "" + l.size());

        for (int i = 0; i < l.size(); ++i) {

            HashMap<String, String> map = new HashMap<String, String>();

            String s = l.get(i).getPlaylistURL();
            s = s.substring(13);
            s = s.substring(0, s.indexOf(':'));

            //map.put(KEY_ID, Long.toString(thisId));
            map.put(KEY_TITLE, l.get(i).getName());
            map.put(KEY_ARTIST, s);
            map.put(KEY_DURATION, String.valueOf(l.get(i).getNumberOfSongs()));
            map.put(KEY_THUMB_URL, l.get(i).getImageURL());

            // adding HashList to ArrayList
            playlist.add(map);
            Log.e("", playlist.get(i).toString());

//            URL newurl = new URL(photo_url_str);
//            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
//            profile_photo.setImageBitmap(mIcon_val);
        }
        list = (ListView) findViewById(R.id.list);
        Log.e("playlist", "" + playlist.size());
        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(this, playlist);
        list.setAdapter(adapter);

        // Click event for single list row
        final List<FacePlaylist> finalL = l;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(id + " " + position + " " + view.toString());
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                intent.setData(Uri.parse(finalL.get(position).getPlaylistURL()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SpotifyList.this.startActivity(intent);

            }
        });
    }


}
