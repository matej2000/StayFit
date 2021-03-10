package com.example.stayfit;

import android.provider.BaseColumns;

public final class pbContract {

    private pbContract(){}

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + User.TABLE_NAME + " (" +
            User.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            User.WEIGHT + " INTEGER, " +
            User.HEIGHT + " INTEGER, " +
            User.BIRTHDAY + " TEXT, " +
            User.ACTIVITY + " TEXT, " +
            User.GENDER + " TEXT, " +
            User.GOAL + " TEXT)";

    /*public static final String SQL_CREATE_ENTRIES_WEIGHT = "CREATE TABLE " + Weight.TABLE_NAME + " (" +
            Weight.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Weight.WEIGHT + " INTEGER)";

    public static final String SQL_CREATE_ENTERIES_FOOD = "CREATE TABLE " +Food.TABLE_NAME + " (" +
            Food.ID + " INTEGER PRIMARY KEY AUTOINCREMENT)";*/

    public static final String SQL_CREATE_ENTRIES_FOOD = "CREATE TABLE " + FoodHistory.TABLE_NAME + " (" +
            FoodHistory.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FoodHistory.NAME + " TEXT, " +
            FoodHistory.DATE + " TEXT, " +
            FoodHistory.MEAL + " TEXT, " +
            FoodHistory.SERVING_SIZE + " TEXT, " +
            FoodHistory.SERVING_NUMBER + " TEXT, " +
            FoodHistory.CALORIES + " TEXT, " +
            FoodHistory.TOTAL_FAT + " TEXT, " +
            FoodHistory.SATURATED_FAT + " TEXT, " +
            FoodHistory.CHOLESTEROL + " TEXT, " +
            FoodHistory.SODIUM + " TEXT, " +
            FoodHistory.CARBOHYDRATES + " TEXT, " +
            FoodHistory.FIBER + " TEXT, " +
            FoodHistory.SUGAR + " TEXT, " +
            FoodHistory.PROTEIN + " TEXT)" ;

    /*public static final String SQL_CREATE_ENTRIES_USER_FOOD = "CREATE TABLE " + FoodUser.TABLE_NAME + " (" +
            FoodUser.ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FoodUser.ID2 + "INTEGER, " +
            "FOREIGN KEY(" + FoodUser.ID2 + ") REFERENCES " + FoodHistory.TABLE_NAME + "(" + FoodHistory.ID + ") );";*/


    public static final String SQL_CREATE_ENTRIES_FOOD_AVRAGE = "CREATE TABLE " + FoodAvrage.TABLE_NAME + " (" +
            FoodAvrage.DATE + " TEXT PRIMARY KEY, " +
            FoodAvrage.CALORIES + " REAL, " +
            FoodAvrage.FAT + " REAL, " +
            FoodAvrage.CARBS + " REAL, " +
            FoodAvrage.PROTEIN + " REAL) ";

    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + User.TABLE_NAME;

    public static String selectQuery = "SELECT * FROM " + FoodHistory.TABLE_NAME;


    protected static final String[] activity = {""};

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String ID = "Id";
        //public static final String COLUMN_NAME_username = "Username";
        public static final String GENDER = "Geder";
        public static final String WEIGHT = "Weight";
        public static final String HEIGHT = "Height";
        public static final String BIRTHDAY = "BirthDay";
        public static final String ACTIVITY = "Activity";
        public static final String GOAL = "Goal";
    }

    /*public static class Weight implements BaseColumns {
        public static final String TABLE_NAME = "Weight";
        public static final String ID = "Id_weight";
        public static final String WEIGHT = "Weight";
    }*/

    /*public static class FoodEaten implements BaseColumns {
        public static final String TABLE_NEME = "FoodEaten";
        public static final String ID = "Id_food";
        public static final String DATE = "Date";
        public static final String MEAL = "Meal";
    }*/

    /*public static class Food implements BaseColumns {
        public static final String TABLE_NAME = "Food";
        public static final String ID = "Id_food";
        public static final String NAME = "Name";
        public static final String PORTION = "Portion_default";
        public static final String PORTION_AMOUNT = "Portion_amount";
        public static final String PORTION_DISPLAY = "Portion_display";
        public static final String CALORIES = "Calories";
        
    }*/

    public static class FoodHistory implements BaseColumns {
        public static final String TABLE_NAME = "FoodHistory";
        public static final String ID = "Id_food";
        public static final String DATE = "Date";
        public static final String MEAL = "Meal";
        public static final String NAME = "Name";
        public static final String SERVING_SIZE = "Serving_size";
        public static final String SERVING_NUMBER = "Serving_number";
        public static final String CALORIES = "Calorie";
        public static final String TOTAL_FAT = "Total_fat";
        public static final String SATURATED_FAT = "Saturated_fat";
        public static final String CHOLESTEROL = "Cholesterol";
        public static final String SODIUM = "Sodium";
        public static final String CARBOHYDRATES = "Carbohydrates";
        public static final String FIBER = "Fiber";
        public static final String SUGAR = "Sugar";
        public static final String PROTEIN = "Protein";
    }

    /*public static class FoodUser{
        public static final String TABLE_NAME = "FoodUser";
        public static final String ID = "Id_food_user";
        public static final String ID2 = FoodHistory.ID;
    }*/

    public static class FoodAvrage{
        public static final String TABLE_NAME = "FoodAvrage";
        public static final String DATE = "Date";
        public static final String CALORIES = "avg_calories";
        public static final String FAT = "avg_fat";
        public static final String CARBS = "avg_carbs";
        public static final String PROTEIN = "avg_protein";

    }
}
