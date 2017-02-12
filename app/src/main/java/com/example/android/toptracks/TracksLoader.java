package com.example.android.toptracks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by edwin on 13/09/16.
 */
public class TracksLoader extends AsyncTaskLoader <List<Track>> {
    String urls[];
    public TracksLoader(Context context, String... url) {
        super(context);
        urls = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Track> loadInBackground() {

        List <Track> tracks = TrackQutils.fetchTopTracks(urls[0]);

        return tracks;
    }
}
