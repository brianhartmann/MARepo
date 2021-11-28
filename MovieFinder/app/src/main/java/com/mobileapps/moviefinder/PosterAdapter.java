package com.mobileapps.moviefinder;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/* RecyclerView/Adapter modeled after Adam Champion's recycler view in PhotoGalleryFragment
    from his TicTacToeNew application */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterHolder> {
    private final List<GalleryItem> mGalleryItems;
    private final String mFragName;
    private final FragmentActivity mActivity;
    private final Map<String, Object> mPrevWatchedList;
    private final Map<String, Object> mWatchLaterList;
    private final DocumentReference mDocumentReference;
    private final Map<String, Object> mUserRecord;
    static int sizeChange = 0;


    public PosterAdapter(List<GalleryItem> galleryItems, String fragName, FragmentActivity activity, Map<String, Object> prevWatchList,
                         Map<String, Object> watchLatList, DocumentReference docRef, Map<String, Object> userRec, int changeInSize) {
        mGalleryItems = galleryItems;
        mFragName = fragName;
        mActivity = activity;
        mPrevWatchedList = prevWatchList;
        mWatchLaterList = watchLatList;

        mUserRecord = userRec;
        mDocumentReference = docRef;

        sizeChange = changeInSize;


    }



    public static class PosterHolder extends RecyclerView.ViewHolder {
        private final TextView mItemTextView;
        private final ImageView posterImageView ;
        private final TextView posterOverview, posterReleaseDate, posterRating;
        private final LinearLayout extraInfoLayout;
        private final Button showMoreBtn, addPrevWatchedBtn, addWatchLaterBtn, removeFromListBtn;
        private final View addListLayout, removeListLayout;

        public PosterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_gallery, parent, false));
            Log.d("Change", String.valueOf(sizeChange));
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
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_baseline_local_movies_24)
                .resize(600, 900).into(holder.posterImageView);

        // If movie is on watch later list then highlight title
        holder.mItemTextView.setText(galleryItem.getTitle());
        if(mFragName.equals("MoviePoster") && mWatchLaterList != null && mWatchLaterList.containsKey(galleryItem.getId())) {
            holder.mItemTextView.setTypeface(null, Typeface.BOLD_ITALIC);
            holder.mItemTextView.setBackgroundColor(Color.rgb(255, 235, 59));

        } else {
            holder.mItemTextView.setTypeface(null, Typeface.NORMAL);
            holder.mItemTextView.setBackgroundColor(Color.rgb(255, 255, 255));
        }

        String release = "Release Date: " + galleryItem.getReleaseDate();
        String rating = "Average Rating: " + galleryItem.getVoteAverage() + "/10";
        String overview = "Overview: " + galleryItem.getOverview();
        holder.posterReleaseDate.setText(release);
        holder.posterRating.setText(rating);
        holder.posterOverview.setText(overview);

        holder.showMoreBtn.setText("Show More Info");
        holder.extraInfoLayout.setVisibility(View.GONE);
        if(mFragName.equals("WatchLater") || mFragName.equals("PreviouslyWatched")) {
            holder.addListLayout.setVisibility(View.GONE);
            holder.removeListLayout.setVisibility(View.VISIBLE);

        } else {
            holder.addListLayout.setVisibility(View.VISIBLE);
            holder.removeListLayout.setVisibility(View.GONE);
        }

        enableButton(holder.addPrevWatchedBtn);
        enableButton(holder.addWatchLaterBtn);
        enableButton(holder.removeFromListBtn);


        holder.showMoreBtn.setOnClickListener((View view) -> {
            if(holder.extraInfoLayout.getVisibility() == View.GONE) {
                holder.showMoreBtn.setText("Show Less Info");

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
                    if(mPrevWatchedList == null || mPrevWatchedList.containsKey(galleryItem.getId())) {
                        disableButton(holder.addPrevWatchedBtn);
                    }

                    if(mWatchLaterList == null || mWatchLaterList.containsKey(galleryItem.getId())) {
                        disableButton(holder.addWatchLaterBtn);
                    }

                }

            } else {
                holder.showMoreBtn.setText("Show More Info");
                holder.extraInfoLayout.setVisibility(View.GONE);
            }
        });



        // Handle adding the movie, the galItem, to the user's previously watched/watch later lists
        holder.addPrevWatchedBtn.setOnClickListener((View view) ->{
            mPrevWatchedList.put(galleryItem.getId(), galleryItem);
            if(mUserRecord != null) {
                mUserRecord.put("Previously Watched", mPrevWatchedList);

                if(mDocumentReference != null) {
                    mDocumentReference.set(mUserRecord).addOnSuccessListener((Void aVoid) -> {
                        Log.d("PosterAdapter", "onSuccess: Movie was added to prev watched list and updated");
                        Toast.makeText(mActivity, "Added to previously watched", Toast.LENGTH_SHORT).show();
                        disableButton(holder.addPrevWatchedBtn);

                    }).addOnFailureListener((@NonNull Exception e) -> Log.d("PosterAdapter", "onFailure: " + e.toString()));
                }
            }
        });


        holder.addWatchLaterBtn.setOnClickListener((View view) -> {
            if(mWatchLaterList != null) {
                mWatchLaterList.put(galleryItem.getId(), galleryItem);
            }

            if(mUserRecord != null) {
                mUserRecord.put("Watch Later", mWatchLaterList);

                if(mDocumentReference != null) {
                    mDocumentReference.set(mUserRecord).addOnSuccessListener((Void aVoid) -> {
                        Log.d("PosterAdapter", "onSuccess: Movie was added to watch later list and updated");
                        Toast.makeText(mActivity, "Added to watch later", Toast.LENGTH_SHORT).show();
                        disableButton(holder.addWatchLaterBtn);

                    }).addOnFailureListener((@NonNull Exception e) -> Log.d("PosterAdapter", "onFailure: " + e.toString()));
                }
            }
        });


        // Handle removing the movie, the galItem, from the user's previously watched/watch later lists
        holder.removeFromListBtn.setOnClickListener((View view) -> {
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


            if(mUserRecord != null && mDocumentReference != null) {
                mDocumentReference.set(mUserRecord).addOnSuccessListener((Void aVoid) -> {
                    Log.d("PosterAdapter", "onSuccess: Movie was removed to the list and updated");
                    Toast.makeText(mActivity, "Removed from list.", Toast.LENGTH_SHORT).show();
                    disableButton(holder.removeFromListBtn);

                    // Remove the item from the page
                    System.out.println("GAL " + mGalleryItems);
                    int pos = mGalleryItems.indexOf(galleryItem);
                    mGalleryItems.remove(galleryItem);
                    notifyItemRemoved(pos);

                }).addOnFailureListener((@NonNull Exception e) -> Log.d("PosterAdapter", "onFailure: " + e.toString()));
            }
        });


