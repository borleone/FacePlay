package com.faceplay.emotion;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistsPager;

/**
 * Created by Borle on 11/15/2015.
 */
public class SpotifyFetch extends AsyncTask<String,List<FacePlaylist>,Object> {
    @Override
    protected List<FacePlaylist> doInBackground(String[] params) {
        SpotifyApi api = new SpotifyApi();

        final SpotifyService spotify = api.getService();
        String mood= params[0];
        List<FacePlaylist> playlist = new ArrayList<>();
        PlaylistsPager playlistsPager = spotify.searchPlaylists(mood);
        Pager<PlaylistSimple> playlists = playlistsPager.playlists;
        for(PlaylistSimple item:playlists.items){
            FacePlaylist list = new FacePlaylist(item.images.get(0).url,item.name,item.tracks.total,item.uri);
            playlist.add(list);
        }
        return playlist;
    }
}
