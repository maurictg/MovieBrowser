package com.avans.movieapp.base_logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.avans.movieapp.helpers.BinaryData;
import com.avans.movieapp.helpers.RequestMethod;
import com.avans.movieapp.interfaces.ICallback;
import com.avans.movieapp.models.Genre;
import com.avans.movieapp.models.Movie;
import com.avans.movieapp.models.MovieDetails;
import com.avans.movieapp.models.MovieList;
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
    private static final String JSON_GENRE_IDS = "genre_ids";
    private static final String JSON_ORIGINAL_LANGUAGE = "original_language";

    private static final String JSON_RUNTIME = "runtime";
    private static final String JSON_GENRES = "genres";
    private static final String JSON_NAME = "name";
    private static final String JSON_PRODUCTION_COMPANIES = "production_companies";
    private static final String JSON_CAST = "cast";
    private static final String JSON_CREW = "crew";
    private static final String JSON_JOB = "job";

    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_ITEM_COUNT = "item_count";
    private static final String JSON_ITEMS = "items";

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

    public static void deleteList(MovieList movieList) {
        NetworkTask networkTask = new NetworkTask(RequestMethod.DELETE, ((data, success) -> {

        }));

        int movieListId = movieList.getId();

        networkTask.addParameter("api_key", "0767cc753758bdc7d9556d163b0b3f3d");
        networkTask.addParameter("session_id", "61a26c854ae3c0b7fb9422cada90dd1773a98146");
        networkTask.execute(" https://api.themoviedb.org/3/list/ " + movieListId);
//        https://api.themoviedb.org/3/list/137552?api_key=0767cc753758bdc7d9556d163b0b3f3d&session_id=61a26c854ae3c0b7fb9422cada90dd1773a98146
    }


    public static void createList(String name, String description) {
        NetworkTask networkTask = new NetworkTask(RequestMethod.POST, ((data, success) -> {

        }));

        networkTask.addParameter("api_key", "0767cc753758bdc7d9556d163b0b3f3d");
        networkTask.addParameter("session_id", "61a26c854ae3c0b7fb9422cada90dd1773a98146");
        networkTask.addFormData("name", name);
        networkTask.addFormData("description", description);
        networkTask.addFormData("language", "en");
        networkTask.execute("https://api.themoviedb.org/3/list");

//        https://api.themoviedb.org/3/list?api_key=0767cc753758bdc7d9556d163b0b3f3d&session_id=61a26c854ae3c0b7fb9422cada90dd1773a98146

    }


    public static void getLists(ICallback callback) {
        NetworkTask networkTask = new NetworkTask(RequestMethod.GET, ((data, success) -> {
            if (success) {
                ArrayList<MovieList> movieLists = new ArrayList<>();
                BinaryData binaryData = (BinaryData) data;

                try {
                    JSONObject JSONResult = binaryData.toJSONObject();
                    JSONArray results = JSONResult.optJSONArray(JSON_RESULTS);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movieList = (JSONObject) results.get(i);
                        String description = movieList.optString(JSON_DESCRIPTION);
                        int id = movieList.optInt(JSON_ID);
                        int itemCount = movieList.optInt(JSON_ITEM_COUNT);
                        String name = movieList.optString(JSON_NAME);

                        movieLists.add(new MovieList(description, id, itemCount, name));
                    }

                    callback.callback(movieLists, true);

                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse json");
                    callback.callback(movieLists, false);
                }
            }
        }));

        networkTask.addParameter("api_key", "0767cc753758bdc7d9556d163b0b3f3d");
        networkTask.addParameter("session_id", "61a26c854ae3c0b7fb9422cada90dd1773a98146");
        networkTask.execute("https://api.themoviedb.org/3/account/" + 9160674 + "/lists");
//        https://api.themoviedb.org/3/account/9160674/lists?api_key=0767cc753758bdc7d9556d163b0b3f3d&language=en-US&session_id=61a26c854ae3c0b7fb9422cada90dd1773a98146&page=1
    }

    public static void getMoviesFromMovieList(MovieList movieList, ICallback callback) {
        int movieListId = movieList.getId();
        NetworkTask networkTask = new NetworkTask(RequestMethod.GET, ((data, success) -> {
            if (success) {
                BinaryData binaryData = (BinaryData) data;
                ArrayList<Movie> movies = new ArrayList<>();
                try {
                    JSONObject JSONResult = binaryData.toJSONObject();
                    JSONArray items = JSONResult.optJSONArray(JSON_ITEMS);
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject jsonMovie = (JSONObject) items.get(i);

                        Movie movie = parseMovie(jsonMovie);

                        movies.add(movie);
                    }

                    callback.callback(movies, success);

                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse json");
                    callback.callback(null, false);
                }
            }

        }));

        networkTask.addParameter("api_key", "0767cc753758bdc7d9556d163b0b3f3d");
        networkTask.execute(" https://api.themoviedb.org/3/list/" + movieListId);
