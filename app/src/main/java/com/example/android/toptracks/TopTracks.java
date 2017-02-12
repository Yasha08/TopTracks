package com.example.android.toptracks;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TopTracks extends AppCompatActivity implements LoaderManager.LoaderCallbacks <List<Track>> {
String artistId;
    String heroImage;
    ListView topTracksList;
    TrackAdapter mTrackAdapter;
    private String BASE_URL = "https://api.spotify.com/v1/artists/";
    LoaderManager loaderManager;
    ProgressBar tracksLoading;
    TextView noTracksFound;
    ImageView heroImageArtist;
    Toolbar toolbar;
    String artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar);
       // collapsingToolbarLayout.setExpandedTitleMarginStart(8);
       // collapsingToolbarLayout.setTitle("Top Tracks");


        final Intent intent = getIntent();
        artistId = intent.getStringExtra("artistId");
        heroImage = intent.getStringExtra("heroImage");
        artist = intent.getStringExtra("artist");
        noTracksFound = (TextView)findViewById(R.id.noTracksFound);
        collapsingToolbarLayout.setTitle(artist);
        heroImageArtist = (ImageView) findViewById(R.id.artistImageHero);
        Picasso.with(getApplicationContext()).load(heroImage).into(heroImageArtist);


        BASE_URL = BASE_URL + artistId + "/top-tracks?country=US";

        topTracksList = (ListView) findViewById(R.id.topTracksList);
       topTracksList.setNestedScrollingEnabled(true);
        mTrackAdapter = new TrackAdapter(TopTracks.this, new ArrayList<Track>());
        topTracksList.setAdapter(mTrackAdapter);
        tracksLoading = (ProgressBar) findViewById(R.id.tracksLoading);
        topTracksList.setEmptyView(tracksLoading);
        topTracksList.setEmptyView(noTracksFound);
        noTracksFound.setVisibility(View.GONE);

        if (loaderManager == null){
            loaderManager = getLoaderManager();
            loaderManager.initLoader(2, null, TopTracks.this);
        }
        else {
            loaderManager.restartLoader(2, null, TopTracks.this);
        }


       topTracksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Track selectedTrack = mTrackAdapter.getItem(position);
               String preview_url = selectedTrack.getmPreviewUrl();
               String albumImage = selectedTrack.getmAlbumImageBigLink();
               Bundle bundle = new Bundle();
                bundle.putString("previewUrl", preview_url);
                bundle.putString("albumImage", albumImage);
                bundle.putString("song", selectedTrack.getSongName());
                bundle.putString("artist", selectedTrack.getArtist());
                bundle.putString("album", selectedTrack.getmAlbumName());
               Intent intent1 = new Intent(TopTracks.this, PlayPreview.class);
               intent1.putExtras(bundle);
               startActivity(intent1);
               //Toast.makeText(getApplicationContext(), preview_url, Toast.LENGTH_SHORT).show();
           }
       });
        //Toast.makeText(TopTracks.this, BASE_URL, Toast.LENGTH_LONG).show();


    }

    @Override
    public Loader<List<Track>> onCreateLoader(int i, Bundle bundle) {
        return new TracksLoader(this, BASE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Track>> loader, List<Track> tracks) {
    mTrackAdapter.clear();
        if(tracks != null) {
            tracksLoading.setVisibility(View.GONE);
            mTrackAdapter.addAll(tracks);
        }
        else {
            tracksLoading.setVisibility(View.GONE);
            noTracksFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Track>> loader) {
    mTrackAdapter.clear();
    }
}
