package com.mobileapps.moviefinder;

import androidx.fragment.app.Fragment;

/**
 * Activity for about screen
 */

public class AboutActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AboutFragment();
    }
}