package com.sample_mvvm.data.prefs;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by HP on 03/10/2017.
 */

public class MySharedPreferences implements PreferencesHelper {

    private SharedPreferences mSharedPreferences;

    @Inject
    public MySharedPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }


    private static final String PREF_KEY_USER_EMAIL = "PREF_KEY_USER_EMAIL";
    private static final String PREF_KEY_USER_PASSWORD = "PREF_KEY_USER_PASSWORD";

    @Override
    public String getUserEmail() {
        return mSharedPreferences.getString(PREF_KEY_USER_EMAIL, "");
    }

    @Override
    public void setUserEmail(String email) {
        mSharedPreferences.edit().putString(PREF_KEY_USER_EMAIL, email).apply();
    }

    @Override
    public String getUserPassword() {
        return mSharedPreferences.getString(PREF_KEY_USER_PASSWORD, "");
    }

    @Override
    public void setUserPassword(String password) {
        mSharedPreferences.edit().putString(PREF_KEY_USER_PASSWORD, password).apply();
    }
}