//         https://api.themoviedb.org/3/list/137504?api_key=0767cc753758bdc7d9556d163b0b3f3d&language=en-US

    }


    /**
     * * geeft een Arraylist<Genre> genres terug.
     * https://api.themoviedb.org/3/genre/movie/list?api_key=0767cc753758bdc7d9556d163b0b3f3d&language=en-US
     * Lauran, Maurice
     */
    public static void getGenres(Context context, ICallback callback) {

        SharedPreferences sp = context.getSharedPreferences("MOVIEDB", Context.MODE_PRIVATE);
        String strGenres = sp.getString("genres", "");
        if (!strGenres.equals("")) {
            callback.callback(API.parseGenres(strGenres), true);
            Log.d(TAG, "Returned genres from device storage");
            return;
        }

        NetworkTask nt = new NetworkTask(RequestMethod.GET, (data, success) -> {
            if (success) {
                ArrayList<Genre> genres = API.parseGenres(data.toString());
                if (genres != null) {
                    callback.callback(genres, true);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("genres", data.toString());
                    editor.apply();
                    Log.d(TAG, "Returned genres from API");
                } else
                    callback.callback(null, false);
            } else {
                callback.callback(null, false);
            }

        });

        nt.addParameter("api_key", "e4324f0349da1f199362d20965c34a40");
        nt.execute("https://api.themoviedb.org/3/genre/movie/list");
//        https://api.themoviedb.org/3/genre/movie/list?api_key=0767cc753758bdc7d9556d163b0b3f3d&language=en-US
    }

    private static ArrayList<Genre> parseGenres(String json) {
        ArrayList<Genre> genres = new ArrayList<>();
        try {
            JSONObject result = new JSONObject(json);
            JSONArray jsonGenres = result.getJSONArray(JSON_GENRES);
            for (int i = 0; i < jsonGenres.length(); i++) {
                JSONObject arrayResult = jsonGenres.getJSONObject(i);
                String genreName = arrayResult.optString(JSON_NAME);
                int genreId = arrayResult.optInt(JSON_ID);
                genres.add(new Genre(genreName, genreId));
            }

            return genres;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to parse JSON getGenres");
            return null;
        }
    }

    /**
     * Movie is een film die hij binnenkrijgt.
     * * geeft een moviedetails klasse terug.
     * https://developers.themoviedb.org/3/movies/get-movie-details
     * roept ook getcast aan om de cast te krijgen (staat niet in detials)
     * Lauran
     */

    public static void getMovieDetails(Movie movie, ICallback callback) {
        NetworkTask nt = new NetworkTask((data, success) -> {
            if (success) {
                getMovieCreditsJsonObject(movie, (data1, success1) -> {
                    if (success1) {
                        try {
                            JSONObject movieCredits = (JSONObject) data1;
                            JSONObject movieDetails = ((BinaryData) data).toJSONObject();

                            //Get movieDetails
                            MovieDetails m = new MovieDetails(movie);
                            m.setLength(movieDetails.optInt(JSON_RUNTIME));

                            JSONArray genres = movieDetails.optJSONArray(JSON_GENRES);
                            if (genres != null && genres.length() > 0)
                                m.setGenre(genres.getJSONObject(0).optString(JSON_NAME));

                            JSONArray companies = movieCredits.optJSONArray(JSON_PRODUCTION_COMPANIES);
                            if (companies != null && companies.length() > 0)
                                m.setCompany(companies.getJSONObject(0).optString(JSON_NAME));

                            JSONArray casts = movieCredits.optJSONArray(JSON_CAST);
                            ArrayList<String> actors = new ArrayList<>();
                            if (casts != null) {
                                for (int i = 0; i < Math.min(3, casts.length()); i++)
                                    actors.add(casts.getJSONObject(i).optString(JSON_NAME));
                            }

                            m.setActors(actors);

                            JSONArray crew = movieCredits.optJSONArray(JSON_CREW);
                            if (crew != null) {
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

    /**
     * Movie is een film die hij binnenkrijgt.
     * * Geeeft een JSONobject terug waarin de cast staat.
     * https://developers.themoviedb.org/3/movies/get-movie-credits
     * Lauran
     */

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
     * https://developers.themoviedb.org/3/getting-started/search-and-query-for-details
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

//                        Commentaar hieronder staat nu in de methode parseMovie()
                        movies.add(parseMovie(movie));

//                        int id = movie.optInt(JSON_ID);
//                        String title = movie.optString(JSON_TITLE);
//                        String overview = movie.optString(JSON_OVERVIEW);
//                        String imageUrlPoster = imageUrlLocation + imageUrlOriginalWidth + movie.optString(JSON_IMAGE_URL_POSTER);
//                        String imageUrlBackdrop = imageUrlLocation + imageUrlOriginalWidth + movie.optString(JSON_IMAGE_URL_BACKDROP);
//                        boolean isAdult = movie.optBoolean(JSON_ADULT);
//                        String releaseDateString = movie.optString(JSON_DATE);
//                        Date date = null;
//                        try {
//                            Log.d(TAG, "Date: "+releaseDateString);
//                            date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        double voteAverage = movie.optDouble(JSON_VOTE_AVERAGE);
//
//
//                        JSONArray jsonGenreIds = movie.optJSONArray(JSON_GENRE_IDS);
//                        ArrayList<Integer> genreIds = new ArrayList<>();
//                        if (jsonGenreIds != null && jsonGenreIds.length() > 0){
//                            for (int j = 0; j < jsonGenreIds.length(); j++){
//                                int a = jsonGenreIds.optInt(j);
//                                genreIds.add(j);
//                            }
//                        }
//
//                        String originalLanguage = movie.optString(JSON_ORIGINAL_LANGUAGE);
//
//
//                        movies.add(new Movie(id, title, overview,
//                                imageUrlPoster, imageUrlBackdrop, isAdult,
//                                date, voteAverage, genreIds, originalLanguage));
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

    public static Movie parseMovie(JSONObject movie) {

        int id = movie.optInt(JSON_ID);
        String title = movie.optString(JSON_TITLE);
        String overview = movie.optString(JSON_OVERVIEW);
        String imageUrlPoster = imageUrlLocation + imageUrlOriginalWidth + movie.optString(JSON_IMAGE_URL_POSTER);
        String imageUrlBackdrop = imageUrlLocation + imageUrlOriginalWidth + movie.optString(JSON_IMAGE_URL_BACKDROP);
        boolean isAdult = movie.optBoolean(JSON_ADULT);
        String releaseDateString = movie.optString(JSON_DATE);
        Date date = null;
        try {
            Log.d(TAG, "Date: " + releaseDateString);
            date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double voteAverage = movie.optDouble(JSON_VOTE_AVERAGE);


        JSONArray jsonGenreIds = movie.optJSONArray(JSON_GENRE_IDS);
        ArrayList<Integer> genreIds = new ArrayList<>();
        if (jsonGenreIds != null && jsonGenreIds.length() > 0) {
            for (int j = 0; j < jsonGenreIds.length(); j++) {
                int a = jsonGenreIds.optInt(j);
                genreIds.add(a);
            }
        }

        String originalLanguage = movie.optString(JSON_ORIGINAL_LANGUAGE);

        return new Movie(id, title, overview,
                imageUrlPoster, imageUrlBackdrop, isAdult,
                date, voteAverage, genreIds, originalLanguage);
    }

    public static void discover(ICallback callback) {
        ArrayList<Movie> movies = new ArrayList<>();
        NetworkTask nt = Security.getAPI3NetworkTask((data, success) -> {
            if(success) {
                try {
                    JSONObject json = ((BinaryData)data).toJSONObject();
                    JSONArray results = json.optJSONArray("results");
                    if(results != null) {
                        for (int i = 0; i < results.length(); i++) {
                            movies.add(API.parseMovie(results.getJSONObject(i)));
                        }
                    }
                    callback.callback(movies, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.callback(null, false);
                }
            }
        });
        nt.execute("https://api.themoviedb.org/3/discover/movie");
        //See https://developers.themoviedb.org/3/discover/movie-discover
    }

    public static void getMovieById(String id, ICallback callback) {
        NetworkTask nt = Security.getAPI3NetworkTask((data, success) -> {
            if(success) {
                try {
                    JSONObject res = ((BinaryData)data).toJSONObject();
                    Movie m = parseMovie(res);
                    callback.callback(m, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.callback(null, false);
                }
            }
        });
        nt.execute("https://api.themoviedb.org/3/movie/"+id);
    }
}
