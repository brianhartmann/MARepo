package com.mobileapps.moviefinder;

import androidx.fragment.app.Fragment;

public class FindMovieActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new FindMovieFragment();
    }
}