//        ViewGroup.LayoutParams params = holder.posterImageView.getLayoutParams();
//        Log.d("Poster", String.valueOf(params.height));
//        Log.d("Poster", String.valueOf(params.height));
//        params.
//        if (sizeChange > 0){
//            params.width = params.width + 1;
//            params.height = (params.height + 2);
//        } else if (sizeChange < 0){
//            params.width = params.width - 1;
//            params.height = (params.height - 2);
//        }

//        holder.posterImageView.setLayoutParams(params);
        
        holder.mItemTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,holder.mItemTextView.getTextSize()+sizeChange);
        holder.posterOverview.setTextSize(TypedValue.COMPLEX_UNIT_PX,holder.posterOverview.getTextSize()+sizeChange);
        holder.posterRating.setTextSize(TypedValue.COMPLEX_UNIT_PX,holder.posterRating.getTextSize()+sizeChange);
        holder.posterReleaseDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,holder.posterReleaseDate.getTextSize()+sizeChange );




    }

    @Override
    public int getItemCount() {
        return mGalleryItems.size();
    }

    public void disableButton(Button btn) {
        btn.setEnabled(false);
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(149, 149, 149)));
        btn.getBackground().setAlpha(170);
    }

    public void enableButton(Button btn) {
        btn.setEnabled(true);
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(23, 31, 198)));
        btn.getBackground().setAlpha(255);
    }
}
