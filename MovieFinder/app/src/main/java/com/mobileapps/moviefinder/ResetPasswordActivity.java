package com.mobileapps.moviefinder;

import androidx.fragment.app.Fragment;

/**
 * Activity for updating account information (resetting password).
 */

public class ResetPasswordActivity extends SingleFragmentAppBarActivity {
    @Override
    protected Fragment createFragment() {
        return new ResetPasswordFragment();
    }
}