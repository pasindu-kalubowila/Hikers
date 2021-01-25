package com.example.hikers.service.api.apiinterface;

import com.example.hikers.service.api.jsonmodels.WeatherResponse;

public interface WeatherForecastCallback {

    void onSuccess(WeatherResponse weatherResponse);

    void onFailed(int status, String reason);
}
