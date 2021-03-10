package com.example.stayfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences res = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = res.edit();
        //edit.putBoolean(getString(R.string.first_startup), Boolean.TRUE);
        //edit.commit();
        boolean startup = res.getBoolean(getString(R.string.first_startup), false);

        if(!startup){
            super.onCreate(savedInstanceState);

            Intent myIntent2 = new Intent(this, register.class);
            startActivity(myIntent2);



        }
        else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), FoodSearch.class);
                    startActivity(myIntent);
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(this, register.class);
            myIntent.putExtra("type", "settings");
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   @Override
    protected void onResume() {
        SharedPreferences res = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean startup = res.getBoolean(getString(R.string.first_startup), false);
        if(startup){
            super.onResume();
        }
        else {
            super.onResume();
            finish();
        }
    }
}