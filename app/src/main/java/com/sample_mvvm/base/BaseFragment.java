package com.sample_mvvm.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.sample_mvvm.R;
import com.sample_mvvm.data.prefs.PreferencesHelper;
import com.sample_mvvm.di.Injectable;
import com.sample_mvvm.ui.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;

public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment
        implements Injectable {

    @Inject
    protected EventBus m_eventBus;

    @Inject
    protected SharedPreferences m_sharedPreferences;

    @Inject
    protected PreferencesHelper m_preferencesHelper;

    private BaseActivity m_activity;
    private T m_viewDataBinding;
    private V m_viewModel;
    private View m_rootView;
    protected MainActivity mainAT;

    public boolean isInLeft;
    public boolean isOutLeft;
    public boolean isCurrentScreen;
    public boolean isLoaded = false;
    public boolean isDead = false;
    private Object object = new Object();

    public int getLeftInAnimId() {
        return R.anim.slide_in_left;
    }

    public int getRightInAnimId() {
        return R.anim.slide_in_right;
    }

    public int getLeftOutAnimId() {
        return R.anim.slide_out_left;
    }

    public int getRightOutAnimId() {
        return R.anim.slide_out_right;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainAT = (MainActivity) context;
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.m_activity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (isCurrentScreen) {
            initView();
            setNavigation();
        }

        Animation animation = null;
        if (enter) {
            int left = getLeftInAnimId();
            int right = getRightInAnimId();
            animation = AnimationUtils.loadAnimation(getActivity(), isInLeft ? left : right);
        } else {
            int left = getLeftOutAnimId();
            int right = getRightOutAnimId();
            animation = AnimationUtils.loadAnimation(getActivity(), isOutLeft ? left : right);
        }
        if (animation != null) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        initEvent();
        return animation;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            m_viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            m_rootView = m_viewDataBinding.getRoot();
            ButterKnife.bind(this, m_rootView);
        } catch (Exception ex) {
        }
        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        synchronized (object) {
            isLoaded = true;
            object.notifyAll();
        }
        super.onViewCreated(view, savedInstanceState);

        m_viewModel = getViewModel();
        m_viewDataBinding.setVariable(getBindingVariable(), m_viewModel);
        m_viewDataBinding.executePendingBindings();
        m_viewModel.onViewCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        m_activity = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoaded = false;
        mainAT = null;
    }

    @Override
    public void onDestroyView() {
        isDead = true;
        super.onDestroyView();
    }

    public BaseActivity getBaseActivity() {
        return m_activity;
    }

    public T getViewDataBinding() {
        return m_viewDataBinding;
    }

    public void setNavigation() {
        if (mainAT != null) {
            mainAT.setTitleScreen(getTitleScreen());
            mainAT.setStateButtonMenu(getStateButtonMenu());
            mainAT.setStateButtonBack(getStateButtonBack());
        }
    }

    protected void initView() {
    }

    protected void initEvent() {
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    /**
     * Override for set binding variable
     *
     * @return variable card_id
     */
    public abstract
    @IdRes
    int getBindingVariable();

    /**
     * @return layout resource card_id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

//    public abstract void performDependencyInjection();

    public abstract String getTitleScreen();

    public abstract boolean getStateButtonBack();

    public abstract boolean getStateButtonMenu();
}
