package com.yasir.musicstation.tv.network;

import com.squareup.okhttp.OkHttpClient;
import com.yasir.musicstation.tv.utils.AppLog;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class WebServiceFactory {

    private final static long readTimeoutValue = 120;
    private final static long connectTimeoutValue = 121;

    public static WebService getInstance() {
        OkHttpClient name = new OkHttpClient();
        name.setReadTimeout(readTimeoutValue, TimeUnit.SECONDS);
        name.setConnectTimeout(connectTimeoutValue, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WebServiceConstants.API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)

                .setClient(new OkClient(name))
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        AppLog.Debug("Web Response =  " + msg);
                    }
                })
                .setConverter(new GSonConverter(GSonFactory.getConfiguredGSon()))
                .build();
        return restAdapter.create(WebService.class);
    }
}
