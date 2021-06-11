package com.yasir.musicstation.tv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.yasir.musicstation.tv.R;
import com.yasir.musicstation.tv.models.DataModel;
import com.yasir.musicstation.tv.network.CallbackRetrofit;
import com.yasir.musicstation.tv.network.WebServiceFactory;
import com.yasir.musicstation.tv.utils.PreferenceHelper;

import java.net.CookieHandler;
import java.net.CookieManager;

public class SplashActivity extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferenceHelper = new PreferenceHelper(this);

        CookieHandler.setDefault(new CookieManager()); // Setting default CookieManager

        getAllMusicStations(); // Call the API to get all the latest data

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainActivity();
            }
        }, 4200);
    }

    private void showMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getAllMusicStations(){
        WebServiceFactory.getInstance().getMusicStations(new CallbackRetrofit<DataModel>() {
            @Override
            public void on200(DataModel value, Response response) {
                if(value != null && value.getData() != null){
                    preferenceHelper.putAllMusicStations(value);
                }
            }

            @Override
            public void on204(DataModel value, Response response) {

            }

            @Override
            public void on400(RetrofitError error) {

            }

            @Override
            public void on401(RetrofitError error) {

            }

            @Override
            public void on403(RetrofitError error) {

            }

            @Override
            public void on404(RetrofitError error) {

            }

            @Override
            public void onFailure(RetrofitError error) {

            }
        });
    }
}