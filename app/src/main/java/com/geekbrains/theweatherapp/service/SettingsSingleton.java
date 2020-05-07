package com.geekbrains.theweatherapp.service;

import java.io.Serializable;

public class SettingsSingleton implements Serializable {

    private boolean mShowWindSpeed, mShowPressure;

    private String mSelectedCity;

    private static SettingsSingleton sInstance;

    private SettingsSingleton() {
        mShowPressure = mShowWindSpeed = true;
    }

    public static SettingsSingleton getInstance() {
        if (sInstance == null) {
            sInstance = new SettingsSingleton();
        }
        return sInstance;
    }

    public void setShowWindSpeed(boolean showWindSpeed) {
        mShowWindSpeed = showWindSpeed;
    }

    public void setShowPressure(boolean showPressure) {
        mShowPressure = showPressure;
    }

    public boolean getShowWindSpeed() {
        return mShowWindSpeed;
    }

    public boolean getShowPressure() {
        return mShowPressure;
    }

    public String getSelectedCity() {
        return mSelectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        mSelectedCity = selectedCity;
    }
}
