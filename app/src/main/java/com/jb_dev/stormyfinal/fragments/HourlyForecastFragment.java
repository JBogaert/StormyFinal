package com.jb_dev.stormyfinal.fragments;

/**
 * Created by Dad on 10/9/2016.
 */


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jb_dev.stormyfinal.R;
import com.jb_dev.stormyfinal.adapters.HourAdapter;
import com.jb_dev.stormyfinal.weather.Hour;

import java.util.Arrays;



public class HourlyForecastFragment extends Fragment {
//    public static final String EXTRA_URL ="url";

    private Hour[] mHours;


    public static HourlyForecastFragment newInstance(Parcelable[] parcelable) {
        HourlyForecastFragment mfirstFragment = new HourlyForecastFragment();
        Bundle args = new Bundle();
        args.putParcelableArray("parcelable", parcelable);
        mfirstFragment.setArguments(args);


        return mfirstFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hourly_forecast,
                container, false);


        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

//        Intent intent = getActivity().getIntent();
        Parcelable[] parcelables = getArguments().getParcelableArray("parcelable");
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter = new HourAdapter(getContext(), mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);






        return view;
    }


//    public void hideFragment () {
//        getFragmentManager().beginTransaction().replace(R.id.fragmentFrame, null).commit();
//
//
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String link = bundle.getString("url");
            setText(link);
        }
    }

    public void setText(String url) {
//        TextView view = (TextView) getView().findViewById(R.id.detailsText);
//        view.setText(url);
    }

}
