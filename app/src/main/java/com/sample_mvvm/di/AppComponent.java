package com.sample_mvvm.di;


import android.app.Application;

import com.sample_mvvm.define.MyApplication;
import com.sample_mvvm.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, ActivityBuilder.class, AppModule.class})

public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

        Builder appModule(AppModule appModule);
    }

    void inject(MyApplication app);
}
