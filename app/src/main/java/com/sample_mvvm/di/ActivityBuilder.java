package com.sample_mvvm.di;


import com.sample_mvvm.di.module.ActivityModule;
import com.sample_mvvm.di.provider.FragmentProvider;
import com.sample_mvvm.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {ActivityModule.class, FragmentProvider.class})
    abstract MainActivity bindMainActivity();
}
