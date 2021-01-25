package com.example.hikers.service.sync;

import android.content.Context;
import androidx.annotation.NonNull;

import com.example.hikers.helpers.Const;
import com.example.hikers.service.api.ServiceGenerator;
import com.example.hikers.service.api.apiinterface.WeatherForecastCallback;
import com.example.hikers.service.api.jsonmodels.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherForecastSync extends ServiceGenerator {

    public WeatherForecastSync(Context context) {
        super(context);
    }

    public void getWeatherForeCast(final WeatherForecastCallback callback, String cityName) {

        getApiInstance().getWeatherForecast(cityName, Const.API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                callback.onFailed(0, t.getMessage());
            }
        });
    }
}
