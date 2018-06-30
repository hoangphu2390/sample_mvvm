package com.sample_mvvm.ui.login;

import android.os.Bundle;
import android.view.View;

import com.sample_mvvm.R;
import com.sample_mvvm.base.BaseFragment;
import com.sample_mvvm.databinding.FragmentLoginBinding;
import com.sample_mvvm.util.Utils;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.OnClick;

/**
 * Created by HP on 30/06/2018.
 */

public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel> {

    @BindString(R.string.login_screen)
    String m_titleScreen;

    @Inject
    LoginViewModel m_loginViewModel;

    @Override
    public LoginViewModel getViewModel() {
        return m_loginViewModel;
    }

    @Override
    public int getBindingVariable() {
        return com.sample_mvvm.BR.loginviewmodel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public String getTitleScreen() {
        return m_titleScreen;
    }

    @Override
    public boolean getStateButtonBack() {
        return false;
    }

    @Override
    public boolean getStateButtonMenu() {
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(m_preferencesHelper.getUserEmail().isEmpty() && m_preferencesHelper.getUserPassword().isEmpty()){
            m_preferencesHelper.setUserEmail("hai01@gmail.com");
            m_preferencesHelper.setUserPassword("123456");
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_login)
    public void clickLogin() {
        m_loginViewModel.onLoginApp().observe(this, user -> {
            if (user == null) return;
            Utils.showToast("Login Successful. Get User's Price: $" + user.price);
        });
    }
}
