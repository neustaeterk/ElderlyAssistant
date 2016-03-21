package com.example.kenton.elderlyassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenton on 2016-02-24.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 5;

    // database and table names
    private static final String DATABASE_NAME = "reminders.db";
    private static final String TABLE_NAME = "medicationReminders";

    // Table column names
    private  static final String KEY_ID = "id";
    private static final String KEY_MED_NAME = "medication_name";
    private static final String KEY_TIME = "time";
    private static final String KEY_DAYS = "daysOfWeek";
    private static final String KEY_PHOTO_NAME = "photoName";
    private static final String KEY_PHOTO_DIR = "photoDir";
    private static final String KEY_DISMISSED = "dismissed";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Gets called if DB doesn’t exist.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TIME + " TEXT,"
                + KEY_DAYS + " TEXT,"
                + KEY_MED_NAME + " TEXT,"
                + KEY_PHOTO_NAME + " TEXT,"
                + KEY_PHOTO_DIR + " TEXT,"
                + KEY_DISMISSED + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Gets called if DB
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public long addReminder(MedicationReminders medReminder)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, medReminder.getTime());
        values.put(KEY_DAYS, medReminder.getDaysOfWeek());
        values.put(KEY_MED_NAME, medReminder.getMedicationName());
        values.put(KEY_PHOTO_NAME, medReminder.getPhotoName());
        values.put(KEY_PHOTO_DIR, medReminder.getPhotoDirectory());
        values.put(KEY_DISMISSED, medReminder.getDismissed());

        // Inserting Row
        long id = db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection

        return id;
    }

    public MedicationReminders getMedicationReminder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
        //                KEY_MED_NAME, KEY_TIME, KEY_DAYS, KEY_PHOTO_NAME, KEY_PHOTO_DIR, KEY_DISMISSED },
        //                KEY_ID + "=?",
        //                new String[] { String.valueOf(id) }, null, null, null, null);
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + "=?",
                                  new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MedicationReminders medReminder = new MedicationReminders(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), Integer.parseInt(cursor.getString(6)));

        return medReminder;
    }

    public List<MedicationReminders> getAllReminders() {
        List<MedicationReminders> reminderList = new ArrayList<MedicationReminders>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MedicationReminders medReminder = new MedicationReminders();
                medReminder.setId(Integer.parseInt(cursor.getString(0)));
                medReminder.setTime(cursor.getString(1));
                medReminder.setDaysOfWeek(cursor.getString(2));
                medReminder.setMedicationName(cursor.getString(3));
                medReminder.setPhotoName(cursor.getString(4));
                medReminder.setPhotoDirectory(cursor.getString(5));
                medReminder.setDismissed(Integer.parseInt(cursor.getString(6)));
                // Adding contact to list
                reminderList.add(medReminder);
            } while (cursor.moveToNext());
        }

        // return contact list
        return reminderList;
    }

    public int getRemindersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateReminder(MedicationReminders medReminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, medReminder.getTime());
        values.put(KEY_DAYS, medReminder.getDaysOfWeek());
        values.put(KEY_MED_NAME, medReminder.getMedicationName());
        values.put(KEY_PHOTO_NAME, medReminder.getPhotoName());
        values.put(KEY_PHOTO_DIR, medReminder.getPhotoDirectory());
        values.put(KEY_DISMISSED, medReminder.getDismissed());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(medReminder.getId())});
    }

    public void deleteReminder(MedicationReminders medReminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(medReminder.getId())});
        db.close();
    }

    public void deleteAllReminders()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
