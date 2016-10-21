package com.jb_dev.stormyfinal.fragments;

/**
 * Created by Dad on 10/9/2016.
 */


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jb_dev.stormyfinal.R;
import com.jb_dev.stormyfinal.adapters.DayAdapter;
import com.jb_dev.stormyfinal.weather.Day;

import java.util.Arrays;


public class DailyForecastFragment extends Fragment {
//    public static final String EXTRA_URL ="url";

    private Day[] mDays;
    private Boolean mIsCold;


    public static DailyForecastFragment newInstance(Parcelable[] parcelable) {


        DailyForecastFragment mFragment = new DailyForecastFragment();
        Bundle args = new Bundle();
        args.putParcelableArray("parcelable", parcelable);

        mFragment.setArguments(args);


        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_daily_forecast,
                container, false);

            ListView mListView = (ListView) view.findViewById(R.id.list);






            Parcelable[] parcelables = getArguments().getParcelableArray("parcelable");

            mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

            DayAdapter adapter = new DayAdapter(getContext(), mDays);
            mListView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String link = bundle.getString("url");
//            setText(link);
        }


    public void setText(String url) {
//        TextView view = (TextView) getView().findViewById(R.id.detailsText);
//        view.setText(url);
    }
}
