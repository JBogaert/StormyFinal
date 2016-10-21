package com.jb_dev.stormyfinal.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jb_dev.stormyfinal.MainActivity;
import com.jb_dev.stormyfinal.R;
import com.jb_dev.stormyfinal.weather.Current;
import com.jb_dev.stormyfinal.weather.Day;
import com.jb_dev.stormyfinal.weather.Forecast;
import com.jb_dev.stormyfinal.weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.jb_dev.stormyfinal.R.drawable.bg_gradient;
import static com.jb_dev.stormyfinal.R.drawable.bg_gradient_cold;


/**
 * Created by Dad on 10/20/2016.
 */

public class MainActivityFragment extends Fragment {

    private static final String
            VIEW_PAGER_FRAGMENT = "viewpager_fragment";
    private static final String
            DUAL_PANE_FRAGMENT = "dual_pane_fragment";


    public static final String TAG = MainActivity.class.getSimpleName();
    Forecast mForecast;
    TextView mTimeLabel;
    TextView mTemperatureLabel;
    TextView mHumidityValue;
    TextView mPrecipValue;
    TextView mSummaryLabel;
    Button mButton;
    ImageView mIconImageView;
    ImageView mRefreshImageView;
    ProgressBar mProgressBar;
    Boolean mIsCold;
    Boolean mIsTablet;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
}


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsTablet = getResources().getBoolean(R.bool.is_tablet);
        mIsCold = false;
        mButton = (Button) getActivity().findViewById(R.id.hourlyButton);
        mTimeLabel = (TextView) getActivity().findViewById(R.id.timeLabel);
        mTemperatureLabel = (TextView) getActivity().findViewById(R.id.temperatureLabel);
        mHumidityValue = (TextView) getActivity().findViewById(R.id.humidityValue);
        mPrecipValue = (TextView) getActivity().findViewById(R.id.precipValue);
        mSummaryLabel = (TextView) getActivity().findViewById(R.id.summaryLabel);
        mIconImageView = (ImageView) getActivity().findViewById(R.id.iconImageView);
        mRefreshImageView = (ImageView) getActivity().findViewById(R.id.refreshImageView);
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mIsTablet) {
                    DualPaneFragment dualPaneFragment = new DualPaneFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArray("daily", mForecast.getDailyForecast());
                    bundle.putParcelableArray("hourly", mForecast.getHourlyForecast());
                    bundle.putBoolean("isCold", mIsCold);
                    dualPaneFragment.setArguments(bundle);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.placeholder, dualPaneFragment, DUAL_PANE_FRAGMENT);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                ViewPagerFragment fragment = new ViewPagerFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArray("daily", mForecast.getDailyForecast());
                bundle.putParcelableArray("hourly", mForecast.getHourlyForecast());
                bundle.putBoolean("isCold", mIsCold);
                fragment.setArguments(bundle);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.placeholder, fragment, VIEW_PAGER_FRAGMENT);
                ft.addToBackStack(null);
                ft.commit();
            }}
        });

        final double latitude = 37.8267;
        final double longitude = -122.423;


        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });

        getForecast(latitude, longitude);



        Log.d(TAG, "Main UI code is running!");
    }



    private void getForecast(double latitude, double longitude) {
        String apiKey = "29b1991ec49012104fc7ea3c66d8f260";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "NETWORK UNAVAILABLE",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }


    private void updateDisplay() {
        Current current = mForecast.getCurrent();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (current.getTemperature() < 72) {
                mIsCold = true;
                Drawable background = getActivity().getResources().getDrawable(bg_gradient_cold);
                this.getView().setBackground(background);
            } else {
                Drawable background = getActivity().getResources().getDrawable(bg_gradient);
                this.getView().setBackground(background);
            }
        }



        mTemperatureLabel.setText(current.getTemperature() + "");

        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());

        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));

        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);

            days[i] = day;
        }

        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);

            hours[i] = hour;
        }

        return hours;
    }


    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        Toast.makeText(getActivity(), "WARNING", Toast.LENGTH_LONG).show();
    }



//    @OnClick(R.id.dailyButton)
//    public void startDailyActivity(View view) {
//        if (dailyForecastFragment == null) {
//            dailyForecastFragment = DailyForecastFragment.newInstance(mForecast.getDailyForecast());
//        }
//
//        ft = getSupportFragmentManager().beginTransaction();
//        if (dailyForecastFragment.isAdded()) { // if the fragment is already in container
//            ft.remove(dailyForecastFragment);
//
//
//        } else { // fragment needs to be added to frame container
//
//            ft.add(R.id.fragmentFrame, dailyForecastFragment);
//        }
//        if (hourlyForecastFragment != null) {
//            ft.remove(hourlyForecastFragment);
//        }
//        // Hide fragment B
////        if (fragmentB.isAdded()) { ft.hide(fragmentB); }
//        // Hide fragment C
////        if (fragmentC.isAdded()) { ft.hide(fragmentC); }
//        // Commit changes
//        ft.commit();
//
//
//// Replace the contents of the container with the new fragment
////        ft.replace(R.id.fragmentFrame, HourlyForecastFragment.newInstance(mForecast.getHourlyForecast()));
//// or ft.add(R.id.your_placeholder, new FooFragment());
//// Complete the changes added above
////        ft.commit();
////
////        startActivity(intent);
//
//    }
//
//
//    @OnClick(R.id.hourlyButton)
//    public void startHourlyActivity(View view) {
//        if (hourlyForecastFragment == null) {
//            hourlyForecastFragment = HourlyForecastFragment.newInstance(mForecast.getHourlyForecast());
//        }// Begin the transaction
//        ft = getSupportFragmentManager().beginTransaction();
//        if (hourlyForecastFragment.isAdded()) { // if the fragment is already in container
//            ft.remove(hourlyForecastFragment);
//        } else {
//            ft.add(R.id.fragmentFrame, hourlyForecastFragment);
//        }
//        if (dailyForecastFragment != null) {
//            ft.remove(dailyForecastFragment);
//        }
////
//
////
////        }
//        { // fragment needs to be added to frame container
//
//        }
//        // Hide fragment B
////        if (fragmentB.isAdded()) { ft.hide(fragmentB); }
//        // Hide fragment C
////        if (fragmentC.isAdded()) { ft.hide(fragmentC); }
//        // Commit changes
//        ft.commit();
//
//
//// Replace the contents of the container with the new fragment
////        ft.replace(R.id.fragmentFrame, HourlyForecastFragment.newInstance(mForecast.getHourlyForecast()));
//// or ft.add(R.id.your_placeholder, new FooFragment());
//// Complete the changes added above
////        ft.commit();
////
////        startActivity(intent);
//
//    }
//
////    @Override
////    public void onConfigurationChanged(Configuration newConfig) {
////        super.onConfigurationChanged(newConfig);
////
////        // Checks the orientation of the screen
////        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
////            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
////            setContentView(R.layout.activity_main_land);
////        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
////            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
////            setContentView(R.layout.activity_main);
////        }
////    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void onForecastSelected(View v) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.placeholder, fragment, VIEW_PAGER_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

}



