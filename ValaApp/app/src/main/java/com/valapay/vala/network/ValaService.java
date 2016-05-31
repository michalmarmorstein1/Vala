package com.valapay.vala.network;

import com.valapay.vala.common.CashCollectRequestMessage;
import com.valapay.vala.common.UserLoginMessage;
import com.valapay.vala.common.UserQueryMessage;
import com.valapay.vala.common.UserRcvrListMessage;
import com.valapay.vala.common.UserSignupMessage;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ValaService {

    @POST("/unAuth/login")
    Call<UserLoginMessage> login(@Body UserLoginMessage user);

    @GET("/cdn/profilePic")
    Call<ResponseBody> getImageFile(@Query("userId") String userId);

    @POST("/unAuth/signup")
    Call<UserLoginMessage> signup(@Body UserSignupMessage user);

    @Multipart
    @POST("/cdn/profilePic")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    @POST("/user/query")
    Call<UserQueryMessage> searchUser(@Body UserQueryMessage query);

    @POST("/user/rcvrList")
    Call<UserRcvrListMessage> addRecipient(@Body UserRcvrListMessage recipient);

    @POST("/collect/collect")
    Call<CashCollectRequestMessage> collect(@Body CashCollectRequestMessage request);
}
