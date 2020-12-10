package com.nsu.csd.data.remote;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nsu.csd.BuildConfig;

import java.util.Objects;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilsToken {

    private static Retrofit retrofit;
    private static Retrofit retrofitT;
    private static OkHttpClient client;
    private static Gson gson;
    private static Api api;

    public static OkHttpClient getBasicAuthClient(String token, boolean createNewInstance) {
        if (createNewInstance || client == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.authenticator((route, response) -> {
                return response.request().newBuilder().header("Authorization", token).build();
            });
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

            client = builder.build();
        }
        return client;
    }

    public static Retrofit getRetrofit(final String token) {
        if (gson == null) {
            gson = new Gson();
        }
        if(retrofit == null){
            retrofit = new Retrofit.Builder().
                    baseUrl(BuildConfig.SERVER_URL).//Базовая часть адреса
                    client(getBasicAuthClient(token,  false))
                     .addConverterFactory(GsonConverterFactory.create(gson)).//Конвертер, необходимый для преобразования JSON'а в объекты
                    build();
        }
        return retrofit;
    }

    public static Api getApiService(String token) {
        if (api == null) {
            api = getRetrofit(token).create(Api.class);//Создаем объект, при помощи которого будем выполнять запросы
        }
        return api;
    }
}
