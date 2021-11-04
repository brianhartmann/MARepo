package com.mobileapps.moviefinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

//https://api.themoviedb.org/3/movie/550?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US

public class FindMovieFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    TextView searchForMovies;
    TextView category;
    Spinner filterCat;
    Spinner filter2;
    Button findMovie;
    EditText numPages, numYear;


    Map<String, Integer> genreMapping = new HashMap<String, Integer>();
    Map<String, Integer> streamerMapping = new HashMap<>();
    ArrayAdapter<CharSequence> adapter2;
    ArrayAdapter<CharSequence> adapter3;

    public FindMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v;
        Activity activity = requireActivity();
        v = inflater.inflate(R.layout.fragment_find_movie, container, false);
        filterCat = (Spinner) v.findViewById(R.id.filterCategory);
        filter2 = (Spinner) v.findViewById(R.id.specificFilter);
        findMovie = v.findViewById(R.id.button);
        searchForMovies = v.findViewById(R.id.searchForMoviesfor);
        category = v.findViewById(R.id.category);
        numPages = v.findViewById(R.id.editTextNumber);
        numYear = v.findViewById(R.id.numberField);

        //maps streaming service to streaming service id for api
        streamerMapping.put("Netflix", 8);
        streamerMapping.put("Amazon Prime Video", 9);
        streamerMapping.put("Disney Plus", 337);
        streamerMapping.put("Apple iTunes", 2);
        streamerMapping.put("Google Play Movies", 3);
        streamerMapping.put("Hulu", 15);
        streamerMapping.put("Paramount Plus", 531);
        streamerMapping.put("HBO Max", 384);
        streamerMapping.put("Peacock", 386);
        streamerMapping.put("YouTube", 192);
        streamerMapping.put("Showtime", 37);



        //maps genre to genre id for api
        genreMapping.put("Action", 28);
        genreMapping.put("Adventure", 12);
        genreMapping.put("Animation", 16);
        genreMapping.put("Comedy", 35);
        genreMapping.put("Crime", 80);
        genreMapping.put("Documentary", 99);
        genreMapping.put("Drama", 18);
        genreMapping.put("Family", 10751);
        genreMapping.put("Fantasy", 14);
        genreMapping.put("History", 36);
        genreMapping.put("Horror", 27);
        genreMapping.put("Music", 10402);
        genreMapping.put("Mystery", 9648);
        genreMapping.put("Romance", 10749);
        genreMapping.put("Science Fiction", 878);
        genreMapping.put("TV Movie", 10770);
        genreMapping.put("Thriller", 53);
        genreMapping.put("War", 10752);
        genreMapping.put("Western", 37);

        //Following code was from developer android guide--

        RequestQueue requestQueue;

        // Instantiate the cache
        Cache cache = new DiskBasedCache(activity.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        findMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dropDownSelected = (String) filter2.getSelectedItem();
                String pages = numPages.getText().toString().trim();
                if (pages.equals("")){
                    pages="1";
                }
                int numPagesInt = Integer.parseInt(pages);
                if (numPagesInt < 1) {
                    numPagesInt = 1;
                }

                String url = "";

                if (filterCat.getSelectedItem().toString().equals("Year")){
                    url = String.format("https://api.themoviedb.org/3/discover/movie?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&page=%d&primary_release_year=%d",
                            numPagesInt, Integer.parseInt(numYear.getText().toString()));
                } else if (filterCat.getSelectedItem().toString().equals("Within certain length")){
                    url = String.format("https://api.themoviedb.org/3/discover/movie?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&page=%d&with_runtime.lte=%d",
                            numPagesInt, Integer.parseInt(numYear.getText().toString()));
                } else if (filterCat.getSelectedItem().toString().equals("Genre")){
                    url = String.format("https://api.themoviedb.org/3/discover/movie?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&page=%d&with_genres=%d",
                            numPagesInt, genreMapping.get(dropDownSelected));
                } else if (filterCat.getSelectedItem().toString().equals("From streaming service")) {
                    url = String.format("https://api.themoviedb.org/3/discover/movie?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&page=%d&with_watch_providers=%d&watch_region=US",
                            numPagesInt, streamerMapping.get(dropDownSelected));
                } else {
                    System.exit(1);
                }

                // Formulate the request and handle the response.
                     StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject obj = null;
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                                    //Log.d("FindMovie", jsonObject.toString());
                                    for (int i = 0; i < jsonArray.length(); i++){
                                        Log.d("FindMovie", jsonArray.getJSONObject(i).getString("title"));
                                        //Log.d("FindMovie", jsonArray.getJSONObject(i).getString("provider_name"));
                                        //Log.d("FindMovie", jsonArray.getJSONObject(i).toString());
                                    }

                                    // Start the Movie Posters generated activity
                                    Intent i = new Intent(activity, MoviePosterActivity.class);
                                    i.putExtra("jsonArray", jsonArray.toString());
                                    Log.d("FindMovie", "Generate Movie Posters, start activity");
                                    startActivity(i);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //r.setText("Response is: "+ response.substring(0,500));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("FindMovieFragment", "Fail");
                            }
                        });

                // Add the request to the RequestQueue.
                requestQueue.add(stringRequest);


            }
        });

        //set listeners for dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterCat.setAdapter(adapter);
        filterCat.setOnItemSelectedListener(this);


        adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.genre, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.streamingService, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String item;
        item = parent.getItemAtPosition(pos).toString();

        if (item.equals("Genre")){
            numYear.setVisibility(View.INVISIBLE);
            filter2.setVisibility(View.VISIBLE);
            searchForMovies.setVisibility(View.VISIBLE);
            category.setVisibility(View.VISIBLE);
            filter2.setAdapter(adapter2);


        } else if (item.equals("Year") || item.equals("Within certain length")) {
            numYear.setVisibility(View.VISIBLE);
            filter2.setVisibility(View.INVISIBLE);
            searchForMovies.setVisibility(View.VISIBLE);
            category.setVisibility(View.VISIBLE);
        } else if (item.equals("From streaming service")){
            numYear.setVisibility(View.INVISIBLE);
            filter2.setVisibility(View.VISIBLE);
            searchForMovies.setVisibility(View.VISIBLE);
            category.setVisibility(View.VISIBLE);
            filter2.setAdapter(adapter3);
        } else {
            filter2.setVisibility(View.INVISIBLE);
            searchForMovies.setVisibility(View.INVISIBLE);
            category.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}