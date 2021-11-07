package com.mobileapps.moviefinder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatchLaterFragment extends MoviePosterFragment {
    private TextView pageTitle;
    private RecyclerView watchLaterPosterRV;
    private Map<String, Object> watchLaterList = new HashMap<>();
    private DocumentReference documentReference = null;
    private Map<String, Object> userRecord = null;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_poster, container, false);

        pageTitle = v.findViewById(R.id.pageTitle);
        pageTitle.setText("Movies to Watch Later");

        // Handle getting the current user's watch later list
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            documentReference = FirebaseFirestore.getInstance().
                    collection("users").document(user.getUid());

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("WatchLater", "DocumentSnapshot data: " + document.getData());
                            userRecord = document.getData();

                            watchLaterList = (Map<String, Object>) userRecord.get("Watch Later");
                            List<GalleryItem> gallery = new ArrayList<>();

                            // Populate the gallery items (watch later movie poster info)
                            for (Object objItem : watchLaterList.values()) {
                                HashMap<String, String> obj = (HashMap<String, String>) objItem;
                                gallery.add(new GalleryItem(obj.get("title"), obj.get("id"),
                                        obj.get("url"), obj.get("overview"),
                                        obj.get("releaseDate"), obj.get("voteAverage")));
                            }
                            // System.out.println(" WatchLaterGallery " + gallery);

                            watchLaterPosterRV = v.findViewById(R.id.postersRecyclerView);
                            watchLaterPosterRV.setLayoutManager(new GridLayoutManager(requireContext(), 1));
                            watchLaterPosterRV.setAdapter(new PosterAdapter(gallery, "WatchLater",
                                    requireActivity(), null, watchLaterList, documentReference, userRecord));
                        } else {
                            Log.d("WatchLater", "No such document");
                        }
                    } else {
                        Log.d("WatchLater", "Document ref get failed with ", task.getException());
                    }
                }
            });

        } else {
            Log.d("WatchLater", "ERROR: User returned null");
        }

        return v;
    }
}