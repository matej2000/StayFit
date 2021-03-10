package com.example.stayfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String gender1 = "";
        String birthday1 = "Birthday";
        String height1 = "";
        String weight1 = "";
        String activityLevel1 = "";
        String goals1 = "";

        final Bundle extras = getIntent().getExtras();
        TextView settings = (TextView) findViewById(R.id.settings);

        if (extras != null) {
            settings.setText(R.string.settings);

            pbDbHelper dbHelper = new pbDbHelper(this);
            SQLiteDatabase db2 = dbHelper.getReadableDatabase();
            String[] projection = {
                    pbContract.User.WEIGHT,
                    pbContract.User.HEIGHT,
                    pbContract.User.BIRTHDAY,
                    pbContract.User.ACTIVITY,
                    pbContract.User.GENDER,
                    pbContract.User.GOAL
            };



            Cursor cursor = db2.query(pbContract.User.TABLE_NAME, projection, null, null, null, null,null);
            while(cursor.moveToNext()){
                EditText we = (EditText) findViewById(R.id.weight);
                we.setText(cursor.getString(cursor.getColumnIndexOrThrow(pbContract.User.WEIGHT)));

                EditText he = (EditText) findViewById(R.id.height);
                he.setText(cursor.getString(cursor.getColumnIndexOrThrow(pbContract.User.HEIGHT)));

                gender1 = cursor.getString(cursor.getColumnIndexOrThrow(pbContract.User.GENDER));
                RadioGroup group = (RadioGroup) findViewById(R.id.gender);

                if(gender1.equals("Male")){
                    group.check(R.id.male);
                }
                else{group.check(R.id.female);}

                activityLevel1 = cursor.getString(cursor.getColumnIndexOrThrow(pbContract.User.ACTIVITY));
                goals1 = cursor.getString(cursor.getColumnIndexOrThrow(pbContract.User.GOAL));
                birthday1 = cursor.getString(cursor.getColumnIndexOrThrow(pbContract.User.BIRTHDAY));
            }

        }
        else{
            settings.setText(R.string.welcoe);
        }

        settings.setTextSize(30);



        final Calendar myCalendar = Calendar.getInstance();
        final EditText edittext= (EditText) findViewById(R.id.Birthday);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, edittext);
            }

        };
        edittext.setText(birthday1);


        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Spinner activity = findViewById(R.id.activity);
        String[] activityLevel = {
                "Little or no exercise",
                "Light: exercise 1-3 times/week",
                "Moderate: exercise 4-5 times/week",
                "Active: daily exercise or intense exercise 3-4 times/week",
                "Very active: intense exercise daily, or physical job",
        };



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activityLevel);
        activity.setAdapter(adapter);

        if(activityLevel1.equals("")) {
            int match = 0;

            for (int i = 0; i < activityLevel.length; i++) {
                if (activityLevel1.equals(activityLevel[i])) {
                    match = i;
                    break;
                }
            }
            activity.setSelection(match);
        }


        Spinner goals = findViewById(R.id.goal);
        String [] allGoals = {
                "Maintain weight",
                "Mild weight loss (0.25 kg/week)",
                "Weight loss (0.5 kg/week)",
                "Mild weigh gain (0.25 kg/week)",
                "Weigh gain (0,5 kg/week)"
        };
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allGoals);
        goals.setAdapter(adapter2);

        if(goals1.equals("")) {
            int match = 0;

            for (int i = 0; i < allGoals.length; i++) {
                if (goals1.equals(allGoals[i])) {
                    match = i;
                    break;
                }
            }
            goals.setSelection(match);
        }

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(extras != null){
                    pbDbHelper dbHelper = new pbDbHelper(view.getContext());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    db.rawQuery("DELETE FROM " + pbContract.User.TABLE_NAME + " WHERE " + pbContract.User.ID + " IN ( SELECT " + pbContract.User.ID + " FROM " + pbContract.User.TABLE_NAME + " LIMIT 1);", null);
                    
                }


                boolean correct_values = true;
                EditText birthday= (EditText) findViewById(R.id.Birthday);
                EditText weight = (EditText) findViewById(R.id.weight);
                EditText height= (EditText) findViewById(R.id.height);
                Spinner activity = findViewById(R.id.activity);
                RadioGroup gender = findViewById(R.id.gender);
                Spinner goals = findViewById(R.id.goal);
                RadioButton selected_gender = findViewById(gender.getCheckedRadioButtonId());
                if(selected_gender == null){
                    correct_values = false;
                }


                String activityText = activity.getSelectedItem().toString();
                String goalsText = goals.getSelectedItem().toString();
                //String usernameText = username.getText().toString();
                String birthdayText = birthday.getText().toString();
                String weightText = weight.getText().toString();
                String heightText = height.getText().toString();
                String genderText = "";
                if(selected_gender != null) {
                    genderText = selected_gender.getText().toString();
                }
                if(activityText.equals("") ){correct_values = false;}
                //else if(usernameText.equals("")){correct_values = false;}
                else if(birthdayText.equals("")){correct_values = false;}
                else if(weightText.equals("")){correct_values = false;}
                else if(heightText.equals("")){correct_values = false;}
                else if(genderText.equals("")){correct_values = false;}
                else if(goalsText.equals("")){correct_values = false;}

                if(!correct_values){

                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Incorrect values")
                            .setMessage("One or more values are not set.")
                            .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else{


                    // info za vpis v pb
                    pbDbHelper dbHelper = new pbDbHelper(view.getContext());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(pbContract.User.WEIGHT, Integer.parseInt(weightText));
                    values.put(pbContract.User.HEIGHT, Integer.parseInt(heightText));
                    values.put(pbContract.User.BIRTHDAY, birthdayText);
                    values.put(pbContract.User.ACTIVITY, activityText);
                    values.put(pbContract.User.GENDER, genderText);
                    values.put(pbContract.User.GOAL, goalsText);

                    db.insert(pbContract.User.TABLE_NAME, null, values);



                }
                if(correct_values) {
                    Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(myIntent);
                }
                SharedPreferences res = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor edit = res.edit();
                edit.putBoolean(getString(R.string.first_startup), Boolean.TRUE);
                edit.commit();

                //info izracun parametrov
                double calories = 0;
                double rmr = 0;
                int wei = Integer.parseInt(weightText);
                double hi = Integer.parseInt(heightText);
                double pal = 1.2;

                Calendar dod = Calendar.getInstance();
                Calendar today = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date2 = df.format(Calendar.getInstance().getTime());
                String[] deli2 = date2.split("/");
                String[] deli = birthdayText.split("/");
                int year2 = Integer.parseInt(deli2[2]);
                int month2 = Integer.parseInt(deli2[1]);
                int day2 = Integer.parseInt(deli2[0]);

                int year = Integer.parseInt(deli[2]);
                int month = Integer.parseInt(deli[1]);
                int day = Integer.parseInt(deli[0]);

                dod.set(year, month, day);

                int danes = today.get(Calendar.YEAR);
                int prej = dod.get(Calendar.YEAR);
                int age = today.get(Calendar.YEAR) - dod.get(Calendar.YEAR);

                int age2 = year2 - year;
                if(month2 <= month && day2 < day){
                    age--;
                }

                /*if(today.get(Calendar.DAY_OF_YEAR) < dod.get(Calendar.DAY_OF_YEAR)){
                    age--;
                }*/



                if (genderText.equals("Male")) {
                    rmr = 66.47 + (13.75 * wei) + (5.03 * hi) - (6.75 * age2);
                } else {
                    rmr = 655.10 + (9.56 * wei) + (1.85 * hi) - (4.68 * age2);
                }

                switch (activityText){
                    case "Little or no exercise": pal = 1.2; break;
                    case "Light: exercise 1-3 times/week": pal = 1.45; break;
                    case "Moderate: exercise 4-5 times/week": pal = 1.65; break;
                    case "Active: daily exercise or intense exercise 3-4 times/week": pal = 1.85; break;
                    case "Very active: intense exercise daily, or physical job": pal = 2.2; break;
                }

                rmr *= pal;

                switch (goalsText){
                    case "Mild weight loss (0.25 kg/week)": rmr = rmr * 0.9; break;
                    case "Weight loss (0.5 kg/week)": rmr = rmr * 0.81; break;
                    case "Mild weigh gain (0.25 kg/week)": rmr += rmr * 0.1; break;
                    case "Weigh gain (0,5 kg/week)": rmr += rmr * 0.19; break;
                }

               rmr = round(rmr, 2);

                double fat = (rmr * 0.25) / 9;
                fat = round(fat, 2);
                double carbs = (rmr * 0.6) / 4;
                carbs = round(carbs, 2);
                double protein = (rmr * 0.15) / 4;
                protein = round(protein, 2);


                //info shrani kalorije za posameznika v shared preferences
                //SharedPreferences sharedPref = view.getContext().getSharedPreferences(getString(R.string.preference_file_key), view.getContext().MODE_PRIVATE);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putLong(getString(R.string.user_calories), Double.doubleToRawLongBits(rmr));
                editor.putString(getString(R.string.user_calories), Double.toString(rmr));
                editor.putString(getString(R.string.user_fat), Double.toString(fat));
                editor.putString(getString(R.string.user_carbs), Double.toString(carbs));
                editor.putString(getString(R.string.user_protein), Double.toString(protein));
                // info za brat Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
                editor.apply();

                finish();
            }
        });


    }

    private void updateLabel(Calendar myCalendar, EditText edittext) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



}