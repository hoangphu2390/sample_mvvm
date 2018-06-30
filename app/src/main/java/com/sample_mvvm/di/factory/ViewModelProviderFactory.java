package com.sample_mvvm.di.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class ViewModelProviderFactory<V> implements ViewModelProvider.Factory {

    private V m_viewModel;

    public ViewModelProviderFactory(V m_viewModel) {
        this.m_viewModel = m_viewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(m_viewModel.getClass())) {
            return (T) m_viewModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
