package com.geekbrains.theweatherapp.fragments;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.theweatherapp.data.City;
import com.geekbrains.theweatherapp.service.Parcel;
import com.geekbrains.theweatherapp.R;
import com.geekbrains.theweatherapp.data.Weather;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.geekbrains.theweatherapp.service.Parcel.PARCEL_TAG;

public class FragmentWeather extends Fragment implements Target {

    private TextView mCityNameTV, mTempValue, mWindSpeedTV, mPressureTV;
    private Button mInfoButton;
    private String mSearchUrl;
    private ImageView mBackgroundImage;

    private DecimalFormat mTempFormat;

    private RecyclerView mForecastRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_weather, container, false);

        findControls(layout);
        configServiceData();

        Parcel parcel = getParcel();
        if (parcel == null) {
            return layout;
        }

        City city = parcel.getCity();
        List<Weather> wths = city.getWeathers();

        mCityNameTV.setText(city.getCityName());
        if (wths.size() > 0) {
            setWeatherData(mTempValue, wths.get(0));

            mPressureTV.setText(String.format("%s hPa", String.valueOf(wths.get(0).getPressure())));
            mWindSpeedTV.setText(String.format("%s mps", String.valueOf(wths.get(0).getWindSpeed())));

            configRecycler();
            updateForecast(wths);
            changeBackground(wths.get(0));
        }

        return layout;
    }

    private void configRecycler() {
        int orientation = LinearLayoutManager.HORIZONTAL;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = LinearLayoutManager.VERTICAL;
        }
        mForecastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                orientation, false));
    }

    private void changeBackground(Weather weather) {
        String weatherBackgroundPath;
        switch (weather.getDrawableID()) {
            case R.drawable.sunny:
                weatherBackgroundPath = "https://unsplash.com/photos/5Iur1y9FtNs/download?force=true&w=1920";
                break;
            case R.drawable.cloudy:
                weatherBackgroundPath = "https://unsplash.com/photos/3-EiAnsIXps/download?force=true";
                break;
            case R.drawable.rain:
                weatherBackgroundPath = "https://unsplash.com/photos/LClnj58ZP9g/download?force=true&w=1920";
                break;
            case R.drawable.snow:
                weatherBackgroundPath = "https://unsplash.com/photos/PKAW8MQYlU8/download?force=true&w=1920";
                break;
            case R.drawable.storm:
                weatherBackgroundPath = "https://unsplash.com/photos/ZZkeAKWFjzY/download?force=true&w=1920";
                break;
            default:
                weatherBackgroundPath = "https://unsplash.com/photos/B6hw9ooyldM/download?force=true&w=1920";
                break;
        }
        Picasso.get().load(weatherBackgroundPath).into(this);
    }

    private Parcel getParcel() {
        Parcel parcel;
        try {
            parcel = (Parcel) requireArguments().getSerializable(PARCEL_TAG);
        } catch (NullPointerException ex) {
            return null;
        }
        return parcel;
    }

    private void setWeatherData(final TextView tempTV, final Weather w) {
        tempTV.setText(String.format("%s℃", mTempFormat.format(w.getTemp())));
        tempTV.setCompoundDrawablesWithIntrinsicBounds(0,
                w.getDrawableID(), 0, 0);
    }

    static FragmentWeather create(@NonNull Parcel parcel) {
        FragmentWeather f = new FragmentWeather();
        Bundle b = new Bundle();
        b.putSerializable(PARCEL_TAG, parcel);
        f.setArguments(b);
        return f;
    }

    private void configServiceData() {
        mSearchUrl = getString(R.string.wiki_search_url);
        mTempFormat = new DecimalFormat(getResources().getString(R.string.temp_format));
    }

    private void findControls(View layout) {
        mCityNameTV = layout.findViewById(R.id.city_name);
        mInfoButton = layout.findViewById(R.id.info_button);
        mTempValue = layout.findViewById(R.id.temp_value);
        mForecastRecyclerView = layout.findViewById(R.id.forecast_list);
        mWindSpeedTV = layout.findViewById(R.id.wind_speed_tv);
        mPressureTV = layout.findViewById(R.id.pressure_tv);
        mBackgroundImage = layout.findViewById(R.id.background_image);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        mBackgroundImage.setImageBitmap(bitmap);
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }

    private class ForecastHolder extends RecyclerView.ViewHolder {

        private TextView mTempTV, mDayTV;
        private ImageView mImageView;

        ForecastHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_forecast, parent, false));

            mTempTV = itemView.findViewById(R.id.forecast_temp);
            mDayTV = itemView.findViewById(R.id.forecast_day);
            mImageView = itemView.findViewById(R.id.forecast_image);
        }

        void bind(List<Weather> weathers, final int position) {
            Weather w = weathers.get(position);
            mTempTV.setText(String.format("%s℃", mTempFormat.format(w.getTemp())));
            mDayTV.setText(getWeekDay(position));
            mImageView.setImageResource(w.getDrawableID());
        }

        private String getWeekDay(final int position) {
            String weekDay;
            switch (position) {
                case 0:
                    weekDay = getResources().getString(R.string.today);
                    break;
                case 1:
                    weekDay = getResources().getString(R.string.tomorrow);
                    break;
                default:
                    Date dt = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dt);
                    cal.add(Calendar.DATE, position);
                    SimpleDateFormat df = new SimpleDateFormat("EEE", Locale.getDefault());
                    weekDay = df.format(cal.getTime());
                    break;
            }
            return weekDay;
        }
    }

    private class ForecastAdapter extends RecyclerView.Adapter<ForecastHolder> {

        private List<Weather> mWeathers;

        ForecastAdapter(List<Weather> weathers) {
            mWeathers = weathers;
        }

        @NonNull
        @Override
        public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ForecastHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ForecastHolder holder, int position) {
            holder.bind(mWeathers, position);
        }

        @Override
        public int getItemCount() {
            return mWeathers.size();
        }
    }

    private void updateForecast(List<Weather> weathers) {
        mForecastRecyclerView.setAdapter(new ForecastAdapter(weathers));
    }
}
