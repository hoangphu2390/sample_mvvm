package com.sample_mvvm.base;

import android.arch.lifecycle.ViewModel;

import com.sample_mvvm.ui.main.MainActivity;
import com.sample_mvvm.api.Dependencies;
import com.sample_mvvm.api.ServerAPI;
import com.sample_mvvm.data.prefs.PreferencesHelper;

public abstract class BaseViewModel extends ViewModel {

    private ServerAPI m_serverAPI;
    protected static MainActivity mainAT;
    protected static PreferencesHelper s_preferencesHelper;

    public abstract void initViewModel();

    public void onViewCreated() {
        this.m_serverAPI = Dependencies.getServerAPI();
        initViewModel();
    }

    public void setMainAT(MainActivity mainAT) {
        this.mainAT = mainAT;
    }

    protected void setLoadingProgress(boolean progressState) {
        if (progressState) mainAT.showProgressBar();
        else mainAT.hideProgressBar();
    }

    public void setPreferencesHelper(PreferencesHelper preferencesHelper) {
        this.s_preferencesHelper = preferencesHelper;
    }

    public ServerAPI getServerAPI() {
        return m_serverAPI;
    }
}
