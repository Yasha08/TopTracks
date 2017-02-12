package com.example.android.toptracks;

import java.util.ArrayList;

/**
 * Created by edwin on 13/09/16.
 */
public class Artists extends ArrayList {
private String mName;
    private String mId;
    private String mGenre;
    private long mFollowers;
    private String mImageLink;
    private String mImageLinkHero;

    public Artists (String name, long followers, String id, String genre, String imageLink, String ImageLinkHero){
        mName = name;
        mId = id;
        mFollowers = followers;
        mGenre = genre;
        mImageLink = imageLink;
        mImageLinkHero = ImageLinkHero;

    }

    public String getName(){
        return mName;
    }

    public String getId(){
        return mId;
    }

    public long getFollowers(){
        return mFollowers;
    }

    public String getGenre(){
        return mGenre;
    }

    public String getmImageLink(){
        return mImageLink;
    }

    public String getmImageLinkHero(){
        return mImageLinkHero;
    }

}


