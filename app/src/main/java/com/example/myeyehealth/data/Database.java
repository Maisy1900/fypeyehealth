package com.example.myeyehealth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myeyehealth.model.User;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Amsler Grid table
    public static final String TABLE_AMSLER_GRID = "amslertable";
    public static final String COLUMN_AG_ID = "id";
    public static final String COLUMN_AG_USER_ID = "user_id";
    public static final String COLUMN_AG_TEST_ID = "test_id";
    public static final String COLUMN_AG_TEST_DATE = "test_date";
    public static final String COLUMN_AG_GRID = "grid";
    public static final String COLUMN_AG_X_COORD = "x_coord";
    public static final String COLUMN_AG_Y_COORD = "y_coord";
    public static final String COLUMN_AG_NOTES = "notes";
    public static final String COLUMN_AG_CREATED_DATE = "created_date";

    // User table
    public static final String TABLE_USER = "usertable";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_DOCTOR_NAME = "doctor_name";
    public static final String COLUMN_USER_DOCTOR_EMAIL = "doctor_email";
    public static final String COLUMN_USER_CARER_NAME = "carer_name";
    public static final String COLUMN_USER_CARER_EMAIL = "carer_email";

    // Saccades table
    public static final String TABLE_SACCADES = "saccadestable";
    public static final String COLUMN_SACCADES_ID = "id";
    public static final String COLUMN_SACCADES_USER_ID = "user_id";
    public static final String COLUMN_SACCADES_TEST_NUMBER = "test_number";
    public static final String COLUMN_SACCADES_TIME_TAKEN = "time_taken";
    public static final String COLUMN_SACCADES_DISTANCE = "distance";
    public static final String COLUMN_SACCADES_CREATED_DATE = "created_date";

    private static Database instance;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Amsler Grid table
        String CREATE_AMSLER_GRID_TABLE = "CREATE TABLE " + TABLE_AMSLER_GRID +
                "(" +
                COLUMN_AG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_AG_USER_ID + " INTEGER," +
                COLUMN_AG_TEST_ID + " INTEGER," +
                COLUMN_AG_TEST_DATE + " TEXT," +
                COLUMN_AG_GRID + " VARCHAR(1)," +
                COLUMN_AG_X_COORD + " INTEGER," +
                COLUMN_AG_Y_COORD + " INTEGER," +
                COLUMN_AG_NOTES + " TEXT," +
                COLUMN_AG_CREATED_DATE + " TEXT" +
                ");";
        db.execSQL(CREATE_AMSLER_GRID_TABLE);

        // Create User table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER +
                "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_NAME + " VARCHAR(100)," +
                COLUMN_USER_EMAIL + " VARCHAR(100)," +
                COLUMN_USER_PASSWORD + " VARCHAR(100)," +
                COLUMN_USER_DOCTOR_NAME + " VARCHAR(100)," +
                COLUMN_USER_DOCTOR_EMAIL + " VARCHAR(100)," +
                COLUMN_USER_CARER_NAME + " VARCHAR(100)," +
                COLUMN_USER_CARER_EMAIL + " VARCHAR(100)" +
                ")";
        db.execSQL(CREATE_USER_TABLE);
        // Create Saccades table
        String CREATE_SACCADES_TABLE = "CREATE TABLE " + TABLE_SACCADES +
                "(" +
                COLUMN_SACCADES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SACCADES_USER_ID + " INTEGER," +
                COLUMN_SACCADES_TEST_NUMBER + " INTEGER," +
                COLUMN_SACCADES_TIME_TAKEN + " FLOAT," +
                COLUMN_SACCADES_DISTANCE + " FLOAT," +
                COLUMN_SACCADES_CREATED_DATE + " DATETIME" +
                ")";
        db.execSQL(CREATE_SACCADES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AMSLER_GRID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SACCADES);

        // Create tables again
        onCreate(db);
    }


    public void addAmslerGridData(int user_id, int test_id, String test_date, String grid,
                                  int x_coord, int y_coord, String notes, String created_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AG_USER_ID, user_id);
        values.put(COLUMN_AG_TEST_ID, test_id);
        values.put(COLUMN_AG_TEST_DATE, test_date);
        values.put(COLUMN_AG_GRID, grid);
        values.put(COLUMN_AG_X_COORD, x_coord);
        values.put(COLUMN_AG_Y_COORD, y_coord);
        values.put(COLUMN_AG_NOTES, notes);
        values.put(COLUMN_AG_CREATED_DATE, created_date);
        db.insert(TABLE_AMSLER_GRID, null, values);
        db.close();
    }

    //user
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_DOCTOR_NAME, user.getDoctorName());
        values.put(COLUMN_USER_DOCTOR_EMAIL, user.getDoctorEmail());
        values.put(COLUMN_USER_CARER_NAME, user.getCarerName());
        values.put(COLUMN_USER_CARER_EMAIL, user.getCarerEmail());
        long id = db.insert(TABLE_USER, null, values);
        db.close();
        return id;
    }



    public boolean checkUserEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public void addSaccadesData(int user_id, int test_number, float time_taken,
                                float distance, String created_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SACCADES_USER_ID, user_id);
        values.put(COLUMN_SACCADES_TEST_NUMBER, test_number);
        values.put(COLUMN_SACCADES_TIME_TAKEN, time_taken);
        values.put(COLUMN_SACCADES_DISTANCE, distance);
        values.put(COLUMN_SACCADES_CREATED_DATE, created_date);
        db.insert(TABLE_SACCADES, null, values);
        db.close();
    }
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_DOCTOR_NAME,
                COLUMN_USER_DOCTOR_EMAIL,
                COLUMN_USER_CARER_NAME,
                COLUMN_USER_CARER_EMAIL
        };
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL);
            int passwordIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);
            int doctorNameIndex = cursor.getColumnIndex(COLUMN_USER_DOCTOR_NAME);
            int doctorEmailIndex = cursor.getColumnIndex(COLUMN_USER_DOCTOR_EMAIL);
            int carerNameIndex = cursor.getColumnIndex(COLUMN_USER_CARER_NAME);
            int carerEmailIndex = cursor.getColumnIndex(COLUMN_USER_CARER_EMAIL);
            if (idIndex >= 0 && nameIndex >= 0 && emailIndex >= 0 && passwordIndex >= 0 &&
                    doctorNameIndex >= 0 && doctorEmailIndex >= 0 && carerNameIndex >= 0 && carerEmailIndex >= 0) {
                int userId = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String userEmail = cursor.getString(emailIndex);
                String password = cursor.getString(passwordIndex);
                String doctorName = cursor.getString(doctorNameIndex);
                String doctorEmail = cursor.getString(doctorEmailIndex);
                String carerName = cursor.getString(carerNameIndex);
                String carerEmail = cursor.getString(carerEmailIndex);
                user = new User(userId, name, userEmail, password, doctorName, doctorEmail, carerName, carerEmail);
            }
        }
        cursor.close();
        db.close();
        return user;
    }
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_DOCTOR_NAME,
                COLUMN_USER_DOCTOR_EMAIL,
                COLUMN_USER_CARER_NAME,
                COLUMN_USER_CARER_EMAIL
        };
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL);
            int passwordIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);
            int doctorNameIndex = cursor.getColumnIndex(COLUMN_USER_DOCTOR_NAME);
            int doctorEmailIndex = cursor.getColumnIndex(COLUMN_USER_DOCTOR_EMAIL);
            int carerNameIndex = cursor.getColumnIndex(COLUMN_USER_CARER_NAME);
            int carerEmailIndex = cursor.getColumnIndex(COLUMN_USER_CARER_EMAIL);
            if (idIndex >= 0 && nameIndex >= 0 && emailIndex >= 0 && passwordIndex >= 0 &&
                    doctorNameIndex >= 0 && doctorEmailIndex >= 0 && carerNameIndex >= 0 && carerEmailIndex >= 0) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String userEmail = cursor.getString(emailIndex);
                String password = cursor.getString(passwordIndex);
                String doctorName = cursor.getString(doctorNameIndex);
                String doctorEmail = cursor.getString(doctorEmailIndex);
                String carerName = cursor.getString(carerNameIndex);
                String carerEmail = cursor.getString(carerEmailIndex);
                user = new User(id, name, userEmail, password, doctorName, doctorEmail, carerName, carerEmail);
            }
        }
        cursor.close();
        db.close();
        return user;
    }
    public void clearUserTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER);
        db.close();
    }





}
