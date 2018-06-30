package com.sample_mvvm.di.module;

import android.arch.lifecycle.ViewModelProvider;

import com.sample_mvvm.di.factory.ViewModelProviderFactory;
import com.sample_mvvm.ui.main.MainViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    MainViewModel provideMainViewModel() {
        return new MainViewModel();
    }

    @Provides
    ViewModelProvider.Factory mainViewModelProvider(MainViewModel mainViewModel) {
        return new ViewModelProviderFactory<>(mainViewModel);
    }
}
