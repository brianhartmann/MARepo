package com.mobileapps.moviefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

/* Modeled with help from Adam Champion's GameOptionsFragment from his TicTacToe application*/

public class AppBarActivity extends AppCompatActivity {
    private static final String TAG = "AppBarActivity";
    Toolbar appToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);

        appToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appToolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_menu, menu);
        super.onCreateOptionsMenu(menu);

        return true;
    }

    public void accountInfoSelected(MenuItem item) {
        Toast.makeText(this, "Account Info is selected", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), AccountInfoActivity.class));
    }

    public void previouslyWatchedSelected(MenuItem item) {
        Toast.makeText(this, "Previously Watched is selected", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(getApplicationContext(), PreviouslyWatchedActivity.class));
    }

    public void watchLaterSelected(MenuItem item) {
        Toast.makeText(this, "Watch Later is selected", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(getApplicationContext(), WatchLaterActivity.class));
    }

    public void aboutSettingsSelected(MenuItem item) {
        Toast.makeText(this, "About/Settings is selected", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
    }
}