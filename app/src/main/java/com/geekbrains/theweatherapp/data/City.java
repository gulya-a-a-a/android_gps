package com.geekbrains.theweatherapp.data;

import com.geekbrains.theweatherapp.model.CityEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class City implements Serializable {
    private final static int FORECAST_LENGTH_IN_DAYS = 7;

    @SerializedName("name")
    @Expose
    private String mCityName;

    private List<Weather> mWeathers;

    public City(String cityName) {
        mCityName = cityName;
        mWeathers = new ArrayList<>(FORECAST_LENGTH_IN_DAYS);
    }

    public City(CityEntity dbEntity) {
        mCityName = dbEntity.getCityName();
        mWeathers = new ArrayList<>(FORECAST_LENGTH_IN_DAYS);
    }

    public String getCityName() {
        return mCityName;
    }

    public List<Weather> getWeathers() {
        return mWeathers;
    }

    public void setWeathers(List<Weather> weathers) {
        mWeathers = weathers;
    }
}

