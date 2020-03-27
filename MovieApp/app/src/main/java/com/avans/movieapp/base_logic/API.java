package com.avans.movieapp.base_logic;

import android.util.Log;

import com.avans.movieapp.helpers.BinaryData;
import com.avans.movieapp.interfaces.ICallback;
import com.avans.movieapp.helpers.RequestMethod;
import com.avans.movieapp.models.Movie;
import com.avans.movieapp.models.MovieDetails;
import com.avans.movieapp.network.NetworkTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    private static final String JSON_RUNTIME = "runtime";
    private static final String JSON_GENRES = "genres";
    private static final String JSON_NAME = "name";
    private static final String JSON_PRODUCTION_COMPANIES = "production_companies";
    private static final String JSON_CAST = "cast";
    private static final String JSON_CREW = "crew";
    private static final String JSON_JOB = "job";

    /**
     * Get request token to ask user for permission
     * Then ask the user for permission
     *
     * @param callback Returns String in callback data
     */
    public static void getRequestToken(ICallback callback) {
        NetworkTask nt = Security.getAPI3NetworkTask((data, success) -> {
            if (success) {
                try {
                    JSONObject o = ((BinaryData) data).toJSONObject();
                    if (o.optBoolean("success", false)) {
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
     *
     * @param requestToken The request token. Must be granted by the user
     * @param callback     Returns string in callback data
     */
    public static void getSessionId(String requestToken, ICallback callback) {
        NetworkTask nt = Security.getAPI3NetworkTask((data, success) -> {
            if (success) {
                try {
                    JSONObject o = ((BinaryData) data).toJSONObject();
                    if (o.optBoolean("success", false)) {
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

    public static void getMovieDetails(Movie movie, ICallback callback) {
        NetworkTask nt = new NetworkTask((data, success) -> {
            if(success) {
                getMovieCreditsJsonObject(movie, (data1, success1) -> {
                    if(success1) {
                        try {
                            JSONObject movieCredits = (JSONObject)data1;
                            JSONObject movieDetails = ((BinaryData)data).toJSONObject();

                            //Get movieDetails
                            MovieDetails m = new MovieDetails(movie);
                            m.setLength(movieDetails.optInt(JSON_RUNTIME));

                            JSONArray genres = movieDetails.optJSONArray(JSON_GENRES);
                            if(genres != null && genres.length() > 0)
                                m.setGenre(genres.getJSONObject(0).optString(JSON_NAME));

                            JSONArray companies = movieCredits.optJSONArray(JSON_PRODUCTION_COMPANIES);
                            if(companies != null && companies.length() > 0)
                                m.setCompany(companies.getJSONObject(0).optString(JSON_NAME));

                            JSONArray casts = movieCredits.optJSONArray(JSON_CAST);
                            ArrayList<String> actors = new ArrayList<>();
                            if(casts != null) {
                                for (int i = 0; i < Math.min(3, casts.length()); i++)
                                    actors.add(casts.getJSONObject(i).optString(JSON_NAME));
                            }

                            m.setActors(actors);

                            JSONArray crew = movieCredits.optJSONArray(JSON_CREW);
                            if(crew != null) {
                                for (int i = 0; i < crew.length(); i++) {
                                    JSONObject crewMember = crew.getJSONObject(i);
                                    if (crewMember.optString(JSON_JOB, "").equals("Director")) {
                                        m.setDirector(crewMember.optString(JSON_NAME));
                                        break;
                                    }
                                }
                            }

                            callback.callback(m, true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Failed to parse JSON");
                            callback.callback(null, false);
                        }
                    }
                });
            } else {
                Log.e(TAG, "Failed to get movie details");
                callback.callback(null, false);
            }
        });
        nt.addParameter("api_key", "e4324f0349da1f199362d20965c34a40");
        nt.execute("https://api.themoviedb.org/3/movie/" + movie.getId());
    }

    public static void getMovieCreditsJsonObject(Movie movie, ICallback callback) {
        NetworkTask networkTask = new NetworkTask(RequestMethod.GET, ((data, success) -> {
            if (success) {
                BinaryData binaryData = (BinaryData) data;
                try {
                    JSONObject JSONResult = binaryData.toJSONObject();
                    callback.callback(JSONResult, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to parse json");
                    callback.callback(null, false);
                }
            }
        }));

        networkTask.addParameter("api_key", "e4324f0349da1f199362d20965c34a40");
        networkTask.execute("https://api.themoviedb.org/3/movie/" + movie.getId() + "/credits");
//        https://api.themoviedb.org/3/movie/10191/credits?api_key=0767cc753758bdc7d9556d163b0b3f3d

    }

    /**
     * Search is een zoekterm voor films (vb. Star Wars)
     * Geeeft een ArrayList<movies> terug.
     */
    public static void searchMovies(String search, ICallback callback) {
        NetworkTask networkTask = new NetworkTask(RequestMethod.GET, (data, success) -> {
            if (success) {
                ArrayList<Movie> movies = new ArrayList<>();

                BinaryData binaryData = ((BinaryData) data);

                try {
                    JSONObject JSONResult = binaryData.toJSONObject();
                    JSONArray results = JSONResult.getJSONArray(JSON_RESULTS);
                    for (int i = 0; i < results.length(); i++) {
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
                            Log.d(TAG, "Date: "+releaseDateString);
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        double voteAverage = movie.optDouble(JSON_VOTE_AVERAGE);

                        movies.add(new Movie(id, title, overview,
                                imageUrlPoster, imageUrlBackdrop, isAdult,
                                date, voteAverage));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse json");
                    callback.callback(movies, false);
                }

                callback.callback(movies, true);

            }
        });

        networkTask.addParameter("api_key", "e4324f0349da1f199362d20965c34a40");
        networkTask.addParameter("query", search);
        networkTask.execute("https://api.themoviedb.org/3/search/movie");
    }
}
