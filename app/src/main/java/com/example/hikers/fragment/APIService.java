package com.example.hikers.fragment;

import com.example.hikers.notifications.MyResponse;
import com.example.hikers.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAEsIqWxU:APA91bFWfmRHitZgdamVsVs-jxk9l1fy36XtZ7yqkof4auiIrvrzRFV8dUPwNAZG_jJEjDlX3E8zDf1q89nvs4uA3rmXxUoFR6ClJ_1AEvxfGMZfPHcSn2fBal-m4fZyASajFNL-TX_e"
            }
    )

    @POST("fcm/send")
    Call<MyResponse>sendNotification(@Body Sender body);
}
