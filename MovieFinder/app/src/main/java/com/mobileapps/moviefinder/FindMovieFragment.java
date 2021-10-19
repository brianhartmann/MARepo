package com.mobileapps.moviefinder;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class FindMovieFragment extends Fragment {
    TextView r;


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
        Log.d("FindMovieFragment", "sss");
        Activity activity = requireActivity();
        v = inflater.inflate(R.layout.fragment_find_movie, container, false);
        r = (TextView) v.findViewById(R.id.Title);


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

        String url = "https://api.themoviedb.org/3/movie/550?api_key=60567f6564d6a0a4630275f9658c2fd2&language=en-US";

        // Formulate the request and handle the response.
        Log.d("FindMovieFragment", "Before call");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            r.setText(obj.getString("title"));
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
        Log.d("FindMovieFragment", "After call");

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);


        Log.d("FindMovieFragment", "end");
        return v;
    }
}