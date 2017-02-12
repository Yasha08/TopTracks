package com.example.android.toptracks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 13/09/16.
 */
public class TrackQutils {


    private TrackQutils() {
    }

    public static List<Track> fetchTopTracks(String requestUrl) {
        String simpleJsonResponse = null;

        URL url = createUrl(requestUrl);
        try {
            if (url != null) {
                simpleJsonResponse = makeHttpRequest(url);
            }
        } catch (IOException e) {
            Log.e("TrackQutils", "Problem with URL", e);
        }
        List<Track> topTracks = getTopTracksfromJson(simpleJsonResponse);

        return topTracks;
    }

    private static URL createUrl(String url) {
        URL createUrl = null;

        try {
            createUrl = new URL(url);
        } catch (MalformedURLException e) {
            Log.e("TrackQutils", "Problem Creating URL", e);
        }

        return createUrl;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
try {
    urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.setRequestMethod("GET");
    urlConnection.setReadTimeout(15000);
    urlConnection.setConnectTimeout(10000);
    urlConnection.connect();

    if (urlConnection.getResponseCode() == 200) {
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
    }
}
catch (IOException e){
    Log.e("TrackQutils", "Problem Making connection", e);
}
        finally{

    if(urlConnection != null){
             urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }

}


return jsonResponse;
    }

    public static String readFromStream (InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Track> getTopTracksfromJson(String jsonResponse){

        List <Track> topTracks = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(jsonResponse);

            JSONArray tracksArray = rootObject.getJSONArray("tracks");

            for (int i = 0; i < tracksArray.length(); i++){
                JSONObject tracksObject = tracksArray.getJSONObject(i);
                String name = tracksObject.getString("name");
                long time = tracksObject.getLong("duration_ms");
                String previewUrl = tracksObject.getString("preview_url");

                JSONObject albumObject = tracksObject.getJSONObject("album");
                String albumName = albumObject.getString("name");

            JSONArray images = albumObject.getJSONArray("images");
            JSONObject imageObject = images.getJSONObject(1);
                String imageLink = imageObject.getString("url");
                JSONObject imageObjectBig = images.getJSONObject(0);
                String imageBig = imageObjectBig.getString("url");

               JSONArray artistsArray = tracksObject.getJSONArray("artists");
                String artists = "";
               for(int j= 0; j<artistsArray.length(); j++){
                  JSONObject artistsObject = artistsArray.getJSONObject(j);
                   artists = artists + artistsObject.getString("name") + ". ";
                }
                topTracks.add(new Track(name, artists, albumName, time, previewUrl, imageLink, imageBig));
            }
        }
        catch (JSONException e){
            Log.e("TrackQutils", "Error Parsing JsonResponse", e);
        }
      return topTracks;
    }

}