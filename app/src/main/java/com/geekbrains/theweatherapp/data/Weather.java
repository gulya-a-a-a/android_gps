package com.geekbrains.theweatherapp.data;

import com.geekbrains.theweatherapp.R;
import com.geekbrains.theweatherapp.service.AdditionalWeatherData;
import com.geekbrains.theweatherapp.service.MainWeatherData;
import com.geekbrains.theweatherapp.service.WindData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Weather implements Serializable {
    @SerializedName("dt")
    @Expose
    private long mTimestamp;

    @SerializedName("main")
    @Expose
    private MainWeatherData mMainWeatherData;

    @SerializedName("weather")
    @Expose
    private AdditionalWeatherData[] mAdditionalWeatherData;

    @SerializedName("wind")
    @Expose
    private WindData mWindData;

    private int mDrawableID;

    public Weather() {
    }

    public float getTemp() {
        return mMainWeatherData.getTemp();
    }

    public int getPressure() {
        return mMainWeatherData.getPressure();
    }

    public void setPressure(int pressure) {
        mMainWeatherData.setPressure(pressure);
    }

    public float getWindSpeed() {
        return mWindData.getSpeed();
    }

    public void setWindSpeed(float windSpeed) {
        mWindData.setSpeed(windSpeed);
    }

    public int getDrawableID() {
        return mDrawableID;
    }

    public static int getDrawableFromCode(int weatherCode) {
        int drawableID = R.drawable.sunny;
        if (weatherCode < 300) {
            drawableID = R.drawable.storm;
        } else if (weatherCode < 600) {
            drawableID = R.drawable.rain;
        } else if (weatherCode < 700) {
            drawableID = R.drawable.snow;
        } else if (weatherCode == 800) {
            drawableID = R.drawable.sunny;
        } else {
            drawableID = R.drawable.cloudy;
        }
        return drawableID;
    }

    public void setDrawableID(int weatherCode) {
        mDrawableID = getDrawableFromCode(weatherCode);
    }

    public void setTemp(float temp) {
        mMainWeatherData.setTemp(temp);
    }

    public MainWeatherData getMainWeatherData() {
        return mMainWeatherData;
    }

    public void setMainWeatherData(MainWeatherData mainWeatherData) {
        mMainWeatherData = mainWeatherData;
    }

    public WindData getWindData() {
        return mWindData;
    }

    public void setWindData(WindData windData) {
        mWindData = windData;
    }

    public AdditionalWeatherData getAdditionalWeatherData() {
        return mAdditionalWeatherData[0];
    }

    public void setAdditionalWeatherData(AdditionalWeatherData[] additionalWeatherData) {
        mAdditionalWeatherData = additionalWeatherData;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }
}
