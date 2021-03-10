package com.example.stayfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Added_items extends AppCompatActivity {
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_items);


        final EditText edittext = (EditText) findViewById(R.id.date);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date2 = df.format(Calendar.getInstance().getTime());

        edittext.setText(date2);

        final Calendar myCalendar = Calendar.getInstance();
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

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Added_items.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createItems(getBaseContext(), editable.toString());
            }
        });

        createItems(this, date2);


    }

    private void updateLabel(Calendar myCalendar, EditText edittext) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    private void createItems(final Context context, final String date){


        pbDbHelper dbHelper = new pbDbHelper(context);
        SQLiteDatabase db2 = dbHelper.getReadableDatabase();
        String[] projection = {
                pbContract.FoodHistory.ID,
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


        Cursor cursor = db2.query(pbContract.FoodHistory.TABLE_NAME, projection, pbContract.FoodHistory.DATE + "=?", new String[]{date}, null, null, null);
        TextView[] itemText = new TextView[cursor.getCount()];
        final LinearLayout[] layoutText = new LinearLayout[cursor.getCount()];
        TextView[] delete = new TextView[cursor.getCount()];
        final String[] foodID = new String[cursor.getCount()];
        final String[] foodName = new String[cursor.getCount()];
        final String[] foodCalories = new String[cursor.getCount()];
        final String[] foodFat =  new String[cursor.getCount()];
        final String[] foodCarbs =  new String[cursor.getCount()];
        final String[] foodProtein =  new String[cursor.getCount()];
        final double[] foodNumber = new double[cursor.getCount()];

        final String[] izpis = new String[cursor.getCount()];

        i= 0;
        if(cursor.moveToFirst()) {
           do {
                foodID[i] = cursor.getString(cursor.getColumnIndexOrThrow(pbContract.FoodHistory.ID));
                foodName[i] = cursor.getString(cursor.getColumnIndexOrThrow(pbContract.FoodHistory.NAME));
                foodNumber[i] = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(pbContract.FoodHistory.SERVING_NUMBER)));

                //info podatki po posameznih tabelah
                foodCalories[i] =  Double.toString(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(pbContract.FoodHistory.CALORIES))) * foodNumber[i]);
                foodFat[i] =  Double.toString(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(pbContract.FoodHistory.TOTAL_FAT))) * foodNumber[i]);
                foodCarbs[i] =  Double.toString(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(pbContract.FoodHistory.CARBOHYDRATES))) * foodNumber[i]);
                foodProtein[i] = Double.toString(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(pbContract.FoodHistory.PROTEIN))) * foodNumber[i]);



               izpis[i] = "Calories: " + foodCalories[i] + " cal\n" +
                       "Fat: " + foodFat[i] + " g\n" +
                       "Carbs: " + foodCarbs[i] + " g\n" +
                       "Protein: " + foodProtein[i] + " g";

               FrameLayout layout_MainMenu = (FrameLayout) findViewById( R.id.mainmenu);
               layout_MainMenu.getForeground().setAlpha( 0);



                layoutText[i] = new LinearLayout(this);
                layoutText[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layoutText[i].setOrientation(LinearLayout.HORIZONTAL);
                layoutText[i].setClickable(true);
                layoutText[i].setTag(i);
                layoutText[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LinearLayout linearLayout = (LinearLayout) view;
                        int index = (int) linearLayout.getTag();


                        LayoutInflater inflater = (LayoutInflater)
                                getSystemService(LAYOUT_INFLATER_SERVICE);
                        final View popupView = inflater.inflate(R.layout.popup_window, null);

                        // create the popup window
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true; // lets taps outside the popup also dismiss it
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        TextView popup = (TextView) popupView.findViewById(R.id.popup);
                        popup.setText(foodName[index] );

                        TextView dataPopup = (TextView) popupView.findViewById(R.id.other_data) ;
                        dataPopup.setPadding(30, 0, 30, 0);
                        dataPopup.setText(izpis[index]);

                        final Button delete = (Button) popupView.findViewById(R.id.delete);
                        delete.setTag(index);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                deleteEntery(view.getContext(), foodID[(int)(view.getTag())], foodCalories[(int)(view.getTag())], foodFat[(int)(view.getTag())], foodCarbs[(int)(view.getTag())], foodProtein[(int)(view.getTag())]);
                                popupWindow.dismiss();
                                createItems(view.getContext(), date);
                            }
                        });

                        final FrameLayout layout_MainMenu = (FrameLayout) findViewById( R.id.mainmenu);
                        layout_MainMenu.setAlpha(1);
                        layout_MainMenu.setBackgroundColor(ContextCompat.getColor(context, R.color.moja));
                        layout_MainMenu.getForeground().setAlpha(255);
                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                layout_MainMenu.setBackgroundColor(Color.WHITE);
                            }
                        });


                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);



                        popupView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                popupWindow.dismiss();

                                return true;
                            }
                        });
                    }
                });

                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
                layoutText[i].setBackgroundResource(typedValue.resourceId);


                itemText[i] = new TextView(context);
                itemText[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                itemText[i].setPadding( 30, 40, 0, 0);

                String text = cursor.getString(cursor.getColumnIndexOrThrow(pbContract.FoodHistory.NAME));
                itemText[i].setText(text);
                itemText[i].setTextSize(40);
                layoutText[i].addView(itemText[i]);

               TextView cal = new TextView(context);
               cal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
               cal.setText(foodCalories[i] + " cal");
               cal.setGravity(Gravity.RIGHT);
               cal.setPadding(10, 0, 30, 0);

               layoutText[i].addView(cal);

                delete[i] = new TextView(this);
                delete[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

                i++;
            }
           while (cursor.moveToNext());
        }
        else {
            TextView a = new TextView(this);
            a.setText("No enteries");
            a.setGravity(Gravity.CENTER);
            LinearLayout polje = (LinearLayout) findViewById(R.id.items);
            polje.addView(a);

        }


        LinearLayout polje = (LinearLayout) findViewById(R.id.items);
        polje.removeAllViews();
        for(int k = 0; k < cursor.getCount(); k++){
            polje.addView(layoutText[k]);
            LinearLayout div = new LinearLayout(context);
            div.setPadding(0, 10, 0, 10);
            polje.addView(div);
        }


    }


    private void deleteEntery(Context context, String id, String calories, String fat, String carbs, String protein){

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date2 = df.format(Calendar.getInstance().getTime());

        pbDbHelper dbHelper = new pbDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //info izbrisi iz tabele foodHistory

        String query = "DELETE FROM " + pbContract.FoodHistory.TABLE_NAME + " WHERE " + pbContract.FoodHistory.ID + " = " + id;
        db.execSQL("DELETE FROM " + pbContract.FoodHistory.TABLE_NAME + " WHERE " + pbContract.FoodHistory.ID + " = ?; ", new String[]{id});

        //info posodobi tabelo avrage
        Cursor c = db.rawQuery("SELECT " + pbContract.FoodAvrage.CALORIES + ", " +pbContract.FoodAvrage.FAT
                + ", " + pbContract.FoodAvrage.CARBS + ", " + pbContract.FoodAvrage.PROTEIN + " FROM "
                + pbContract.FoodAvrage.TABLE_NAME + " WHERE " + pbContract.FoodAvrage.DATE + " = "
                + "\"" + date2 + "\"" + ";", null);

        if(c.moveToFirst()) {
            double cal = Double.parseDouble(c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.CALORIES)));
            double fa = Double.parseDouble((c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.FAT))));
            double car = Double.parseDouble((c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.CARBS))));
            double pr = Double.parseDouble((c.getString(c.getColumnIndexOrThrow(pbContract.FoodAvrage.PROTEIN))));

            cal -= Double.parseDouble(calories);
            fa -= Double.parseDouble(fat);
            car -= Double.parseDouble(carbs);
            pr -= Double.parseDouble(protein);

            ContentValues contentValues = new ContentValues();
            contentValues.put(pbContract.FoodAvrage.CALORIES, cal);
            contentValues.put(pbContract.FoodAvrage.FAT, fa);
            contentValues.put(pbContract.FoodAvrage.CARBS, car);
            contentValues.put(pbContract.FoodAvrage.PROTEIN, pr);

            db.update(pbContract.FoodAvrage.TABLE_NAME, contentValues, pbContract.FoodAvrage.DATE + " = ?", new String[]{date2});

        }



    }
}