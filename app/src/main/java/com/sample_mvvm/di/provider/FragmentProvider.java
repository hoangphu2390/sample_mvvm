package com.sample_mvvm.di.provider;

import com.sample_mvvm.di.module.FragmentModule;
import com.sample_mvvm.ui.login.LoginFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentProvider {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract LoginFragment provideLoginFragmentFactory();
}