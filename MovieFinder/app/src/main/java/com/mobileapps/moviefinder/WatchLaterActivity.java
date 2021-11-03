package com.mobileapps.moviefinder;

import androidx.fragment.app.Fragment;

/**
 * Activity for watch later screen
 */

public class WatchLaterActivity extends SingleFragmentAppBarActivity {
    @Override
    protected Fragment createFragment() {
        return new WatchLaterFragment();
    }
}