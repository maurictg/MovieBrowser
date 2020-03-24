package com.avans.movieapp.base_logic;

import android.util.Log;

import com.avans.movieapp.helpers.BinaryData;
import com.avans.movieapp.interfaces.ICallback;
import com.avans.movieapp.helpers.RequestMethod;
import com.avans.movieapp.network.NetworkTask;

import org.json.JSONException;
import org.json.JSONObject;

public class API {
    private static final String TAG = API.class.getSimpleName();

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

    public static void searchMovie(String search, ICallback callback){
        NetworkTask networkTask = new NetworkTask(RequestMethod.GET, (data, success) -> {
            if (success){
                BinaryData binaryData = ((BinaryData)data);
                JSONObject JSON = binaryData.toJSONObject();


                callback.callback(JSON, true);




            }
        });


        networkTask.addParameter("api_key", "e4324f0349da1f199362d20965c34a40");
        networkTask.addParameter("query", search);
        networkTask.execute("https://api.themoviedb.org/3/search/movie");
    }
}
