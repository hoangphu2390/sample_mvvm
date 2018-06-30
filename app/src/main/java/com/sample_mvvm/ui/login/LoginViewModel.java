package com.sample_mvvm.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.sample_mvvm.base.BaseViewModel;
import com.sample_mvvm.data.response.AuthLoginResponse.User;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HP on 30/06/2018.
 */

public class LoginViewModel extends BaseViewModel {

    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();

    @Override
    public void initViewModel() {
        email.set(s_preferencesHelper.getUserEmail());
        password.set(s_preferencesHelper.getUserPassword());
    }

    public LiveData<User> onLoginApp() {
        final MutableLiveData<User> result_user = new MutableLiveData<>();
        setLoadingProgress(true);
        getServerAPI().loginApp(email.get(), password.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setLoadingProgress(false);
                    result_user.setValue(response.user);
                }, throwable -> {
                    setLoadingProgress(false);
                    result_user.setValue(null);
                });
        return result_user;
    }
}
