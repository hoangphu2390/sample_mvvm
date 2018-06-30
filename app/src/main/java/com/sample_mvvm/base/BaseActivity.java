package com.sample_mvvm.base;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.sample_mvvm.data.prefs.PreferencesHelper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;


public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel>
        extends FragmentActivity implements BaseFragment.Callback {

    @Inject
    protected EventBus m_eventBus;

    @Inject
    protected SharedPreferences m_sharedPreferences;

    @Inject
    protected PreferencesHelper m_preferencesHelper;


    private T m_viewDataBinding;
    private V m_viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();
        ButterKnife.bind(this);
    }

    private void performDataBinding() {
        m_viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        m_viewModel = m_viewModel == null ? getViewModel() : m_viewModel;
        m_viewDataBinding.setVariable(getBindingVariable(), m_viewModel);
        m_viewDataBinding.executePendingBindings();
        m_viewModel.setPreferencesHelper(m_preferencesHelper);
        m_viewModel.onViewCreated();
    }

    public T getViewDataBinding() {
        return m_viewDataBinding;
    }

    public abstract V getViewModel();

    @IdRes
    public abstract int getBindingVariable();

    @LayoutRes
    public abstract int getLayoutId();

    @Override
    public void onFragmentAttached() {
    }

    @Override
    public void onFragmentDetached(String tag) {
    }
}

