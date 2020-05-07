package com.geekbrains.theweatherapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.theweatherapp.activities.App;
import com.geekbrains.theweatherapp.data.*;
import com.geekbrains.theweatherapp.model.CitiesRepo;
import com.geekbrains.theweatherapp.model.CitiesRepoListener;
import com.geekbrains.theweatherapp.model.CityEntity;
import com.geekbrains.theweatherapp.service.ForecastResponse;
import com.geekbrains.theweatherapp.service.OpenWeather;
import com.geekbrains.theweatherapp.service.Parcel;
import com.geekbrains.theweatherapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.geekbrains.theweatherapp.service.Parcel.PARCEL_TAG;

public class FragmentList extends Fragment {
    private static boolean isFirstOpen = true;
    private Parcel mCurrentCityData;

    private TextInputEditText mCityNameTV;

    private OpenWeather mOpenWeather;
    private CitiesRepo mCitiesRepo;
    private RecyclerView mRecyclerView;
    private DecimalFormat mTempFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mCitiesRepo = new CitiesRepo(App.getInstance().getCityDao());

        configControls(view);
        initRetrofit();
        mRecyclerView.setAdapter(new CityListAdapter(mCitiesRepo));
        mTempFormat = new DecimalFormat(getResources().getString(R.string.temp_format));

        if (isFirstOpen) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            mCurrentCityData = new Parcel(0, new City(sp.getString(PARCEL_TAG, "")));
            isFirstOpen = false;
            getWeather(mCurrentCityData.getCity().getCityName());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PARCEL_TAG, mCurrentCityData);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        searchItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_list:
                return true;
            case R.id.sort_list_asc:
                mCitiesRepo.sortCities(CitiesRepo.SortOrder.asc);
                return true;
            case R.id.sort_list_desc:
                mCitiesRepo.sortCities(CitiesRepo.SortOrder.desc);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void configControls(View view) {
        mCityNameTV = view.findViewById(R.id.city_name_TI);

        mCityNameTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String newCityName = Objects.requireNonNull(mCityNameTV.getText()).toString();
                    if (!newCityName.isEmpty()) {
                        requestSnack(v, newCityName).show();
                    }
                }
                return false;
            }
        });

        mRecyclerView = view.findViewById(R.id.city_list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
    }

    private void initRetrofit() {
        String BASE_API_URL = "https://api.openweathermap.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mOpenWeather = retrofit.create(OpenWeather.class);
    }


    private void getWeather(final String cityName) {
        String API_KEY = "f912bb6609c3957b0ed1ba6ffcc4c5d6";
        mOpenWeather.loadForecast(cityName, API_KEY, "metric").enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.body() != null) {
                    forecastResponseHandle(response.body());
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.d("Request Error", "request failure");
            }
        });
    }

    private void forecastResponseHandle(ForecastResponse response) {
        try {
            City cityFromResponse = response.getCity();
            cityFromResponse.setWeathers(parseTheForecast(response));
            CityEntity newCityEntity = mCitiesRepo.getCity(cityFromResponse.getCityName());

            if (newCityEntity == null) {
                mCitiesRepo.addCity(cityFromResponse);
            } else {
                mCitiesRepo.updateCity(cityFromResponse);
            }

            mCurrentCityData = new Parcel((int) mCitiesRepo.getCountOfCities() - 1, cityFromResponse);
            saveLastSelection(cityFromResponse.getCityName());
            showTheWeather(mCurrentCityData);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private List<Weather> parseTheForecast(ForecastResponse response) {
        List<Weather> weathers;
        if ((weathers = response.getWeathers()) == null) {
            return null;
        }
        List<Weather> resWeathers = new LinkedList<>();
        for (int i = 0; i < weathers.size(); i += 8) {
            Weather w = weathers.get(i);
            w.setDrawableID(w.getAdditionalWeatherData().getId());
            resWeathers.add(w);
        }
        return resWeathers;
    }

    private void saveLastSelection(String cityName) {
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit()
                .putString(PARCEL_TAG, cityName)
                .apply();
    }

    private void showTheWeather(Parcel parcel) {
        FragmentWeather weather = FragmentWeather.create(parcel);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, weather)
                .addToBackStack("Weather")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private Snackbar requestSnack(View v, final String cityName) {
        return Snackbar.make(v, cityName, Snackbar.LENGTH_LONG).
                setAction(R.string.go, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWeather(cityName);
                    }
                });
    }


    private class CityListHolder extends RecyclerView.ViewHolder {
        private TextView mCityNameTV, mDateTV, mCityTempTV;
        private SimpleDateFormat mSimpleDateFormat;
        private ImageView mImageView;

        CityListHolder(LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.list_city_item, parent, false));

            itemView.findViewById(R.id.city_list_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestSnack(itemView, mCityNameTV.getText().toString()).show();
                }
            });

            mCityNameTV = itemView.findViewById(R.id.city_list_name);
            mDateTV = itemView.findViewById(R.id.city_list_date);
            mCityTempTV = itemView.findViewById(R.id.city_list_temp);
            mImageView = itemView.findViewById(R.id.city_list_img);

            mSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        }

        void bind(CityEntity city) {
            mCityNameTV.setText(city.getCityName());
            mDateTV.setText(String.valueOf(
                    mSimpleDateFormat.format(city.getEntityDate())
            ));
            mCityTempTV.setText(String.format("%s℃", mTempFormat.format(city.getTemp())));
            mImageView.setImageResource(Weather.getDrawableFromCode(city.getCode()));
        }
    }

    private class CityListAdapter extends RecyclerView.Adapter<CityListHolder> implements CitiesRepoListener {

        private List<CityEntity> mCities;

        CityListAdapter(CitiesRepo repo) {
            repo.setCitiesRepoListener(this);
        }

        @NonNull
        @Override
        public CityListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CityListHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CityListHolder holder, int position) {
            holder.bind(mCities.get(position));
            holder.itemView.setSelected(true);
        }

        @Override
        public int getItemCount() {
            return mCities.size();
        }

        public void setCities(List<CityEntity> cities) {
            mCities = cities;
            notifyDataSetChanged();
        }

        @Override
        public void onCitiesListChanged(List<CityEntity> cities) {
            setCities(cities);
        }
    }

}
