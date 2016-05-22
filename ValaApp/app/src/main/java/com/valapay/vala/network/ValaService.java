package com.valapay.vala.network;

import com.valapay.vala.common.UserLoginMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ValaService {

    @POST("/unAuth/login")
    Call<UserLoginMessage> login(@Body UserLoginMessage user);
}
