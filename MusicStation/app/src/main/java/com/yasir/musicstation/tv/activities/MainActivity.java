package com.yasir.musicstation.tv.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.yasir.musicstation.tv.R;

import java.util.ArrayList;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
    }

    public void checkPermissions() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        Permissions.check(this, permissions, getResources().getString(R.string.permission_request), null, new PermissionHandler() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
            }
        });
    }
}