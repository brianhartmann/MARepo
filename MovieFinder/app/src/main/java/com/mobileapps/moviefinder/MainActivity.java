package com.mobileapps.moviefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button mRegisterPage;
    Button mLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Only allow vertical/portrait screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRegisterPage = findViewById(R.id.registerPage);
        mLoginPage = findViewById(R.id.loginPage);

        mRegisterPage.setOnClickListener((View view) -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));

        mLoginPage.setOnClickListener((View view) -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));


    }
}