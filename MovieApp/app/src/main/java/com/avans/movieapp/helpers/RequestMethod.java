package com.avans.movieapp.helpers;

public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String method;

    RequestMethod(String method) {
        this.method = method;
    }

    public String get() {
        return method;
    }
}
