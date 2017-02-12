package com.example.android.toptracks;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks <List<Artists>> {

    android.widget.SearchView searchView;
    List<Artists> martistses;
    String query;
    ArtistAdapter mArtistAdapter;
    ProgressBar loading;
    LoaderManager loaderManager;
    TextView notfound;
    TextView welcome;

    private static final String BASE_URL = "https://api.spotify.com/v1/search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (android.widget.SearchView) findViewById(R.id.search);

        mArtistAdapter = new ArtistAdapter(this, new ArrayList<Artists>());
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mArtistAdapter);

        loading = (ProgressBar)findViewById(R.id.loading);
        listView.setEmptyView(loading);
        loading.setVisibility(View.GONE);

        notfound = (TextView) findViewById(R.id.notfound);
        listView.setEmptyView(notfound);
        notfound.setVisibility(View.GONE);

        welcome = (TextView)findViewById(R.id.welcome);
        listView.setEmptyView(welcome);
        welcome.setVisibility(View.VISIBLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artists selectedArtist = mArtistAdapter.getItem(i);
                String artistId = selectedArtist.getId();
                String heroImagelink = selectedArtist.getmImageLinkHero();
                String artist = selectedArtist.getName();
                Intent showTopTracks = new Intent(MainActivity.this, TopTracks.class);
                showTopTracks.putExtra("artistId", artistId);
                showTopTracks.putExtra("heroImage", heroImagelink);
                showTopTracks.putExtra("artist", artist);
                startActivity(showTopTracks);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


    @Override
    public boolean onQueryTextSubmit(String artist) {

        mArtistAdapter.clear();
        mArtistAdapter.notifyDataSetChanged();
        query = artist;

        loading.setVisibility(View.VISIBLE);
        notfound.setVisibility(View.GONE);
        welcome.setVisibility(View.GONE);

        if(loaderManager == null) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, MainActivity.this);
        }
        else {
            loaderManager.restartLoader(1, null, MainActivity.this);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        query = s;
        return true;
    }});

    }

    @Override
    public Loader<List<Artists>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", query);
        uriBuilder.appendQueryParameter("type" , "artist");

        return new ArtistLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Artists>> loader, List<Artists> artistses) {
        mArtistAdapter.clear();

        if(artistses != null){
            loading.setVisibility(View.GONE);
            welcome.setVisibility(View.GONE);
            notfound.setVisibility(View.GONE);
            mArtistAdapter.addAll(artistses);
            martistses = artistses;
        }
        else {
            loading.setVisibility(View.GONE);
            welcome.setVisibility(View.GONE);
            notfound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Artists>> loader) {
        mArtistAdapter.clear();
    }
}
