package com.example.stayfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState7
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Added_items.class);
                startActivity(myIntent);
                /*NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);*/
            }
        });

        pbDbHelper dbHelper = new pbDbHelper(view.getContext());

        SQLiteDatabase db2 = dbHelper.getReadableDatabase();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date2 = df.format(Calendar.getInstance().getTime());

        Cursor c = db2.rawQuery("SELECT " + pbContract.FoodAvrage.CALORIES + ", " +pbContract.FoodAvrage.FAT
                + ", " + pbContract.FoodAvrage.CARBS + ", " + pbContract.FoodAvrage.PROTEIN + " FROM "
                + pbContract.FoodAvrage.TABLE_NAME + " WHERE " + pbContract.FoodAvrage.DATE + " = "
                + "\"" + date2 + "\"" + ";", null);

        String calorie2 = "0";
        String fat2 = "0";
        String carbs2 = "0";
        String protein2 = "0";

        if(c.moveToFirst()){
            calorie2 = c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.CALORIES));
            fat2 = c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.FAT));
            carbs2 = c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.CARBS));
            protein2 = c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.PROTEIN));
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String calorie = sharedPref.getString(getString(R.string.user_calories), "0");
        String fat = sharedPref.getString(getString(R.string.user_fat), "0");
        String carbs = sharedPref.getString(getString(R.string.user_carbs), "0");
        String protein = sharedPref.getString(getString(R.string.user_protein), "0");

        TextView cal = (TextView) getView().findViewById(R.id.textview_first);
        cal.setText(calorie2 + " / " + calorie);

        TextView fa = (TextView) getView().findViewById(R.id.textview_fat);
        cal.setText(fat2 + " / " + fat);

        TextView car = (TextView) getView().findViewById(R.id.textview_carbs);
        cal.setText(carbs2 + " / " + carbs);

        TextView pro = (TextView) getView().findViewById(R.id.textview_protein);
        cal.setText(protein2 + " / " + protein);




    }


    @Override
    public void onResume() {
        pbDbHelper dbHelper = new pbDbHelper(this.getContext());

        SQLiteDatabase db2 = dbHelper.getReadableDatabase();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date2 = df.format(Calendar.getInstance().getTime());

        Cursor c = db2.rawQuery("SELECT " + pbContract.FoodAvrage.CALORIES + ", " +pbContract.FoodAvrage.FAT
                + ", " + pbContract.FoodAvrage.CARBS + ", " + pbContract.FoodAvrage.PROTEIN + " FROM "
                + pbContract.FoodAvrage.TABLE_NAME + " WHERE " + pbContract.FoodAvrage.DATE + " = "
                + "\"" + date2 + "\"" + ";", null);

        String calorie2 = "0";
        String fat2 = "0";
        String carbs2 = "0";
        String protein2 = "0";

        if(c.moveToFirst()){
            calorie2 = c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.CALORIES));
            fat2 = c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.FAT));
            carbs2 = c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.CARBS));
            protein2 = c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.PROTEIN));
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String calorie = sharedPref.getString(getString(R.string.user_calories), "0");
        String fat = sharedPref.getString(getString(R.string.user_fat), "0");
        String carbs = sharedPref.getString(getString(R.string.user_carbs), "0");
        String protein = sharedPref.getString(getString(R.string.user_protein), "0");

        TextView cal = (TextView) getView().findViewById(R.id.textview_first);
        cal.setText(calorie2 + " / " + calorie + " cal");

        TextView fa = (TextView) getView().findViewById(R.id.textview_fat);
        fa.setText(fat2 + " / " + fat + " g");

        TextView car = (TextView) getView().findViewById(R.id.textview_carbs);
        car.setText(carbs2 + " / " + carbs + " g");

        TextView pro = (TextView) getView().findViewById(R.id.textview_protein);
        pro.setText(protein2 + " / " + protein + " g");

        super.onResume();
    }
}