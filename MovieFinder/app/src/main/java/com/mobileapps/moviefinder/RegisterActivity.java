package com.mobileapps.moviefinder;

import androidx.fragment.app.Fragment;

public class RegisterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }
}