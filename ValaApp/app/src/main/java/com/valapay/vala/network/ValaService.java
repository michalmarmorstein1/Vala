package com.valapay.vala.network;

import com.valapay.vala.common.UserLoginMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ValaService {

    @POST("/unAuth/login")
    Call<UserLoginMessage> login(@Body UserLoginMessage user);

    @GET("/cdn/profilePic")
    Call<ResponseBody> getImageFile(@Query("userId") String userId);
}
