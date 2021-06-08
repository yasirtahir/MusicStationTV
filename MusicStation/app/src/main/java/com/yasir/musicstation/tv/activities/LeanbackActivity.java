package com.yasir.musicstation.tv.activities;

import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

public abstract class LeanbackActivity extends FragmentActivity {

    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity.class));
        return true;
    }
}