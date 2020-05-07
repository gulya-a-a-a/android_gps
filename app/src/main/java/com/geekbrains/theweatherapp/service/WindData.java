package com.geekbrains.theweatherapp.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WindData implements Serializable {
    @SerializedName("speed")
    @Expose
    private float mSpeed;

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }
}
