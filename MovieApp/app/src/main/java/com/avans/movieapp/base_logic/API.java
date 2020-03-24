package com.avans.movieapp.base_logic;

import android.util.JsonReader;
import android.util.Log;

import com.avans.movieapp.helpers.BinaryData;
import com.avans.movieapp.interfaces.ICallback;
import com.avans.movieapp.helpers.RequestMethod;
import com.avans.movieapp.models.Movie;
import com.avans.movieapp.network.NetworkTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class API {
    private static final String TAG = API.class.getSimpleName();

    private static final String imageUrlLocation = "https://image.tmdb.org/t/p/";
    private static final String imageUrlOriginalWidth = "original";

    private static final String JSON_RESULTS = "results";
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_OVERVIEW = "overview";
    private static final String JSON_IMAGE_URL_POSTER = "poster_path";
    private static final String JSON_IMAGE_URL_BACKDROP = "backdrop_path";
    private static final String JSON_ADULT = "adult";
    private static final String JSON_DATE = "release_date";
    private static final String JSON_VOTE_AVERAGE = "vote_average";

    /**
     * Get request token to ask user for permission
     * Then ask the user for permission
     * @param callback Returns String in callback data
     */
    public static void getRequestToken(ICallback callback) {
        NetworkTask nt = Security.getAPI3NetworkTask((data, success) -> {
            if(success) {
                try {
                    JSONObject o = ((BinaryData)data).toJSONObject();
                    if(o.optBoolean("success", false)) {
                        callback.callback(o.getString("request_token"), true);
                    } else {
                        callback.callback(null, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse JSON");
                    callback.callback(null, false);
                }
            } else {
                callback.callback(null, false);
            }
        });

        nt.execute("https://api.themoviedb.org/3/authentication/token/new");
    }

    /**
     * Get session ID with requestToken
     * @param requestToken The request token. Must be granted by the user
     * @param callback Returns string in callback data
     */
    public static void getSessionId(String requestToken, ICallback callback) {
        NetworkTask nt = Security.getAPI3NetworkTask((data,success) -> {
            if(success) {
                try {
                    JSONObject o = ((BinaryData)data).toJSONObject();
                    if(o.optBoolean("success", false)) {
                        callback.callback(o.getString("session_id"), true);
                    } else {
                        callback.callback(null, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse JSON");
                    callback.callback(null, false);
                }
            } else {
                callback.callback(null, false);
            }
        });

        nt.setRequestMethod(RequestMethod.POST);
        nt.addFormData("request_token", requestToken);
        nt.execute("https://api.themoviedb.org/3/authentication/session/new");
    }

    /**
     * Search is een zoekterm voor films (vb. Star Wars)
     * Geeeft een ArrayList<movies> terug.
     */
    public static void searchMovies(String search, ICallback callback){
        NetworkTask networkTask = new NetworkTask(RequestMethod.GET, (data, success) -> {
            if (success){
                BinaryData binaryData = ((BinaryData)data);

                ArrayList<Movie> movies = new ArrayList<>();

                JSONObject JSONResult = binaryData.toJSONObject();
                JSONArray results = JSONResult.getJSONArray(JSON_RESULTS);
                for (int i = 0; i < results.length(); i++){
                    JSONObject movie = (JSONObject) results.get(i);

                    int id = movie.optInt(JSON_ID);
                    String title = movie.optString(JSON_TITLE);
                    String overview = movie.optString(JSON_OVERVIEW);
                    String imageUrlPoster = imageUrlLocation + imageUrlOriginalWidth + movie.optString(JSON_IMAGE_URL_POSTER);
                    String imageUrlBackdrop = imageUrlLocation + imageUrlOriginalWidth + movie.optString(JSON_IMAGE_URL_BACKDROP);
                    boolean isAdult = movie.optBoolean(JSON_ADULT);
                    String releaseDateString = movie.optString(JSON_DATE);
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    double voteAverage = movie.optDouble(JSON_VOTE_AVERAGE);

                    movies.add(new Movie(   id, title, overview,
                                            imageUrlPoster, imageUrlBackdrop, isAdult,
                                            date, voteAverage));
                }

                callback.callback(movies, true);

            }
        });

        networkTask.addParameter("api_key", "e4324f0349da1f199362d20965c34a40");
        networkTask.addParameter("query", search);
        networkTask.execute("https://api.themoviedb.org/3/search/movie");
    }
}
