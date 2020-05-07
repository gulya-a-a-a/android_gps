package com.geekbrains.theweatherapp.service;

import com.geekbrains.theweatherapp.data.City;
import com.geekbrains.theweatherapp.data.Weather;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {

    @SerializedName("list")
    @Expose
    private List<Weather> mWeathers;

    @SerializedName("city")
    @Expose
    private City mCity;

    public City getCity() {
        return mCity;
    }

    public void setCity(City city) {
        mCity = city;
    }

    public List<Weather> getWeathers() {
        return mWeathers;
    }

    public void setWeathers(List<Weather> weathers) {
        mWeathers = weathers;
    }
}
