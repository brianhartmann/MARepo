package com.mobileapps.moviefinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class PreviouslyWatchedFragment extends MoviePosterFragment {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_movie_poster, container, false);

        List<GalleryItem> gallery = new ArrayList<>();
        getDataAndLoadAdapter("PreviouslyWatched", v, gallery, requireActivity(), "PrevWatch");

        TextView pageTitle = v.findViewById(R.id.pageTitle);
        pageTitle.setText("Previously Watched Movies");

        return v;
    }

}