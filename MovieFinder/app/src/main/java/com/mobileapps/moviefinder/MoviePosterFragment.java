package com.mobileapps.moviefinder;

import android.os.Bundle;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/* RecyclerView/Adapter modeled after Adam Champion's recycler view in PhotoGalleryFragment
    from his TicTacToeNew application */

public class MoviePosterFragment extends Fragment {
    private RecyclerView postersRecyclerView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        MoviePosterActivity activity = (MoviePosterActivity) getActivity();
        View v = inflater.inflate(R.layout.fragment_movie_poster, container, false);

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
                        obj.getString("release_date")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        postersRecyclerView = v.findViewById(R.id.postersRecyclerView);
        postersRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        postersRecyclerView.setAdapter(new PosterAdapter(galItems));

        return v;
    }


    private static class PosterHolder extends RecyclerView.ViewHolder {
        private final TextView mItemTextView;
        private final ImageView posterImageView ;
        private TextView posterOverview;
        private LinearLayout extraInfoLayout;
        private Button showMoreBtn;

        public PosterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_gallery, parent, false));
            mItemTextView = itemView.findViewById(R.id.poster_title);
            posterImageView = itemView.findViewById(R.id.poster);
            posterOverview = itemView.findViewById(R.id.poster_overview);
            extraInfoLayout = itemView.findViewById(R.id.extraInfo);
            showMoreBtn = itemView.findViewById(R.id.expandButton);
        }
    }

    private class PosterAdapter extends RecyclerView.Adapter<PosterHolder> {
        private final List<GalleryItem> mGalleryItems;

        public PosterAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @NonNull
        @Override
        public PosterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            return new PosterHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PosterHolder holder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);

            String imageUrl = "https://image.tmdb.org/t/p/w500" + mGalleryItems.get(position).getUrl();
            Picasso.get().load(imageUrl).into(holder.posterImageView);

            holder.mItemTextView.setText(mGalleryItems.get(position).getTitle());
            holder.posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // when you click on a poster to get more info ?
                }
            });

            holder.showMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.extraInfoLayout.getVisibility() == View.GONE) {
                        holder.showMoreBtn.setText("Show Less Info");

                        String title = holder.mItemTextView.getText().toString();
                        holder.posterOverview.setText("Overview: " + galleryItem.getOverview());

                        holder.extraInfoLayout.setVisibility(View.VISIBLE);

                    } else {
                        holder.showMoreBtn.setText("Show More Info");
                        holder.extraInfoLayout.setVisibility(View.GONE);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }
}