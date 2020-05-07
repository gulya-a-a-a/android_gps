package com.geekbrains.theweatherapp.service;

import androidx.annotation.Nullable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    String CITY_ARG = "q";
    String APP_ID_ARG = "appid";
    String UNITS_ARG = "units";

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query(CITY_ARG) String cityName, @Query(APP_ID_ARG) String keyApi, @Nullable @Query(UNITS_ARG) String units);

    @GET("data/2.5/forecast")
    Call<ForecastResponse> loadForecast(@Query(CITY_ARG) String cityName, @Query(APP_ID_ARG) String keyApi, @Nullable @Query(UNITS_ARG) String units);
}
