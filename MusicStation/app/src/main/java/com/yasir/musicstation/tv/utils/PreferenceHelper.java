package com.yasir.musicstation.tv.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.GsonBuilder;
import com.yasir.musicstation.tv.R;
import com.yasir.musicstation.tv.models.CategoryModel;
import com.yasir.musicstation.tv.models.DataModel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PreferenceHelper {

    private static final String FILENAME = "MusicStationApp";
    private static final String KEY_MUSIC_STATIONS = "MusicStationData";
    private static String seed;
    private Context mContext;

    public PreferenceHelper(Context context) {
        this.mContext = context;
        seed = context.getString(R.string.key_password);
    }

    public void putAllMusicStations(DataModel dataModel) {
        String encrypted = encrypt(new GsonBuilder().create().toJson(dataModel));
        putStringPreference(mContext, KEY_MUSIC_STATIONS, encrypted);
    }

    public ArrayList<CategoryModel> getAllMusicStations() {
        String decrypted = decrypt(getStringPreference(mContext, KEY_MUSIC_STATIONS));
        DataModel wrapper = new GsonBuilder().create().fromJson(decrypted, DataModel.class);
        if (wrapper != null && wrapper.getData() != null) {
            return wrapper.getData();
        } else {
            return new ArrayList<>();
        }
    }

    public void removeAllMusicStations() {
        removePreference(mContext, KEY_MUSIC_STATIONS);
    }

    // ////////////////////////////////////////////////////////////////////////////////////

    /**
     * SECURE PREFERENCES
     */
    // ////////////////////////////////////////////////////////////////////////////////////
    private String encrypt(String clearText) {
        byte[] encryptedText;
        try {
            byte[] keyData = seed.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, mContext.getString(R.string.aes));
            Cipher c = Cipher.getInstance(mContext.getString(R.string.aes_padding));
            c.init(Cipher.ENCRYPT_MODE, ks);
            encryptedText = c.doFinal(clearText.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    private String decrypt(String encryptedText) {
        byte[] clearText;
        try {
            byte[] keyData = seed.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, mContext.getString(R.string.aes));
            Cipher c = Cipher.getInstance(mContext.getString(R.string.aes_padding));
            c.init(Cipher.DECRYPT_MODE, ks);
            clearText = c.doFinal(Base64.decode(encryptedText, Base64.DEFAULT));
            return new String(clearText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////

    /**
     * GENERAL PREFERENCES
     */

    // ////////////////////////////////////////////////////////////////////////////////////
    private void putStringPreference(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getStringPreference(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    private void removePreference(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
