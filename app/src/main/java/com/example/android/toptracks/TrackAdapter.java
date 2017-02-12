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
import java.util.concurrent.TimeUnit;

/**
 * Created by edwin on 13/09/16.
 */
public class TrackAdapter extends ArrayAdapter<Track> {

    public TrackAdapter(Context context, List<Track> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listViewItem = convertView;
        if (listViewItem == null){
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.top_tracks_list_item, parent, false);
        }

        Track currentTrack = getItem(position);

        ImageView albumImage = (ImageView)listViewItem.findViewById(R.id.albumImage);
     String imageLink = currentTrack.getmAlbumImageLink();
        Picasso.with(getContext()).load(imageLink)
                .centerCrop().resize(80, 80).placeholder(R.mipmap.ic_launcher).into(albumImage);

        TextView songName = (TextView)listViewItem.findViewById(R.id.songName);
        songName.setText(currentTrack.getSongName());

        TextView artist = (TextView)listViewItem.findViewById(R.id.artistName);
        artist.setText(currentTrack.getArtist());

        TextView albumName = (TextView)listViewItem.findViewById(R.id.type);
        albumName.setText(currentTrack.getType());

       long trackTime = currentTrack.getTime();




        String timeformatted = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(trackTime),
                TimeUnit.MILLISECONDS.toSeconds(trackTime) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(trackTime)));


        TextView time = (TextView) listViewItem.findViewById(R.id.time);
        time.setText(timeformatted);



        return listViewItem;
    }
}
