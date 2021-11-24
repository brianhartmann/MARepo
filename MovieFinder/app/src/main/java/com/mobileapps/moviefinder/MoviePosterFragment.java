package com.mobileapps.moviefinder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoviePosterFragment extends Fragment {
    Button increaseText;
    Button decreaseText;
    int sizeChange;

    Map<String, Object> prevWatchedList;
    Map<String, Object> watchLaterList;
    List<GalleryItem> galItems;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        MoviePosterActivity activity = (MoviePosterActivity) getActivity();
        View v = inflater.inflate(R.layout.fragment_movie_poster, container, false);




        String jsonArr = "";
        if(activity != null) {
            jsonArr = activity.getFindMovieData();
        }
        galItems = new ArrayList<>();

        try{
            JSONArray jsonArray = new JSONArray(jsonArr);
            System.out.println(jsonArray);
            System.out.println("LEN " + jsonArray.length());

            // Populate the gallery items (movie poster info)
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                galItems.add(new GalleryItem(obj.getString("title"), obj.getString("id"),
                        obj.getString("poster_path"), obj.getString("overview"),
                        obj.getString("release_date"), obj.getString("vote_average")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        getDataAndLoadAdapter("MoviePoster", v, galItems, requireActivity(), "");

        return v;
    }


    public void getDataAndLoadAdapter(String fragmentName, View v,
                                      List<GalleryItem> galItems, FragmentActivity activity, String populateArr) {
        TextView description = v.findViewById(R.id.description);

        // Load an empty adapter first then replace later after receiving user's list data
        RecyclerView postersRecyclerView = v.findViewById(R.id.postersRecyclerView);
        postersRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        PosterAdapter adapter = new PosterAdapter(galItems, fragmentName,
                activity, null, null, null, null, 0);
        postersRecyclerView.setAdapter(adapter);


            increaseText = v.findViewById(R.id.increase);
            decreaseText = v.findViewById(R.id.decrease);

        // Handle getting the current user's previously watched and/or watch later lists
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            DocumentReference documentReference = FirebaseFirestore.getInstance().
                    collection("users").document(user.getUid());

            documentReference.get().addOnCompleteListener((@NonNull Task<DocumentSnapshot> task) -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d(fragmentName, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> userRecord = document.getData();

                        prevWatchedList = new HashMap<>();
                        watchLaterList = new HashMap<>();
                        if(userRecord != null) {
                            prevWatchedList = (Map<String, Object>) userRecord.get("Previously Watched");
                            watchLaterList = (Map<String, Object>) userRecord.get("Watch Later");
                        }

                        System.out.println("prev " + prevWatchedList);
                        System.out.println("watch " + watchLaterList);

                        if(populateArr.equals("PrevWatch") && prevWatchedList != null) {
                            // Populate the gallery items (prev watched movie poster info)
                            for (Object objItem : prevWatchedList.values()){
                                HashMap<String, String> obj = (HashMap<String, String>) objItem;
                                galItems.add(new GalleryItem(obj.get("title"), obj.get("id"),
                                        obj.get("url"), obj.get("overview"),
                                        obj.get("releaseDate"), obj.get("voteAverage")));
                            }

                            if(prevWatchedList.size() == 0) {
                                description.setText("No movies on your previously watched list.");
                                description.setVisibility(View.VISIBLE);
                            }

                        } else if(populateArr.equals("WatchLat") && watchLaterList != null) {
                            // Populate the gallery items (watch later movie poster info)
                            for (Object objItem : watchLaterList.values()) {
                                HashMap<String, String> obj = (HashMap<String, String>) objItem;
                                galItems.add(new GalleryItem(obj.get("title"), obj.get("id"),
                                        obj.get("url"), obj.get("overview"),
                                        obj.get("releaseDate"), obj.get("voteAverage")));
                            }

                            if(watchLaterList.size() == 0) {
                                description.setText("No movies on your watch later list.");
                                description.setVisibility(View.VISIBLE);
                            }
                        }

                            increaseText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    sizeChange++;
                                    postersRecyclerView.swapAdapter(new PosterAdapter(galItems, fragmentName,
                                            activity, prevWatchedList, watchLaterList, documentReference, userRecord, 1), false);

                                }

                            });

                            decreaseText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    sizeChange--;
                                    postersRecyclerView.swapAdapter(new PosterAdapter(galItems, fragmentName,
                                            activity, prevWatchedList, watchLaterList, documentReference, userRecord, -1), false);
                                    Log.d("Results", String.valueOf(sizeChange));
                                }
                            });



                        postersRecyclerView.swapAdapter(new PosterAdapter(galItems, fragmentName,
                                activity, prevWatchedList, watchLaterList, documentReference, userRecord, 0), false);
                    } else {
                        Log.d(fragmentName, "No such document");
                    }
                } else {
                    Log.d(fragmentName, "Document ref get failed with ", task.getException());
                }
            });

        } else {
            Log.d(fragmentName, "ERROR: User returned null");
        }
    }
}