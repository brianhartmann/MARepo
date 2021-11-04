package com.mobileapps.moviefinder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

/* RecyclerView modeled after Adam Champion's recycler view in PhotoGalleryFragment
    from his TicTacToeNew application */

public class MoviePosterFragment extends Fragment {
    RecyclerView postersRecyclerView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        MoviePosterActivity activity = (MoviePosterActivity) getActivity();
        View v = inflater.inflate(R.layout.fragment_movie_poster, container, false);

        String jsonArr = activity.getFindMovieData();

        try{
            JSONArray jsonArray = new JSONArray(jsonArr);

            for (int i = 0; i < jsonArray.length(); i++){
                // Figure out how to display the photo and title in xml
                Log.d("MoviePoster", jsonArray.getJSONObject(i).getString("poster_path"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        postersRecyclerView = v.findViewById(R.id.postersRecyclerView);
//        postersRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
//        postersRecyclerView.setAdapter(adapter);

        return v;
    }

}