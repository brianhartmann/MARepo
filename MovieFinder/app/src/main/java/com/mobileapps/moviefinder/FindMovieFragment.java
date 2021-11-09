package com.mobileapps.moviefinder;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;
import java.util.concurrent.TimeUnit;

//https://api.themoviedb.org/3/movie/550?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US

public class FindMovieFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    TextView searchForMovies;
    TextView category;
    Spinner filterCat;
    Spinner filter2;
    Button findMovie;
    EditText numPages, numYear, person;
    RequestQueue requestQueue;
    Map<String, Object> previouslyWatchedList;
    List<Integer> previouslyWatched;


    Map<String, Integer> genreMapping = new HashMap<String, Integer>();
    Map<String, Integer> streamerMapping = new HashMap<>();
    ArrayAdapter<CharSequence> adapter2;
    ArrayAdapter<CharSequence> adapter3;
    int totalNumberOfPages;
    int currentPage;
    ProgressBar progressBar;

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    public FindMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
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
        person = v.findViewById(R.id.moviePerson);
        progressBar = v.findViewById(R.id.buffering);
        currentPage = 0;
        totalNumberOfPages = 0;

        previouslyWatched = new ArrayList<>();
        DocumentReference documentReference = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            documentReference = FirebaseFirestore.getInstance().
                    collection("users").document(user.getUid());

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> userRecord = document.getData();

                            previouslyWatchedList = (Map<String, Object>) userRecord.get("Previously Watched");

                            // Populate the gallery items (prev watched movie poster info)
                            for (Object objItem : previouslyWatchedList.values()){
                                HashMap<String, String> obj = (HashMap<String, String>) objItem;
                                previouslyWatched.add(Integer.parseInt(obj.get("id")));
                                Log.d("FindMovie", obj.get("id"));
                            }


                        }
                    }
                }

            });

        }




        //Following code was from developer android guide----------------

        // Instantiate the cache
        Cache cache = new DiskBasedCache(activity.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        //end developer android code ----------------

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                generateMovies(activity);
            }
        });



        //click listener for 'search' button
        findMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateMovies(activity);
            }
        });



        //drop down for initial search criteria
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterCat.setAdapter(adapter);
        filterCat.setOnItemSelectedListener(this);

        //drop down for genre
        adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.genre, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //drop down for streaming service
        adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.streamingService, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        return v;
    }


    public void generateMovies(Activity activity){
        String dropDownSelected = (String) filter2.getSelectedItem();
        String pages = numPages.getText().toString().trim();
        String moviePerson = person.getText().toString().trim();

        //sets page value if incorrect or left empty
        if (pages.equals("")){
            pages="9";
        }
        int numPagesInt = Integer.parseInt(pages);
        if (numPagesInt < 1) {
            numPagesInt = 9;
        }
        currentPage = 1;

        String url = "";
        String selectedItem = filterCat.getSelectedItem().toString();
        if (selectedItem.equals("Year")){
            url = String.format(Locale.US, "https://api.themoviedb.org/3/discover/movie?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&page=1&primary_release_year=%d",
                    Integer.parseInt(numYear.getText().toString()));
        } else if (selectedItem.equals("Within certain length")){
            url = String.format(Locale.US, "https://api.themoviedb.org/3/discover/movie?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&page=1&with_runtime.lte=%d",
                    Integer.parseInt(numYear.getText().toString()));
        } else if (selectedItem.equals("Genre")){
            url = String.format(Locale.US, "https://api.themoviedb.org/3/discover/movie?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&page=1&with_genres=%d",
                    genreMapping.get(dropDownSelected));
        } else if (selectedItem.equals("From streaming service")) {
            url = String.format(Locale.US, "https://api.themoviedb.org/3/discover/movie?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&page=1&with_watch_providers=%d&watch_region=US", streamerMapping.get(dropDownSelected));
        } else if (selectedItem.equals("With actor/actress")) {

            //format query to replace spaces with '%20'
            String newMoviePerson = "";
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < moviePerson.length(); i++){
                if (moviePerson.charAt(i) == ' '){
                    stringBuilder.append("%20");
                } else {
                    stringBuilder.append(moviePerson.charAt(i));
                }
                newMoviePerson = stringBuilder.toString();
            }
            url = String.format(Locale.US, "https://api.themoviedb.org/3/search/person?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&query=%s&page=1&include_adult=false",
                    newMoviePerson);
        } else {
            System.exit(1);
        }

        progressBar.setVisibility(View.VISIBLE);
        sendAPIRequest(url, activity, selectedItem, numPagesInt);

    }

    //sends request to api (structure of api request gotten from android developer guide)
    public void sendAPIRequest(String url, Activity activity, String request, int numberOfMovies){
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = new JSONArray();

                            //gets most popular actor/actress results and searches again for their movies
                            if (request.equals("With actor/actress")){
                                if (jsonObject.getJSONArray("results").length() == 0){
                                    Log.d("FindMovie", "Here");
                                    Toast.makeText(getContext(), "No actor/actress with that name", Toast.LENGTH_SHORT).show();

                                    progressBar.setVisibility(View.INVISIBLE);
                                } else {
                                    String id = jsonObject.getJSONArray("results").getJSONObject(0).getString("id");
                                    String url = String.format("https://api.themoviedb.org/3/person/%s/movie_credits?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US",
                                            id);
                                    sendAPIRequest(url, activity, "credits", numberOfMovies);
                                }



                            } else {
                                //for parsing movies from actor/actress
                                if (request.equals("credits")){
                                    jsonArray = jsonObject.getJSONArray("cast");
                                } else {    //for parsing other categories
                                    currentPage = Integer.parseInt(jsonObject.getString("page"));
                                    totalNumberOfPages = Integer.parseInt(jsonObject.getString("total_pages"));
                                    jsonArray = jsonObject.getJSONArray("results");

                                }

                                //dont display if no results
                                if (jsonArray.length() == 0){
                                    Toast.makeText(getContext(), "No actor/actress with that name", Toast.LENGTH_SHORT).show();
                                } else {
                                    int count = 0;
                                    JSONArray newJsonArray = new JSONArray();
                                    for (int i = 0; i < jsonArray.length(); i++){
                                        if (i > numberOfMovies) {
                                            break;
                                        } else if (previouslyWatched.contains(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")))){
                                            Log.d("FindMovie", jsonArray.getJSONObject(i).toString());
                                        } else {

                                            Log.d("FindMovie", String.valueOf(i));

                                            newJsonArray.put(count, jsonArray.getJSONObject(i));
                                            count++;
                                        }
                                    }
                                    jsonArray = newJsonArray;
                                    Log.d("FindMovie newjson:", String.valueOf(jsonArray.length()));
                                    Log.d("FindMovie json:", String.valueOf(jsonArray.length()));

                                    //20 issue

                                    progressBar.setVisibility(View.GONE);
                                    // Start the Movie Posters generated activity
                                    Intent i = new Intent(activity, MoviePosterActivity.class);
                                    i.putExtra("jsonArray", newJsonArray.toString());
                                    Log.d("FindMovie", "Generate Movie Posters, start activity");
                                    Toast.makeText(getContext(), "Movie(s) Generated", Toast.LENGTH_LONG).show();
                                    startActivity(i);

                                }

                            }
                        } catch (JSONException e) {
                            Log.d("FindMovie", e.toString());
                        }

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

    //sets UI depending on which search category is selected
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
            person.setVisibility(View.INVISIBLE);
            category.setText("Movies");
        } else if (item.equals("Year") || item.equals("Within certain length")) {
            numYear.setVisibility(View.VISIBLE);
            filter2.setVisibility(View.INVISIBLE);
            searchForMovies.setVisibility(View.VISIBLE);
            category.setVisibility(View.VISIBLE);
            person.setVisibility(View.INVISIBLE);
            if (item.equals("Year")){
                category.setText("Movies");
            } else {
                category.setText("Minutes");
            }
        } else if (item.equals("From streaming service")){
            numYear.setVisibility(View.INVISIBLE);
            filter2.setVisibility(View.VISIBLE);
            searchForMovies.setVisibility(View.VISIBLE);
            category.setVisibility(View.VISIBLE);
            filter2.setAdapter(adapter3);
            person.setVisibility(View.INVISIBLE);
            category.setText("Movies");
        } else if (item.equals("With actor/actress")){
            numYear.setVisibility(View.INVISIBLE);
            filter2.setVisibility(View.INVISIBLE);
            searchForMovies.setVisibility(View.VISIBLE);
            category.setVisibility(View.VISIBLE);
            filter2.setAdapter(adapter2);
            person.setVisibility(View.VISIBLE);
            category.setText("Movies");
        } else {
            filter2.setVisibility(View.INVISIBLE);
            searchForMovies.setVisibility(View.INVISIBLE);
            category.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}