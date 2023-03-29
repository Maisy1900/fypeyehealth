package com.example.myeyehealth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myeyehealth.model.User;

public class UserMethods {
    private final Context context;

    public UserMethods(Context context) {
        this.context = context;
    }
    //user
    public long addUser(User user) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_USER_NAME, user.getName());
        values.put(Database.COLUMN_USER_EMAIL, user.getEmail());
        values.put(Database.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(Database.COLUMN_USER_DOCTOR_NAME, user.getDoctorName());
        values.put(Database.COLUMN_USER_DOCTOR_EMAIL, user.getDoctorEmail());
        values.put(Database.COLUMN_USER_CARER_NAME, user.getCarerName());
        values.put(Database.COLUMN_USER_CARER_EMAIL, user.getCarerEmail());
        long id = db.insert(Database.TABLE_USER, null, values);
        db.close();
        return id;
    }



    public boolean checkUserEmailExists(String email) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {Database.COLUMN_USER_ID};
        String selection = Database.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(Database.TABLE_USER, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }
    public User getUserById(int userId) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {
                Database.COLUMN_USER_ID,
                Database.COLUMN_USER_NAME,
                Database.COLUMN_USER_EMAIL,
                Database.COLUMN_USER_PASSWORD,
                Database.COLUMN_USER_DOCTOR_NAME,
                Database.COLUMN_USER_DOCTOR_EMAIL,
                Database.COLUMN_USER_CARER_NAME,
                Database.COLUMN_USER_CARER_EMAIL
        };
        String selection = Database.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(Database.TABLE_USER, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Database.COLUMN_USER_ID);
            int nameIndex = cursor.getColumnIndex(Database.COLUMN_USER_NAME);
            int emailIndex = cursor.getColumnIndex(Database.COLUMN_USER_EMAIL);
            int passwordIndex = cursor.getColumnIndex(Database.COLUMN_USER_PASSWORD);
            int doctorNameIndex = cursor.getColumnIndex(Database.COLUMN_USER_DOCTOR_NAME);
            int doctorEmailIndex = cursor.getColumnIndex(Database.COLUMN_USER_DOCTOR_EMAIL);
            int carerNameIndex = cursor.getColumnIndex(Database.COLUMN_USER_CARER_NAME);
            int carerEmailIndex = cursor.getColumnIndex(Database.COLUMN_USER_CARER_EMAIL);
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
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        db.execSQL("DELETE FROM " + Database.TABLE_USER);
        db.close();
    }

    public User getUserByEmail(String email) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {
                Database.COLUMN_USER_ID,
                Database.COLUMN_USER_NAME,
                Database.COLUMN_USER_EMAIL,
                Database.COLUMN_USER_PASSWORD,
                Database.COLUMN_USER_DOCTOR_NAME,
                Database.COLUMN_USER_DOCTOR_EMAIL,
                Database.COLUMN_USER_CARER_NAME,
                Database.COLUMN_USER_CARER_EMAIL
        };
        String selection = Database.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(Database.TABLE_USER, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Database.COLUMN_USER_ID);
            int nameIndex = cursor.getColumnIndex(Database.COLUMN_USER_NAME);
            int emailIndex = cursor.getColumnIndex(Database.COLUMN_USER_EMAIL);
            int passwordIndex = cursor.getColumnIndex(Database.COLUMN_USER_PASSWORD);
            int doctorNameIndex = cursor.getColumnIndex(Database.COLUMN_USER_DOCTOR_NAME);
            int doctorEmailIndex = cursor.getColumnIndex(Database.COLUMN_USER_DOCTOR_EMAIL);
            int carerNameIndex = cursor.getColumnIndex(Database.COLUMN_USER_CARER_NAME);
            int carerEmailIndex = cursor.getColumnIndex(Database.COLUMN_USER_CARER_EMAIL);
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

}
