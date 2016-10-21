package com.jb_dev.stormyfinal;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jb_dev.stormyfinal.fragments.MainActivityFragment;

public class MainActivity extends AppCompatActivity  {
    public static final String MAIN_FRAGMENT = "main_fragment";
    private static final String VIEW_PAGER_FRAGMENT = "viewpager_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(savedInstanceState == null) {

            MainActivityFragment fragment = new MainActivityFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.placeholder, fragment, MAIN_FRAGMENT);
            ft.commit();
        }


        }
    }




