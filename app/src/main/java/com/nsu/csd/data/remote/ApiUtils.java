package com.nsu.csd.data.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nsu.csd.BuildConfig;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiUtils {

    private static Retrofit retrofit;
    private static OkHttpClient client;
    private static Gson gson;
    private static Api api;

    public static OkHttpClient getBasicAuthClient(final String email, final String password, boolean createNewInstance) {
        if (createNewInstance || client == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.authenticator((route, response) -> {
                String credential = Credentials.basic(email, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            });
            if (!BuildConfig.BUILD_TYPE.contains("release")) {
                builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            }

            client = builder.build();
        }
        return client;
    }

    public static Retrofit getRetrofit() {
        if (gson == null) {
            gson = new Gson();
        }
        if(retrofit == null){
            retrofit = new Retrofit.Builder().
                    baseUrl(BuildConfig.SERVER_URL).//Базовая часть адреса
                    client(getBasicAuthClient("", "", false))
                     .addConverterFactory(GsonConverterFactory.create(gson)).//Конвертер, необходимый для преобразования JSON'а в объекты
                    build();
        }
        return retrofit;
    }

    public static Api getApiService() {
        if (api == null) {
            api = getRetrofit().create(Api.class);//Создаем объект, при помощи которого будем выполнять запросы
        }
        return api;
    }
}
