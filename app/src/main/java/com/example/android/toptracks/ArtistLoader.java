package com.example.android.toptracks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by edwin on 13/09/16.
 */
public class ArtistLoader extends AsyncTaskLoader <List<Artists>> {
String urls[];

    public ArtistLoader(Context context, String... url) {
        super(context);
        urls = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Artists> loadInBackground() {

        List<Artists> result = Qutils.fetchArtists(urls[0]);

        return result;
    }
}
