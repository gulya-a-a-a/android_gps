package com.geekbrains.theweatherapp.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdditionalWeatherData implements Serializable {

    @SerializedName("id")
    @Expose
    private int mId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
