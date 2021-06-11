package com.yasir.musicstation.tv.network;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class CallbackRetrofit<T> implements Callback<T> {
    /* Success Callback */
    public abstract void on200(T value, Response response);
    /* No Content Callback */
    public abstract void on204(T value, Response response);
    /* Bad request Callback */
    public abstract void on400(RetrofitError error);
    /* Unauthorized Callback */
    public abstract void on401(RetrofitError error);
    /* Forbidden Callback */
    public abstract void on403(RetrofitError error);
    /* Not Found Callback */
    public abstract void on404(RetrofitError error);
    /* Other Error Callback */
    public abstract void onFailure(RetrofitError error);
    @Override
    public void success(T object, Response response) {
        switch (response.getStatus()) {
            case 204:
                on204(object, response);
                break;
            case 200:
                on200(object, response);
                break;
            default:
                break;
        }
    }
    @Override
    public void failure(RetrofitError error) {
        if (error.getResponse() != null) {
            final Response response = error.getResponse();
            switch (response.getStatus()) {
                case 400:
                    on400(error);
                    break;
                case 401:
                    on401(error);
                    break;
                case 403:
                    on403(error);
                    break;
                case 404:
                    on404(error);
                    break;
                default:
                    onFailure(error);
            }
        } else
            onFailure(error);
    }
}