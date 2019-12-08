package com.integrador.petsapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.integrador.petsapp.BuildConfig;
import com.integrador.petsapp.models.ApiError;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceGenerator {
    private static final String TAG = ApiServiceGenerator.class.getSimpleName();

    private static Retrofit retrofitWithAuth;

    private static Picasso picasso;

    private ApiServiceGenerator() {
    }

    private static Retrofit retrofit;

    public static <s> s createService(Class<s> serviceClass){

        if (retrofit==null){ //patron cingleton = creacion de nu solo objeto.

            OkHttpClient.Builder httpClient=new OkHttpClient.Builder();

            httpClient.readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG){

                httpClient.addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY));

            }

            retrofit=new Retrofit
                    .Builder()
                    .baseUrl(ApiService.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

        }

        return retrofit.create(serviceClass);

    }

    public static ApiError parseError(retrofit2.Response<?> response) {
        try {
            Converter<ResponseBody, ApiError> converter = retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
            return converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError("Error en el servicio");
        }
    }


}
