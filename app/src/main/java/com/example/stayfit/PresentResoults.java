package com.example.stayfit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;

public class PresentResoults extends AppCompatActivity implements AsyncResponse {
    RetrieveFeedTask asyncTask = new RetrieveFeedTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_resoults);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            try {
                asyncTask.delegate = this;
                asyncTask.execute("https://api.calorieninjas.com/v1/nutrition?query=" + query);

            } catch (Exception a) {
                System.out.println(a.toString());
            }


        }
    }

    @Override
    public void processFinish(String[] output) {

    }

}