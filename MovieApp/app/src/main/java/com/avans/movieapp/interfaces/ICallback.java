package com.avans.movieapp.interfaces;

import org.json.JSONException;

/**
 * Implement this callback with a lambda to use it
 */
public interface ICallback {
    void callback(Object data, boolean success) throws JSONException;
}
