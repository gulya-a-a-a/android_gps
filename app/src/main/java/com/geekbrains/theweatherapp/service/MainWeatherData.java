package com.geekbrains.theweatherapp.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MainWeatherData implements Serializable {
    @SerializedName("temp")
    @Expose
    private float mTemp;
    @SerializedName("pressure")
    @Expose
    private int mPressure;
    @SerializedName("humidity")
    @Expose
    private int mHumidity;

    public float getTemp() {
        return mTemp;
    }

    public void setTemp(float temp) {
        mTemp = temp;
    }

    public int getPressure() {
        return mPressure;
    }

    public void setPressure(int pressure) {
        mPressure = pressure;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }
}
