package com.sample_mvvm.define;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import com.sample_mvvm.BuildConfig;
import com.sample_mvvm.api.Dependencies;
import com.sample_mvvm.di.AppInjector;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class MyApplication extends MultiDexApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> m_activityDispatchingAndroidInjector;

    private static MyApplication s_instance;

    @Override
    public void onCreate() {
        super.onCreate();
        s_instance = this;
        Dependencies.init();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        AppInjector.init(this);
    }

    public static MyApplication getInstance() {
        return s_instance;
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return m_activityDispatchingAndroidInjector;
    }
}
