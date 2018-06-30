package com.sample_mvvm.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.sample_mvvm.BR;
import com.sample_mvvm.R;
import com.sample_mvvm.base.BaseActivity;
import com.sample_mvvm.base.BaseFragment;
import com.sample_mvvm.base.FragmentHelper;
import com.sample_mvvm.databinding.ActivityMainBinding;
import com.sample_mvvm.define.service.ConnectionService;
import com.sample_mvvm.ui.login.LoginFragment;
import com.sample_mvvm.util.Utils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements
        ConnectionService.postResultConnection, FragmentHelper.FragmentAction, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> m_fragmentDispatchingAndroidInjector;

    @Inject
    MainViewModel m_mainViewModel;

    private ActivityMainBinding m_binding;
    private FragmentHelper m_fragmentHelper;
    private ConnectionService m_connectionService;
    private boolean m_doubleBackToExitPressedOnce;

    @Override
    public MainViewModel getViewModel() {
        return m_mainViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.mainviewmodel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void postResultConnection(boolean isConnection) {
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return m_fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_mainViewModel.setMainAT(MainActivity.this);
        m_binding = getViewDataBinding();
        m_connectionService = Utils.initConnectionService(this, m_binding.tvConnection, m_binding.frameContainer, this);
        m_fragmentHelper = new FragmentHelper(this);
        if (savedInstanceState == null) {
            m_fragmentHelper.showFragment(0);
            reloadComponent();
        }
        ButterKnife.bind(this);
    }

    private void reloadComponent() {
        String title_screen = "";
        Fragment curFragment = m_fragmentHelper.pageList.get(m_fragmentHelper.getPageIndex()).get(0);
        if (m_fragmentHelper.getCurrentPageSize() == 1) {
            if ((curFragment instanceof LoginFragment))
                title_screen = getString(R.string.login_screen);
            m_binding.tvTitle.setText(title_screen);
            m_binding.btnBack.setVisibility(View.GONE);
        } else {
            m_binding.tvTitle.setText(title_screen);
        }
    }

    @OnClick(R.id.btn_back)
    public void clickBack() {
        if (Utils.isCheckShowSoftKeyboard(this)) Utils.hideSoftKeyboard(this);
        if (m_fragmentHelper.getCurrentPageSize() == 1) {
            if (m_doubleBackToExitPressedOnce) {
                super.onBackPressed();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
            m_doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> m_doubleBackToExitPressedOnce = false, 1000);
        } else {
            m_fragmentHelper.showFragment(0);
        }
        popFragment();
        reloadComponent();
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.destroyConnectionService(this, m_connectionService);
    }

    //TODO: **************************** METHOD HANDLE IN FRAGMENTS *******************************

    public void showProgressBar() {
        m_binding.progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void hideProgressBar() {
        m_binding.progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void setStateButtonBack(boolean isState) {
        m_binding.btnBack.setVisibility(isState ? View.VISIBLE : View.GONE);
    }

    public void setStateButtonMenu(boolean isState) {
        m_binding.btnMenu.setVisibility(isState ? View.VISIBLE : View.GONE);
    }

    public void setTitleScreen(String str) {
        m_binding.tvTitle.setText(str);
    }


    //TODO: ******************************* METHOD RAGMENT HELPER **********************************

    @Override
    public BaseFragment[] initFragment() {
        BaseFragment[] baseFragments = new BaseFragment[1];
        baseFragments[0] = new LoginFragment();
        return baseFragments;
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.frame_container;
    }

    @Override
    public FragmentManager setFragmentManager() {
        return getSupportFragmentManager();
    }

    public void pushFragment(BaseFragment baseFragment) {
        if (Utils.isCheckShowSoftKeyboard(this)) Utils.hideSoftKeyboard(this);
        m_fragmentHelper.pushFragment(baseFragment);
    }

    public void popFragment() {
        m_fragmentHelper.popFragment();
    }

    public FragmentHelper getFragmentHelper() {
        return m_fragmentHelper;
    }
}
