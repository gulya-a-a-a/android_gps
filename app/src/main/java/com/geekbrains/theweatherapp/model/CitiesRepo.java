package com.geekbrains.theweatherapp.model;

import android.os.AsyncTask;

import androidx.core.util.Pair;

import com.geekbrains.theweatherapp.dao.CityDao;
import com.geekbrains.theweatherapp.data.City;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CitiesRepo {

    public enum SortOrder {
        asc, desc
    }

    private final CityDao mCitiesDao;
    private InsertTask mInsertTask;
    private SelectAllTask mSelectAllTask;
    private CitiesRepoListener mCitiesRepoListener;

    private List<CityEntity> mEntities;

    public CitiesRepo(CityDao dao) {
        mCitiesDao = dao;
        mEntities = new ArrayList<>();
        loadEntities();
    }

    private void loadEntities() {
        SelectAllTask sat = new SelectAllTask(mCitiesDao);
        sat.delegate = this;
        sat.execute();
    }

    public List<CityEntity> getCities() {
        if (mEntities == null)
            loadEntities();
        return mEntities;
    }

    public long getCountOfCities() {
        return mEntities.size();
    }

    public void addCity(CityEntity city) {
        asyncInsertion(city);
    }

    public void addCity(City city) {
        asyncInsertion(CityEntity.cityToEntity(city));
    }

    private void asyncInsertion(CityEntity newEntity) {
        InsertTask it = new InsertTask(mCitiesDao);
        it.delegate = this;
        it.execute(newEntity);
    }

    private void asyncUpdate(CityEntity newEntity) {
        UpdateTask ut = new UpdateTask(mCitiesDao);
        ut.delegate = this;
        ut.execute(newEntity);
    }

    public void updateCity(CityEntity city) {
        asyncUpdate(city);
    }

    public void updateCity(City city) {
        asyncUpdate(CityEntity.cityToEntity(city));
    }

    public void removeCity(long id) {
        mCitiesDao.deleteCityById(id);
        loadEntities();
    }

    public CityEntity getCity(long id) {
        return mCitiesDao.getCityById(id);
    }

    public CityEntity getCity(String name) {
        SelectByNameTask st = new SelectByNameTask(mCitiesDao);
        st.execute(name);
        CityEntity cityEntity = null;
        try {
            List<CityEntity> res = st.get();
            if (res.size() != 0) {
                cityEntity = res.get(0);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return cityEntity;
    }

    public void sortCities(SortOrder order) {
        SortTask st = new SortTask(mCitiesDao);
        st.delegate = this;
        st.execute(order);
    }

    private void asyncFinished(List<CityEntity> cities) {
        mEntities = cities;
        mCitiesRepoListener.onCitiesListChanged(mEntities);
    }

    public void setCitiesRepoListener(CitiesRepoListener citiesRepoListener) {
        mCitiesRepoListener = citiesRepoListener;
        mCitiesRepoListener.onCitiesListChanged(mEntities);
    }


    private static class SelectAllTask extends AsyncTask<Void, Void, List<CityEntity>> {

        private CitiesRepo delegate = null;

        CityDao mCityDao;

        SelectAllTask(CityDao dao) {
            mCityDao = dao;
        }

        @Override
        protected List<CityEntity> doInBackground(Void... voids) {
            return mCityDao.getAllCities();
        }

        @Override
        protected void onPostExecute(List<CityEntity> cityEntities) {
            delegate.asyncFinished(cityEntities);
        }
    }

    private static class InsertTask extends AsyncTask<CityEntity, Void, Void> {

        CityDao mCityDao;
        private CitiesRepo delegate = null;

        InsertTask(CityDao dao) {
            mCityDao = dao;
        }

        @Override
        protected Void doInBackground(CityEntity... cityEntities) {
            mCityDao.insertCity(cityEntities[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            delegate.loadEntities();
        }
    }

    private static class UpdateTask extends AsyncTask<CityEntity, Void, Void> {

        CityDao mCityDao;
        private CitiesRepo delegate = null;

        UpdateTask(CityDao dao) {
            mCityDao = dao;
        }

        @Override
        protected Void doInBackground(CityEntity... cityEntities) {
            List<CityEntity> res = mCityDao.getCityByName(cityEntities[0].getCityName());
            cityEntities[0].mId = res.get(0).mId;
            mCityDao.updateCity(cityEntities[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            delegate.loadEntities();
        }
    }

    private static class SelectByNameTask extends AsyncTask<String, Void, List<CityEntity>> {

        CityDao mCityDao;
        private CitiesRepo delegate = null;

        SelectByNameTask(CityDao dao) {
            mCityDao = dao;
        }

        @Override
        protected List<CityEntity> doInBackground(String... strings) {
            return mCityDao.getCityByName(strings[0]);
        }
    }

    private static class SortTask extends AsyncTask<SortOrder, Void, List<CityEntity>> {

        CityDao mCityDao;
        private CitiesRepo delegate = null;

        @Override
        protected List<CityEntity> doInBackground(SortOrder... sortOrders) {
            if (sortOrders[0] == SortOrder.asc) {
                return mCityDao.getAscSortedCities();
            } else {
                return mCityDao.getDescSortedCities();
            }
        }

        SortTask(CityDao dao) {
            mCityDao = dao;
        }

        @Override
        protected void onPostExecute(List<CityEntity> cityEntities) {
            super.onPostExecute(cityEntities);
            delegate.asyncFinished(cityEntities);
        }
    }

}
