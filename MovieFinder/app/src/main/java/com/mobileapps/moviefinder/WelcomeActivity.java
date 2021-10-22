package com.mobileapps.moviefinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppBarActivity {
    Button findMoviePageBtn;
    Button logout;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findMoviePageBtn = findViewById(R.id.findMoviePage);
        logout = findViewById(R.id.logoutBtn);
        welcomeText = (TextView) findViewById(R.id.textView2);

        // Welcome the new user with their name or email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            if(name == null || name.equals("")) {
                String email = user.getEmail();
                int i = email.indexOf('@');
                welcomeText.setText("Welcome '" + email.substring(0, i) + "' to MovieFinder!");

            } else {
                welcomeText.setText("Welcome '" + name + "' to MovieFinder!");
            }
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });





        findMoviePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FindMovieActivity.class));
            }
        });

    }
}