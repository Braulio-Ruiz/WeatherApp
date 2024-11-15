package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("current.json")
    Call<WeatherResponse> getCurrentWeather(@Query("q") String city, @Query("key") String apiKey);
}
