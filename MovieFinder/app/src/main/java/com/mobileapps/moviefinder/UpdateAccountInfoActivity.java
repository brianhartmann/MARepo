package com.mobileapps.moviefinder;

import androidx.fragment.app.Fragment;

/**
 * Activity for updating account information.
 */

public class UpdateAccountInfoActivity extends SingleFragmentAppBarActivity {
    @Override
    protected Fragment createFragment() {
        return new UpdateAccountInfoFragment();
    }
}