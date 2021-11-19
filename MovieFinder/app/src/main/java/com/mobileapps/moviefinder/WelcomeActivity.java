package com.mobileapps.moviefinder;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppBarActivity {
    Button findMoviePageBtn;
    Button logout;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // Only allow vertical/portrait screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findMoviePageBtn = findViewById(R.id.findMoviePage);
        logout = findViewById(R.id.logoutBtn);
        welcomeText = findViewById(R.id.textView2);

        // Welcome the new user with their name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            welcomeText.setText("Welcome '" + name + "' to MovieFinder!");

        }


        logout.setOnClickListener((View view) -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });





        findMoviePageBtn.setOnClickListener((View view) -> startActivity(new Intent(getApplicationContext(), FindMovieActivity.class)));

    }
}