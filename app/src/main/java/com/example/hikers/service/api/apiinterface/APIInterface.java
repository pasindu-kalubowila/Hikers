package com.example.hikers.service.api.apiinterface;

import com.example.hikers.service.api.jsonmodels.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/data/2.5/forecast")
    Call<WeatherResponse> getWeatherForecast(@Query("q") String cityName,
                                             @Query("appid") String apiKey);
}
