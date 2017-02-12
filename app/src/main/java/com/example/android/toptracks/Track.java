package com.example.android.toptracks;

import java.util.ArrayList;

/**
 * Created by edwin on 13/09/16.
 */
public class Track extends ArrayList {

    private String mSongName;
    private String mArtist;
    private String mAlbumName;
    private long mTime;
    private String mAlbumImageLink;
    private String mAlbumImageBigLink;
    private String mPreviewUrl;




    public Track (String songName, String artist, String albumName, long time, String previewUrl, String albumImageUrl, String imageBig){
        mSongName = songName;
        mArtist = artist;
        mAlbumName = albumName;
        mTime = time;
        mPreviewUrl = previewUrl;
        mAlbumImageBigLink = imageBig;

      mAlbumImageLink = albumImageUrl;
    }

    public String getSongName(){
        return mSongName;
    }

    public String getArtist(){
        return mArtist;
    }

    public String getType(){
        return mAlbumName;
    }

    public long getTime(){
        return mTime;
    }

    public String getmPreviewUrl() {
        return mPreviewUrl;
    }

    public String getmAlbumImageBigLink() {
        return mAlbumImageBigLink;
    }

    public String getmAlbumName() {
        return mAlbumName;
    }

    public String getmAlbumImageLink(){
        return mAlbumImageLink;
    }

}
