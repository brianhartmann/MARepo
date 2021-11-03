package com.mobileapps.moviefinder;

import androidx.fragment.app.Fragment;

/**
 * Activity for previously watched screen
 */

public class PreviouslyWatchedActivity extends SingleFragmentAppBarActivity {
    @Override
    protected Fragment createFragment() {
        return new PreviouslyWatchedFragment();
    }
}