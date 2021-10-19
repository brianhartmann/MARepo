package com.mobileapps.moviefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

/* Modeled with help from Adam Champion's GameOptionsFragment from his TicTacToe application*/

public class AppBarActivity extends AppCompatActivity {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.account) {
            startActivity(new Intent(getApplicationContext(), AccountInfoActivity.class));
            return true;
        } else if (itemId == R.id.previouslyWatched) {
           // startActivity(new Intent(getApplicationContext(), PreviouslyWatched.class));
            return true;
        } else if (itemId == R.id.watchLater) {
            //startActivity(new Intent(getApplicationContext(), WatchLater.class));
            return true;
        } else if (itemId == R.id.about) {
            //startActivity(new Intent(getApplicationContext(), AboutSettings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}