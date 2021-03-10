package com.example.stayfit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.ViewCompat;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.Locale;

public class FoodSearch extends AppCompatActivity implements AsyncResponse {
    RetrieveFeedTask asyncTask = new RetrieveFeedTask();
    TextView value;
    double calories = 0.0;
    double carbs = 0.0;
    double protein = 0.0;
    double fat = 0.0;
    double fiber = 0.0;
    double serving_size = 0.0;
    double serving_number = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            TextView found = findViewById(R.id.found);
            String query = intent.getStringExtra(SearchManager.QUERY);
            try {

                LinearLayout set = (LinearLayout) findViewById(R.id.search_history_layout);
                ProgressBar pr = new ProgressBar(this);
                pr.setId(R.id.progress_bar);
                set.addView(pr);




                if(isNetworkAvailable()) {
                    found.setText("Searching");
                    asyncTask.delegate = this;
                    asyncTask.execute("https://api.calorieninjas.com/v1/nutrition?query=" + query, query);
                }
                else{
                    found.setText("No internet connection");
                }

            }
            catch (Exception a){
                System.out.println(a.toString());
            }


        }


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = findViewById(R.id.search_widget);
        search.setSubmitButtonEnabled(true);
        SearchableInfo a = searchManager.getSearchableInfo(getComponentName());
        search.setIconifiedByDefault(false);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

    }

    @Override
    public boolean onSearchRequested(@Nullable SearchEvent searchEvent) {
        Bundle appData = new Bundle();
        appData.putBoolean("FoodSearch", true);
        startSearch(null, false, appData, false);
        return true;
    }

    @Override
    public void startSearch(@Nullable String initialQuery, boolean selectInitialQuery, @Nullable Bundle appSearchData, boolean globalSearch) {
        appSearchData.getBoolean("FoodSearch");
        Bundle appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
        super.startSearch(initialQuery, selectInitialQuery, appSearchData, globalSearch);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    public void processFinish(final String[] output) {

        if (output[0] == null) {
            try {
                LinearLayout set = (LinearLayout) findViewById(R.id.search_history_layout);
                ProgressBar prog = findViewById(R.id.progress_bar);
                set.removeView(prog);
                TextView found = findViewById(R.id.found);
                found.setText("No results found");

            }catch (Exception a){System.out.println(a);}

        }
        else{


            pbDbHelper dbHelper = new pbDbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            LinearLayout liner = (LinearLayout) findViewById(R.id.search_history_layout);
            TextView header = new TextView(this);


            try {

                JSONObject json = (JSONObject) new JSONTokener(output[0]).nextValue();

                JSONObject js = new JSONObject(output[0]);


                JSONArray arr2 = json.getJSONArray("items");


                if(arr2.length() != 0) {
                    for (int i = 0; i < arr2.length(); i++) {
                        int length = arr2.length();
                        JSONObject tr = arr2.getJSONObject(i);
                        calories += tr.getDouble("calories");
                        carbs += tr.getDouble("carbohydrates_total_g");
                        protein += tr.getDouble("protein_g");
                        fat += tr.getDouble("fat_total_g");
                        fiber += tr.getDouble("fiber_g");
                        serving_size += tr.getDouble("serving_size_g");
                    }

                    ProgressBar prog = findViewById(R.id.progress_bar);
                    TextView resoults = new TextView(this);
                    LinearLayout drugi = new LinearLayout(this);
                    liner.addView(drugi);
                    drugi.setGravity(Gravity.CENTER);

                    // info prvi stolpec
                    LinearLayout tretjiE = new LinearLayout(this);
                    tretjiE.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tretjiE.setOrientation(LinearLayout.VERTICAL);
                    tretjiE.setPadding(20, 20, 20, 20);
                    //tretjiE.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
                    TextView ime = new TextView(this);
                    value = new TextView(this);
                    value.setTextSize(30);
                    ime.setText("cal");
                    ime.setTypeface(null, Typeface.BOLD);
                    ime.setGravity(Gravity.CENTER);
                    value.setText(String.format("%.2f", calories * serving_number) + " g");
                    tretjiE.addView(value);
                    tretjiE.addView(ime);
                    drugi.addView(tretjiE);

                    // info drugi stolpec
                    LinearLayout tretjiD = new LinearLayout(this);
                    tretjiD.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tretjiD.setOrientation(LinearLayout.VERTICAL);
                    tretjiD.setPadding(20, 20, 20, 20);
                    TextView imeD = new TextView(this);
                    final TextView valueD = new TextView(this);
                    valueD.setId(R.id.fat_TextView);
                    valueD.setTextSize(30);
                    imeD.setText("fat");
                    imeD.setTypeface(null, Typeface.BOLD);
                    imeD.setGravity(Gravity.CENTER);
                    valueD.setText(String.format("%.2f", fat * serving_number) + " g");
                    tretjiD.addView(valueD);
                    tretjiD.addView(imeD);
                    drugi.addView(tretjiD);

                    // info tretji stolpec
                    LinearLayout tretjiT = new LinearLayout(this);
                    tretjiT.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tretjiT.setOrientation(LinearLayout.VERTICAL);
                    tretjiT.setPadding(20, 20, 20, 20);
                    TextView imeT = new TextView(this);
                    final TextView valueT = new TextView(this);
                    valueT.setTextSize(30);
                    imeT.setText("carbs");
                    imeT.setTypeface(null, Typeface.BOLD);
                    imeT.setGravity(Gravity.CENTER);
                    valueT.setText(String.format("%.2f", carbs * serving_number) +" g");
                    tretjiT.addView(valueT);
                    tretjiT.addView(imeT);
                    drugi.addView(tretjiT);


                    // protein
                    LinearLayout cetrtiT = new LinearLayout(this);
                    TextView prot = new TextView(this);
                    prot.setText("Protein: ");
                    prot.setTypeface(null, Typeface.BOLD);
                    cetrtiT.addView(prot);
                    final TextView prot2 = new TextView(this);
                    prot2.setText(String.format("%.2f", protein * serving_number) + " g");
                    prot2.setId(R.id.protein_textView);
                    cetrtiT.addView(prot2);
                    liner.addView(cetrtiT);




                    // info number of servings
                    LinearLayout vert = new LinearLayout(this);
                    TextView numeberT = new TextView(this);
                    numeberT.setText("Number of servings: ");
                    vert.addView(numeberT);

                    EditText number = new EditText(this);
                    number.setId(R.id.number);
                    number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    number.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    number.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                    number.setText(Double.toString(serving_number));
                    number.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (!editable.toString().equals("")) {

                                serving_number = Double.parseDouble(editable.toString());
                                value.setText(String.format("%.2f", calories * serving_number) + " g");
                                TextView test = (TextView) findViewById(R.id.fat_TextView);
                                test.setText(String.format("%.2f", fat * serving_number) + " g");
                                valueT.setText(String.format("%.2f", carbs * serving_number) +" g");
                                prot2.setText(String.format("%.2f", protein * serving_number) + " g");
                            }

                        }
                    });
                    vert.addView(number);
                    liner.addView(vert);


                    // info serving size
                    TextView servingSize = new TextView(this);
                    servingSize.setText("Serving size: " + Double.toString(serving_size));
                    liner.addView(servingSize);

                    //servingSize.setTe
                    // info gumb dodaj
                    Button a = new Button(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER;
                    params.setMargins(0, 30, 0, 0);
                    a.setLayoutParams(params);
                    a.setText("ADD FOOD");

                    //a.setPadding(0, 50, 0, 0);
                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //info dodaj v pb
                            pbDbHelper dbHelper = new pbDbHelper(view.getContext());
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();

                            values.put(pbContract.FoodHistory.NAME, output[1]);
                            values.put(pbContract.FoodHistory.MEAL, "1");
                            Calendar.getInstance().getTime();
                            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                            values.put(pbContract.FoodHistory.DATE, date);
                            values.put(pbContract.FoodHistory.SERVING_SIZE, serving_size);
                            values.put(pbContract.FoodHistory.SERVING_NUMBER, serving_number);
                            values.put(pbContract.FoodHistory.CALORIES, calories);
                            values.put(pbContract.FoodHistory.TOTAL_FAT, fat);
                            values.put(pbContract.FoodHistory.SATURATED_FAT, 0);
                            values.put(pbContract.FoodHistory.CHOLESTEROL, 0);
                            values.put(pbContract.FoodHistory.SODIUM, 0);
                            values.put(pbContract.FoodHistory.CARBOHYDRATES, carbs);
                            values.put(pbContract.FoodHistory.FIBER, fiber);
                            values.put(pbContract.FoodHistory.SUGAR, 0);
                            values.put(pbContract.FoodHistory.PROTEIN, protein);


                            db.insert(pbContract.FoodHistory.TABLE_NAME, null, values);

                            //info preglej ce je v bazi
                            SQLiteDatabase db2 = dbHelper.getReadableDatabase();
                            String[] projection = {
                                    pbContract.FoodHistory.NAME,
                                    pbContract.FoodHistory.MEAL,
                                    pbContract.FoodHistory.DATE,
                                    pbContract.FoodHistory.SERVING_SIZE,
                                    pbContract.FoodHistory.SERVING_NUMBER,
                                    pbContract.FoodHistory.CALORIES,
                                    pbContract.FoodHistory.TOTAL_FAT,
                                    pbContract.FoodHistory.SATURATED_FAT,
                                    pbContract.FoodHistory.CHOLESTEROL,
                                    pbContract.FoodHistory.SODIUM,
                                    pbContract.FoodHistory.CARBOHYDRATES,
                                    pbContract.FoodHistory.FIBER,
                                    pbContract.FoodHistory.SUGAR,
                                    pbContract.FoodHistory.PROTEIN
                            };

                            Cursor cursor = db2.query(pbContract.FoodHistory.TABLE_NAME, projection, null, null, null, null, null);


                            //info pridobi datum, preveri ce je ze v tabeli avrage, ce ni vstavi, ce je popravi
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String date2 = df.format(Calendar.getInstance().getTime());


                            Cursor c = db2.rawQuery("SELECT " + pbContract.FoodAvrage.CALORIES + ", " +pbContract.FoodAvrage.FAT
                                    + ", " + pbContract.FoodAvrage.CARBS + ", " + pbContract.FoodAvrage.PROTEIN + " FROM "
                                    + pbContract.FoodAvrage.TABLE_NAME + " WHERE " + pbContract.FoodAvrage.DATE + " = "
                                    + "\"" + date2 + "\"" + ";", null);

                            if (!c.moveToFirst()) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(pbContract.FoodAvrage.DATE, date2);
                                contentValues.put(pbContract.FoodAvrage.CALORIES, calories * serving_number);
                                contentValues.put(pbContract.FoodAvrage.FAT, fat * serving_number);
                                contentValues.put(pbContract.FoodAvrage.CARBS, carbs * serving_number);
                                contentValues.put(pbContract.FoodAvrage.PROTEIN, protein * serving_number);
                                db.insert(pbContract.FoodAvrage.TABLE_NAME, null, contentValues);
                            } else {
                                double cal = Double.parseDouble(c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.CALORIES)));
                                double fa = Double.parseDouble((c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.FAT))));
                                double car = Double.parseDouble((c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.CARBS))));
                                double pr = Double.parseDouble((c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.PROTEIN))));

                                cal += calories * serving_number;
                                fa += fat *serving_number;
                                car += carbs * serving_number;
                                pr += protein * serving_number;


                                ContentValues contentValues = new ContentValues();
                                //contentValues.put(pbContract.FoodAvrage.DATE, date2);
                                contentValues.put(pbContract.FoodAvrage.CALORIES, cal);
                                contentValues.put(pbContract.FoodAvrage.FAT, fa);
                                contentValues.put(pbContract.FoodAvrage.CARBS, car);
                                contentValues.put(pbContract.FoodAvrage.PROTEIN, pr);

                                db.update(pbContract.FoodAvrage.TABLE_NAME, contentValues, pbContract.FoodAvrage.DATE + " = ?", new String[]{date2});
                            }


                            // info alert window, dodana hrana

                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Entry added")
                                    .setMessage("Yor entry has been successfully added.")

                                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })

                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();



                        }
                    });



                    liner.addView(a);

                    liner.removeView(prog);
                    String fk = "No results found";
                    TextView found = findViewById(R.id.found);
                    found.setText(output[1]);
                    found.setTextSize(40);



                }
                else{
                    LinearLayout set = (LinearLayout) findViewById(R.id.search_history_layout);
                    ProgressBar prog = findViewById(R.id.progress_bar);
                    set.removeView(prog);
                    TextView found = findViewById(R.id.found);
                    found.setText("No results found for: " + output[1]);
                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}