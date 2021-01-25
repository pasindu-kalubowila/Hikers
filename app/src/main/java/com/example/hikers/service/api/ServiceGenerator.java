package com.example.hikers.service.api;


import android.content.Context;

import com.example.hikers.service.api.apiinterface.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    // This is the service generator class, which handle each api call
    // and match it's json to response models

    private Context mContext;

    public ServiceGenerator(Context context) {
        mContext = context;
    }

    public <T> T CreateService(Class<T> serviceClass) {

        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .setPrettyPrinting()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(serviceClass);
    }

    public APIInterface getApiInstance() {
        return new ServiceGenerator(mContext).CreateService(APIInterface.class);
    }
}
