package com.mobileapps.moviefinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "DocSnippets";
    Button findMoviePageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findMoviePageBtn = findViewById(R.id.findMoviePage);

//        findMoviePageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), FindMovie.class));
//                //need to add a FindMovie activity and xml
//            }
//        });

    }
}