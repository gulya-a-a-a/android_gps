package com.geekbrains.theweatherapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.geekbrains.theweatherapp.model.CityEntity;

import java.util.List;

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCity(CityEntity city);

    @Update
    void updateCity(CityEntity city);

    @Delete
    void deleteCity(CityEntity city);

    @Query("DELETE FROM cities WHERE id = :id")
    void deleteCityById(long id);

    @Query("SELECT * FROM cities WHERE id = :id")
    CityEntity getCityById(long id);

    @Query("SELECT * FROM cities WHERE name = :name")
    List<CityEntity> getCityByName(String name);

    @Query("SELECT * FROM cities")
    List<CityEntity> getAllCities();

    @Query("SELECT COUNT() FROM cities")
    long getCountCities();

    @Query("SELECT * FROM cities ORDER BY name ASC")
    List<CityEntity> getAscSortedCities();

    @Query("SELECT * FROM cities ORDER BY name DESC")
    List<CityEntity> getDescSortedCities();
}
