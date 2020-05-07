package com.geekbrains.theweatherapp.model;

import java.util.List;

public interface CitiesRepoListener {
    public void onCitiesListChanged(List<CityEntity> cities);
}
