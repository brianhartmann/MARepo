package com.mobileapps.moviefinder;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.fragment.app.Fragment;

public class FindMovieActivity extends SingleFragmentAppBarActivity {
        @Override
    protected Fragment createFragment() {
        return new FindMovieFragment();
    }
}