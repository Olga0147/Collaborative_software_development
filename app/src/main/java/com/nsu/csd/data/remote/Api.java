package com.nsu.csd.data.remote;

import com.nsu.csd.model.UserLoginDTO;
import com.nsu.csd.model.UserRegistrationDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * аннотация - на какой запрос отвечаем @GET("/api/get")
 */
public interface Api {

    @POST("sign_up")
    Call<Void> sign_up(@Body UserRegistrationDTO userRegistrationDTO);

    @POST("sign_in")
    Call<Void> sign_in(@Body UserLoginDTO userLoginDTO);

    @POST("logout")
    Call<Void> logout(@Body Object o);


}
