package com.geekbrains.theweatherapp.service;

import com.geekbrains.theweatherapp.data.Weather;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherRequest {

    @SerializedName("main")
    @Expose
    private Weather mWeather;

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather weather) {
        mWeather = weather;
    }
}
