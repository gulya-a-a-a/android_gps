package com.geekbrains.theweatherapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.geekbrains.theweatherapp.dao.CityDao;
import com.geekbrains.theweatherapp.model.CityEntity;

@Database(entities = {CityEntity.class}, version = 2)
public abstract class CityDatabase extends RoomDatabase {
    public abstract CityDao getCityDao();
}
