package com.mobileapps.moviefinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class MoviePosterActivity extends SingleFragmentAppBarActivity {
    private String jsonArr;

    @Override
    protected Fragment createFragment() {
        return new MoviePosterFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        jsonArr = intent.getStringExtra("jsonArray");
    }

    public String getFindMovieData() {
        return jsonArr;
    }
}