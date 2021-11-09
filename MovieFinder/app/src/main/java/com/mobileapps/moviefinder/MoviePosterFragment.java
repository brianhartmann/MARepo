package com.mobileapps.moviefinder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* RecyclerView/Adapter modeled after Adam Champion's recycler view in PhotoGalleryFragment
    from his TicTacToeNew application */

public class MoviePosterFragment extends Fragment {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        MoviePosterActivity activity = (MoviePosterActivity) getActivity();
        View v;

        String jsonArr = activity.getFindMovieData();
        List<GalleryItem> galItems = new ArrayList<>();

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

        v = getDataAndLoadAdapter("MoviePoster", inflater, container, galItems, requireActivity(), "");

        return v;
    }


    public View getDataAndLoadAdapter(String fragmentName, @NonNull LayoutInflater inflater, ViewGroup container,
                                      List<GalleryItem> galItems, FragmentActivity activity, String populateArr) {
        View view = inflater.inflate(R.layout.fragment_movie_poster, container, false);

        // Handle getting the current user's previously watched and/or watch later lists
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            DocumentReference documentReference = FirebaseFirestore.getInstance().
                    collection("users").document(user.getUid());

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(fragmentName, "DocumentSnapshot data: " + document.getData());
                            Map<String, Object> userRecord = document.getData();


                            Map<String, Object> prevWatchedList = (Map<String, Object>) userRecord.get("Previously Watched");
                            Map<String, Object> watchLaterList = (Map<String, Object>) userRecord.get("Watch Later");

                            System.out.println("prev " + prevWatchedList);
                            System.out.println("watch " + watchLaterList);

                            if(populateArr.equals("PrevWatch")) {
                                // Populate the gallery items (prev watched movie poster info)
                                for (Object objItem : prevWatchedList.values()){
                                    HashMap<String, String> obj = (HashMap<String, String>) objItem;
                                    galItems.add(new GalleryItem(obj.get("title"), obj.get("id"),
                                            obj.get("url"), obj.get("overview"),
                                            obj.get("releaseDate"), obj.get("voteAverage")));
                                }
                                // System.out.println(" PrevWatchedGallery " + gallery);

                            } else if(populateArr.equals("WatchLat")) {
                                // Populate the gallery items (watch later movie poster info)
                                for (Object objItem : watchLaterList.values()) {
                                    HashMap<String, String> obj = (HashMap<String, String>) objItem;
                                    galItems.add(new GalleryItem(obj.get("title"), obj.get("id"),
                                            obj.get("url"), obj.get("overview"),
                                            obj.get("releaseDate"), obj.get("voteAverage")));
                                }
                                // System.out.println(" WatchLaterGallery " + gallery);
                            }

                            RecyclerView postersRecyclerView = view.findViewById(R.id.postersRecyclerView);
                            postersRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
                            postersRecyclerView.setAdapter(new PosterAdapter(galItems, fragmentName,
                                    activity, prevWatchedList, watchLaterList, documentReference, userRecord));


                        } else {
                            Log.d(fragmentName, "No such document");
                        }
                    } else {
                        Log.d(fragmentName, "Document ref get failed with ", task.getException());
                    }
                }
            });

        } else {
            Log.d(fragmentName, "ERROR: User returned null");
        }

        return view;
    }


    private static class PosterHolder extends RecyclerView.ViewHolder {
        private final TextView mItemTextView;
        private final ImageView posterImageView ;
        private TextView posterOverview, posterReleaseDate, posterRating;
        private LinearLayout extraInfoLayout;
        private Button showMoreBtn, addPrevWatchedBtn, addWatchLaterBtn, removeFromListBtn;
        private View addListLayout, removeListLayout;

        public PosterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_gallery, parent, false));
            mItemTextView = itemView.findViewById(R.id.poster_title);
            posterImageView = itemView.findViewById(R.id.poster);
            posterOverview = itemView.findViewById(R.id.poster_overview);
            posterReleaseDate = itemView.findViewById(R.id.poster_release_date);
            posterRating = itemView.findViewById(R.id.poster_rating);

            extraInfoLayout = itemView.findViewById(R.id.extraInfo);
            showMoreBtn = itemView.findViewById(R.id.expandButton);
            addPrevWatchedBtn = itemView.findViewById(R.id.addPrevWatchedBtn);
            addWatchLaterBtn = itemView.findViewById(R.id.addWatchLaterBtn);
            removeFromListBtn = itemView.findViewById(R.id.removeFromListBtn);

            addListLayout = itemView.findViewById(R.id.addListLayout);
            removeListLayout = itemView.findViewById(R.id.removeListLayout);
        }
    }

    public class PosterAdapter extends RecyclerView.Adapter<PosterHolder> {
        private final List<GalleryItem> mGalleryItems;
        private final String mFragName;
        private final FragmentActivity mActivity;
        private final Map<String, Object> mPrevWatchedList;
        private final Map<String, Object> mWatchLaterList;
        private DocumentReference mDocumentReference = null;
        private Map<String, Object> mUserRecord = null;

        public PosterAdapter(List<GalleryItem> galleryItems, String fragName, FragmentActivity activity, Map<String, Object> prevWatchList,
                             Map<String, Object> watchLatList, DocumentReference docRef, Map<String, Object> userRec) {
            mGalleryItems = galleryItems;
            mFragName = fragName;
            mActivity = activity;
            mPrevWatchedList = prevWatchList;
            mWatchLaterList = watchLatList;

            mUserRecord = userRec;
            mDocumentReference = docRef;
        }

        @NonNull
        @Override
        public PosterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            return new PosterHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PosterHolder holder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);

            String imageUrl = "https://image.tmdb.org/t/p/w500" + mGalleryItems.get(position).getUrl();
            Picasso.get().load(imageUrl).into(holder.posterImageView);

            // If movie is on watch later list then highlight title
            holder.mItemTextView.setText(mGalleryItems.get(position).getTitle());
            if(mFragName.equals("MoviePoster") && mWatchLaterList.containsKey(galleryItem.getId())) {
                holder.mItemTextView.setTypeface(null, Typeface.BOLD_ITALIC);
                holder.mItemTextView.setBackgroundColor(Color.rgb(255, 235, 59));
            }

            holder.showMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.extraInfoLayout.getVisibility() == View.GONE) {
                        holder.showMoreBtn.setText("Show Less Info");

                        holder.posterReleaseDate.setText("Release Date: " + galleryItem.getReleaseDate());
                        holder.posterRating.setText("Average Rating: " + galleryItem.getVoteAverage() + "/10");
                        holder.posterOverview.setText("Overview: " + galleryItem.getOverview());

                        holder.extraInfoLayout.setVisibility(View.VISIBLE);

                        // Make different button layouts visible/not visible depending on which fragment is being displayed
                        if(mFragName.equals("PreviouslyWatched")) {
                            holder.addListLayout.setVisibility(View.GONE);
                            holder.removeListLayout.setVisibility(View.VISIBLE);


                        } else if (mFragName.equals("WatchLater")) {
                            holder.addListLayout.setVisibility(View.GONE);
                            holder.removeListLayout.setVisibility(View.VISIBLE);
                            holder.removeFromListBtn.setText("Watch Later List");


                        } else if (mFragName.equals("MoviePoster")){
                            //If this movie is on the prev watched or watch later list then disable buttons
                            if(mPrevWatchedList.containsKey(galleryItem.getId())) {
                                holder.addPrevWatchedBtn.setEnabled(false);
                            }

                            if(mWatchLaterList.containsKey(galleryItem.getId())) {
                                holder.addWatchLaterBtn.setEnabled(false);
                            }

                        }

                    } else {
                        holder.showMoreBtn.setText("Show More Info");
                        holder.extraInfoLayout.setVisibility(View.GONE);
                    }
                }
            });



            // Handle adding the movie, the galItem, to the user's previously watched/watch later lists
            holder.addPrevWatchedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPrevWatchedList.put(galleryItem.getId(), galleryItem);
                    if(mUserRecord != null) {
                        mUserRecord.put("Previously Watched", mPrevWatchedList);
                    }

                    if(mDocumentReference != null) {
                        mDocumentReference.set(mUserRecord).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("MoviePoster", "onSuccess: Movie was added to prev watched list and updated");
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("MoviePoster", "onFailure: " + e.toString());
                            }
                        });
                    }

                    holder.addPrevWatchedBtn.setEnabled(false);
                }
            });


            holder.addWatchLaterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWatchLaterList.put(galleryItem.getId(), galleryItem);
                    if(mUserRecord != null) {
                        mUserRecord.put("Watch Later", mWatchLaterList);
                    }

                    if(mDocumentReference != null) {
                        mDocumentReference.set(mUserRecord).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("MoviePoster", "onSuccess: Movie was added to watch later list and updated");
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("MoviePoster", "onFailure: " + e.toString());
                            }
                        });
                    }

                    holder.addWatchLaterBtn.setEnabled(false);
                }
            });


            // Handle removing the movie, the galItem, from the user's previously watched/watch later lists
            holder.removeFromListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mFragName.equals("PreviouslyWatched")) {
                        mPrevWatchedList.remove(galleryItem.getId());

                        if(mUserRecord != null) {
                            mUserRecord.put("Previously Watched", mPrevWatchedList);
                        }

                    } else if(mFragName.equals("WatchLater")) {
                        mWatchLaterList.remove(galleryItem.getId());

                        if(mUserRecord != null) {
                            mUserRecord.put("Watch Later", mWatchLaterList);
                        }
                    }


                    if(mDocumentReference != null) {
                        mDocumentReference.set(mUserRecord).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("MoviePosterAdapter", "onSuccess: Movie was removed to the list and updated");
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("MoviePosterAdapter", "onFailure: " + e.toString());
                            }
                        });
                    }

                    holder.removeFromListBtn.setEnabled(false);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }
}