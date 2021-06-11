package com.yasir.musicstation.tv.network;

import com.yasir.musicstation.tv.models.DataModel;

import retrofit.http.GET;

public interface WebService {

    // RESTful service interface will be written here
    // This class is to write all of the methods of the RESTful API

    @GET("/api/getMusicStations")
    public void getMusicStations(
            CallbackRetrofit<DataModel> callbackRetrofit
    );
}
