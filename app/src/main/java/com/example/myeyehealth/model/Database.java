package com.example.myeyehealth.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myeyehealth.controller.AmslerGridMethods;
import com.example.myeyehealth.controller.ReminderMethods;
import com.example.myeyehealth.controller.SaccadesMethods;
import com.example.myeyehealth.controller.UserMethods;


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
    public static final String COLUMN_AG_CREATED_DATE = "created_date";
    public static final String COLUMN_AG_LEFT_GRID_SIZE = "left_grid_size";
    public static final String COLUMN_AG_RIGHT_GRID_SIZE = "right_grid_size";


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
    public static final String COLUMN_SACCADES_TEST_DATE = "test_date";

    public static final String TABLE_REMINDER = "remindertable";
    public static final String COLUMN_REMINDER_ID = "id";
    public static final String COLUMN_REMINDER_USER_ID = "user_id";
    public static final String COLUMN_REMINDER_DAY_OF_WEEK = "day_of_week";
    public static final String COLUMN_REMINDER_HOUR = "hour";
    public static final String COLUMN_REMINDER_MINUTE = "minute";
    public static final String COLUMN_REMINDER_REASON = "reason";
    public static final String COLUMN_REMINDER_COMPLETED = "completed";


    //Table Methods
    private static Database instance;
    private UserMethods userMethods;
    private AmslerGridMethods amslerGridMethods;
    private SaccadesMethods saccadesMethods;
    private ReminderMethods reminderMethods;

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
                COLUMN_AG_GRID + " TEXT," +
                COLUMN_AG_X_COORD + " INTEGER," +
                COLUMN_AG_Y_COORD + " INTEGER," +
                COLUMN_AG_CREATED_DATE + " TEXT," +
                COLUMN_AG_LEFT_GRID_SIZE + " INTEGER," +
                COLUMN_AG_RIGHT_GRID_SIZE + " INTEGER" +
                ")";

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
                COLUMN_SACCADES_TEST_DATE + " TEXT" +
                ");";
        db.execSQL(CREATE_SACCADES_TABLE);

        String CREATE_REMINDER_TABLE = "CREATE TABLE " + TABLE_REMINDER +
                "(" +
                COLUMN_REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_REMINDER_USER_ID + " INTEGER," +
                COLUMN_REMINDER_DAY_OF_WEEK + " INTEGER," +
                COLUMN_REMINDER_HOUR + " INTEGER," +
                COLUMN_REMINDER_MINUTE + " INTEGER," +
                COLUMN_REMINDER_REASON + " TEXT," +
                COLUMN_REMINDER_COMPLETED + " INTEGER" +
                ")";


        db.execSQL(CREATE_REMINDER_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AMSLER_GRID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SACCADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);

        onCreate(db);
    }


    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, null, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL);
            int passwordIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);
            int doctorNameIndex = cursor.getColumnIndex(COLUMN_USER_DOCTOR_NAME);
            int doctorEmailIndex = cursor.getColumnIndex(COLUMN_USER_DOCTOR_EMAIL);
            int carerNameIndex = cursor.getColumnIndex(COLUMN_USER_CARER_NAME);
            int carerEmailIndex = cursor.getColumnIndex(COLUMN_USER_CARER_EMAIL);

            String userName = nameIndex != -1 ? cursor.getString(nameIndex) : null;
            String userEmail = emailIndex != -1 ? cursor.getString(emailIndex) : null;
            String userPassword = passwordIndex != -1 ? cursor.getString(passwordIndex) : null;
            String doctorName = doctorNameIndex != -1 ? cursor.getString(doctorNameIndex) : null;
            String doctorEmail = doctorEmailIndex != -1 ? cursor.getString(doctorEmailIndex) : null;
            String carerName = carerNameIndex != -1 ? cursor.getString(carerNameIndex) : null;
            String carerEmail = carerEmailIndex != -1 ? cursor.getString(carerEmailIndex) : null;

            User user = new User(userId, userName, userEmail, userPassword, doctorName, doctorEmail, carerName, carerEmail);

            cursor.close();
            return user;
        }
        return null;
    }
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_DOCTOR_NAME, user.getDoctorName());
        values.put(COLUMN_USER_DOCTOR_EMAIL, user.getDoctorEmail());
        values.put(COLUMN_USER_CARER_NAME, user.getCarerName());
        values.put(COLUMN_USER_CARER_EMAIL, user.getCarerEmail());

        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }



}

