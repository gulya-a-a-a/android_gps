package com.geekbrains.theweatherapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.geekbrains.theweatherapp.data.City;
import com.geekbrains.theweatherapp.data.Weather;

@Entity(indices = {@Index(value = {
        CityEntity.CITY_NAME
})
}, tableName = "cities")
public class CityEntity {
    private final static String ID = "id";
    final static String CITY_NAME = "name";
    final static String CITY_ENTITY_DATE = "date";
    final static String CITY_ENTITY_TEMP = "temp";
    private final static String CITY_WEATHER_CODE = "weather_code";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public long mId;

    @ColumnInfo(name = CITY_NAME)
    private String mCityName;

    @ColumnInfo(name = CITY_ENTITY_DATE)
    private long mEntityDate;

    @ColumnInfo(name = CITY_ENTITY_TEMP)
    private float mTemp;

    @ColumnInfo(name = CITY_WEATHER_CODE)
    private int mCode;

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public long getEntityDate() {
        return mEntityDate;
    }

    public void setEntityDate(long entityDate) {
        mEntityDate = entityDate;
    }

    public float getTemp() {
        return mTemp;
    }

    public void setTemp(float temp) {
        mTemp = temp;
    }

    static CityEntity cityToEntity(City city) {
        CityEntity entity = new CityEntity();
        entity.setCityName(city.getCityName());
        if (city.getWeathers() != null) {
            Weather w = city.getWeathers().get(0);
            entity.setTemp(w.getTemp());
            entity.setEntityDate(w.getTimestamp() * 1000);  //convert to milliseconds
            entity.setCode(w.getAdditionalWeatherData().getId());
        }
        return entity;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }
}
