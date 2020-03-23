package com.avans.movieapp.base_logic;

import com.avans.movieapp.interfaces.ICallback;
import com.avans.movieapp.network.NetworkTask;

public class Security {
    private static final String keyV4 = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNDMyNGYwMzQ5ZGExZjE5OTM2MmQyMDk2NWMzNGE0MCIsInN1YiI6IjVlNzhhODU2MmYzYjE3MDAxOTU0YjQwMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.HXngkMgAjCp-JjJFq-aEDy2XFERouZtyk8cl5a0WXe4";
    private static final String keyV3 = "e4324f0349da1f199362d20965c34a40";
    private static final String TAG = Security.class.getSimpleName();

    //API v4 template NetworkTask
    public static NetworkTask getAPI4NetworkTask(ICallback callback) {
        NetworkTask nt = new NetworkTask(callback);
        nt.addHeader("Authorization", "Bearer "+keyV4);
        return nt;
    }

    //API v3 template NetworkTask
    public static NetworkTask getAPI3NetworkTask(ICallback callback) {
        NetworkTask nt = new NetworkTask(callback);
        nt.addParameter("api_key", keyV3);
        return nt;
    }
}