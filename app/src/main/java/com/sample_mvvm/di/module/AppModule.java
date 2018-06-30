package com.sample_mvvm.di.module;


import android.content.Context;
import android.content.SharedPreferences;

import com.sample_mvvm.data.prefs.MySharedPreferences;
import com.sample_mvvm.data.prefs.PreferencesHelper;
import com.sample_mvvm.define.Constant;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by HP on 03/10/2017.
 */

@Module
public class AppModule {

    private Context m_context;
    private SharedPreferences m_sharedPreference;

    public AppModule(Context m_context) {
        this.m_context = m_context;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }


    @Provides
    SharedPreferences provideSharedPreferences() {
        m_sharedPreference = m_context.getSharedPreferences(Constant.PREF_KEY_NAME, Context.MODE_PRIVATE);
        return m_sharedPreference;
    }

    @Provides
    PreferencesHelper providePreferencesHelper() {
        return new MySharedPreferences(m_sharedPreference);
    }
}
