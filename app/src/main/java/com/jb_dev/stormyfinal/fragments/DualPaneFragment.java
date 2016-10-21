package com.jb_dev.stormyfinal.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jb_dev.stormyfinal.R;

public class DualPaneFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dualpane, container, false);
        Parcelable[] k = getArguments().getParcelableArray("daily");
        Parcelable[] j = getArguments().getParcelableArray("hourly");
        Boolean isCold = getArguments().getBoolean("isCold");

        view.setBackground(getResources().getDrawable(R.drawable.bg_gradient));

        if (isCold) {
            view.setBackground(getResources().getDrawable(R.drawable.bg_gradient_cold));
        }

        FragmentManager fragmentManager = getChildFragmentManager();
        
        DailyForecastFragment savedDailyForecastFragment = (DailyForecastFragment) fragmentManager
                .findFragmentByTag("daily_forecast_fragment");
        if (savedDailyForecastFragment == null) {
            DailyForecastFragment dailyForecastFragment = DailyForecastFragment.newInstance(k);
            fragmentManager.beginTransaction()
                    .add(R.id.leftPlaceholder, dailyForecastFragment, "daily_forecast_fragment")
                    .commit();
        }

        HourlyForecastFragment savedHourlyForecastFragment = (HourlyForecastFragment) fragmentManager
                .findFragmentByTag("hourly_forecast_fragment");
        if (savedHourlyForecastFragment == null) {
            HourlyForecastFragment hourlyForecastFragment = HourlyForecastFragment.newInstance(j);
            fragmentManager.beginTransaction()
                    .add(R.id.rightPlaceholder, hourlyForecastFragment, "hourly_forecast_fragment")
                    .commit();
        }



        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setTitle(getResources().getString(R.string.app_name));
    }
}
