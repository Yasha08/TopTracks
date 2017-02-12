package com.example.android.toptracks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by edwin on 13/09/16.
 */
public class ArtistAdapter extends ArrayAdapter <Artists> {


    public ArtistAdapter(Context context, List<Artists> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Artists currentArtist = getItem(position);

        TextView artistName = (TextView)listItemView.findViewById(R.id.artistName);
        artistName.setText(currentArtist.getName());

        TextView genres = (TextView) listItemView.findViewById(R.id.genre);
        genres.setText(currentArtist.getGenre());

        TextView followers = (TextView) listItemView.findViewById(R.id.followers);
        followers.setText("Followers: "+ String.valueOf(currentArtist.getFollowers()));

        ImageView artistImage = (ImageView)listItemView.findViewById(R.id.artistImage);
        String url = currentArtist.getmImageLink();
        Picasso.with(getContext()).load(url).centerCrop().resize(80,80).into(artistImage);


        return listItemView;
    }
}
