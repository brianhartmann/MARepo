package com.mobileapps.moviefinder;

import androidx.fragment.app.Fragment;

/**
 * Activity for account information.
 */

public class AccountInfoActivity extends SingleFragmentAppBarActivity {
    @Override
    protected Fragment createFragment() {
        return new AccountInfoFragment();
    }
}