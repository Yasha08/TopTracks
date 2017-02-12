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
public class Qutils {

    private Qutils (){

    }
    public static List<Artists> fetchArtists(String requestUrl) {
        String jsonResponse = null;
        URL url = createUrl(requestUrl);

        try {
            jsonResponse = makeHttprequest(url);
        } catch (IOException e) {
            Log.e("Qutils", "Problem making Http request", e);
        }

        List<Artists> artistses = getArtistsFromJson(jsonResponse);

        return artistses;
    }


    private static URL createUrl(String url){
        URL createurl = null;
        try {
            createurl = new URL(url);
        } catch (MalformedURLException e) {
            Log.e("Qutils", "Problem creating URL", e);
        }
        return createurl;
    }

    private static String makeHttprequest (URL url) throws IOException {

        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Artists> getArtistsFromJson (String jsonResponse){

        List <Artists> artistses = new ArrayList<>();

        try{

            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONObject artistsObject = baseJsonObject.getJSONObject("artists");
            JSONArray itemsArray = artistsObject.getJSONArray("items");

            for( int i = 0; i<itemsArray.length(); i++){

                JSONObject itemObjects = itemsArray.getJSONObject(i);

                String artistName = itemObjects.getString("name");
                String artistId = itemObjects.getString("id");

                JSONArray images = itemObjects.getJSONArray("images");
                JSONObject imageObject = images.getJSONObject(1);
                String imageLink = imageObject.getString("url");

                JSONObject imageObjectLarge = images.getJSONObject(0);
                String imageLinkHero = imageObjectLarge.getString("url");

                JSONObject followersObject = itemObjects.getJSONObject("followers");
                long artistFollowers = followersObject.getLong("total");

                JSONArray genreArray = itemObjects.getJSONArray("genres");
                String genres = "";

                if(genreArray.length() == 0){
                    genres = "Unknown";
                }
                else{
                    for(int j = 0; j < genreArray.length(); j++ ){
                        genres = genres + genreArray.getString(j) + ". ";
                    }
                }
                Artists artists = new Artists(artistName, artistFollowers, artistId, genres, imageLink, imageLinkHero);

                artistses.add(artists);
            }
        }
        catch (JSONException e){
            Log.e("Qutils", "error parsing JSON", e);
        }
        return artistses;
    }
}
