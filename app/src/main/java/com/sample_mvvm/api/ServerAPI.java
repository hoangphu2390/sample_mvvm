package com.sample_mvvm.api;

import com.sample_mvvm.data.response.AuthLoginResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by HP on 30/06/2018.
 */

public interface ServerAPI {

    public static String SERVER_ADDRESS = "http://api.istream4you.com/api/";

    //TODO ***** AUTHENCATION
    String PATH_LOGIN = "v2/user/login";

    @FormUrlEncoded
    @POST(PATH_LOGIN)
    Observable<AuthLoginResponse> loginApp(@Field("email") String email,
                                           @Field("password") String password);
}
