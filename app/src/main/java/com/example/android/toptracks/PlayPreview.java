package com.example.android.toptracks;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PlayPreview extends AppCompatActivity {
    ImageView image;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    int fileDuration;
    FloatingActionButton playpause;
    FloatingActionButton fastForward;
    FloatingActionButton rewind;
    private boolean isPlaying = false;
    private boolean mediaPlayerReady = false;
    private Toast mToast;

    private String previewUrl;
    private String albumImage;
    private String song;
    private String artist;
    private String album;

    TextView songName;
    TextView artistName;
    TextView albumName;


    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        playpause = (FloatingActionButton) findViewById(R.id.playpause);
        fastForward = (FloatingActionButton) findViewById(R.id.fastforward);
        rewind = (FloatingActionButton) findViewById(R.id.rewind);
       seekBar = (SeekBar) findViewById(R.id.seekBar);
        mediaPlayer = new MediaPlayer();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        previewUrl = bundle.getString("previewUrl");
        albumImage = bundle.getString("albumImage");
        song = bundle.getString("song");
        artist = bundle.getString("artist");
        album = bundle.getString("album");

        songName = (TextView)findViewById(R.id.song);
        artistName = (TextView)findViewById(R.id.artist);
        albumName = (TextView)findViewById(R.id.album);

        songName.setText(song);
        artistName.setText(artist);
        albumName.setText(album);

        image = (ImageView) findViewById(R.id.albumImage);
        Picasso.with(getApplicationContext()).load(albumImage)
                .centerCrop().resize(640, 640).placeholder(R.drawable.spotify_logo).into(image);

        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                  play();
                }
                else {
                    if (mToast != null){
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(getApplicationContext(), "Go Back", Toast.LENGTH_SHORT);
                    mToast.show();

                }
            }
        });

        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerReady && isPlaying) {
                    int currentPos = mediaPlayer.getCurrentPosition();
                    if (currentPos > 5000) {
                        mediaPlayer.seekTo(currentPos - 5000);
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    } else {
                        mediaPlayer.seekTo(0);
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
            }
        });

        fastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerReady && isPlaying) {
                    int currentPos = mediaPlayer.getCurrentPosition();
                    mediaPlayer.seekTo(currentPos + 5000);
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                } else if (mediaPlayerReady && !isPlaying) {
                    mediaPlayer.start();
                    isPlaying = true;
                    playpause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(previewUrl);
            mediaPlayer.prepareAsync();
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT);
            mToast.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                fileDuration = mp.getDuration();
                mediaPlayer = mp;
                seekBar.setMax(mediaPlayer.getDuration());

                mediaPlayerReady = true;

                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(getApplicationContext(), "You can play now.", Toast.LENGTH_SHORT);
                mToast.show();

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer = mp;
                mediaPlayer.seekTo(0);
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                mediaPlayer.pause();
                isPlaying = false;
                playpause.setImageResource(android.R.drawable.ic_media_play);
            }
        });

       mHandler = new Handler();
        PlayPreview.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sBar, int progress, boolean fromUser) {

                seekBar = sBar;
               if (fromUser) {
                   seekBar.setProgress(progress);
               }
                else {
                   seekBar.setProgress(mediaPlayer.getCurrentPosition());
               }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(seekBar.getProgress());
                mediaPlayer.seekTo(seekBar.getProgress());
                mediaPlayer.start();

            }
        });


    }

    private void play(){
        if (mediaPlayerReady && !isPlaying) {
            playpause.setImageResource(android.R.drawable.ic_media_pause);
            mediaPlayer.start();
            isPlaying = true;
        } else if (isPlaying) {
            mediaPlayer.pause();
            playpause.setImageResource(android.R.drawable.ic_media_play);
            isPlaying = false;

        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
