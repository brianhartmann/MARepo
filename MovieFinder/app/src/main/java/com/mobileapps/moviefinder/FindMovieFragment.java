package com.mobileapps.moviefinder;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;

//https://api.themoviedb.org/3/movie/550?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US

public class FindMovieFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    TextView r;
    Spinner filterCat;
    Spinner filter2;
    String item;


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
        r = (TextView) v.findViewById(R.id.Title);
        filterCat = (Spinner) v.findViewById(R.id.filterCategory);
        filter2 = (Spinner) v.findViewById(R.id.specificFilter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterCat.setAdapter(adapter);
        filterCat.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.genre, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter2.setAdapter(adapter2);


//        //Following code was from developer android guide--
//
//        RequestQueue requestQueue;
//
//        // Instantiate the cache
//        Cache cache = new DiskBasedCache(activity.getCacheDir(), 1024 * 1024); // 1MB cap
//
//        // Set up the network to use HttpURLConnection as the HTTP client.
//        Network network = new BasicNetwork(new HurlStack());
//
//        // Instantiate the RequestQueue with the cache and network.
//        requestQueue = new RequestQueue(cache, network);
//
//        // Start the queue
//        requestQueue.start();
//
//        //String url = "https://api.themoviedb.org/3/movie/550?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US";
//        //String url = "https://api.themoviedb.org/3/discover/movie?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US&with_genres=action&page=1";
//        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-";
//
//        // Formulate the request and handle the response.
//        Log.d("FindMovieFragment", "Before call");
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        JSONObject obj = null;
//                        try {
//                            obj = new JSONObject(response);
//                            Log.d("FindMovieFragment", obj.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        //r.setText("Response is: "+ response.substring(0,500));
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("FindMovieFragment", "Fail");
//                    }
//                });
//        Log.d("FindMovieFragment", "After call");
//
//        // Add the request to the RequestQueue.
//        requestQueue.add(stringRequest);
//
//
//        Log.d("FindMovieFragment", "end");
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        String item = parent.getItemAtPosition(pos).toString();
        Log.d("FindMovie", item);

        if (!item.equals("Genre")){
            filter2.setEnabled(false);
        } else {
            filter2.setEnabled(true);
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        Log.d("FindMovie", "nothin");
    }
}