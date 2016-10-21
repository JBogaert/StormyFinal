package com.jb_dev.stormyfinal.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jb_dev.stormyfinal.R;

/**
 * Created by Dad on 10/20/2016.
 */

public class ViewPagerFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);

        Parcelable[] k = getArguments().getParcelableArray("daily");
        Parcelable[] j = getArguments().getParcelableArray("hourly");
        Boolean isCold = getArguments().getBoolean("isCold");

        view.setBackground(getResources().getDrawable(R.drawable.bg_gradient));
        if (isCold) view.setBackground(getResources().getDrawable(R.drawable.bg_gradient_cold));

        final DailyForecastFragment dailyForecastFragment = DailyForecastFragment.newInstance(k);
        final HourlyForecastFragment hourlyForecastFragment = HourlyForecastFragment.newInstance(j);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return position == 0 ? dailyForecastFragment : hourlyForecastFragment;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return position == 0 ? "Daily" : "Hourly";
            }

            @Override
            public int getCount() {
                return 2;
            }
        });


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(isCold) {
                    tabLayout.setBackground(getActivity().getDrawable(R.drawable.bg_gradient_cold));
                }
             else {
                    tabLayout.setBackground(getActivity().getDrawable(R.drawable.bg_gradient));
                }}

        return view;

    }
}
