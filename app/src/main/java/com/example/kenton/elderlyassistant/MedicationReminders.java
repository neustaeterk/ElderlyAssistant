package com.example.kenton.elderlyassistant;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kenton on 2016-02-23.
 */
public class MedicationReminders {

    int id;
    String time;
    String medicationName;
    String daysOfWeek;

    public  MedicationReminders()
    {

    }

    public MedicationReminders(int id, String time, String daysOfWeek, String medicationName)
    {
        this.id = id;
        this.time = time;
        this.daysOfWeek = daysOfWeek;
        this.medicationName = medicationName;
    }

    public MedicationReminders(String time, String daysOfWeek, String medicationName)
    {
        this.time = time;
        this.daysOfWeek = daysOfWeek;
        this.medicationName = medicationName;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public void setDaysOfWeek(String daysOfWeek)
    {
        this.daysOfWeek = daysOfWeek;
    }

    public void setMedicationName(String medicationName)
    {
        this.medicationName = medicationName;
    }

    public int getId()
    {
        return this.id;
    }

    public String getTime()
    {
        return this.time;
    }

    public String getDaysOfWeek ()
    {
        return this.daysOfWeek;
    }

    public String getMedicationName()
    {
        return this.medicationName;
    }
}
