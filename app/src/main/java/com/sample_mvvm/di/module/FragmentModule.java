package com.sample_mvvm.di.module;


import com.sample_mvvm.ui.login.LoginViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    @Provides
    LoginViewModel provideLoginViewModel() {
        return new LoginViewModel();
    }
}
