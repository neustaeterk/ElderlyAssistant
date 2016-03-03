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

    public  MedicationReminders()
    {

    }

    public MedicationReminders(int id, String time, String medicationName)
    {
        this.id = id;
        this.time = time;
        this.medicationName = medicationName;
    }

    public MedicationReminders(String time, String medicationName)
    {
        this.time = time;
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

    public String getMedicationName()
    {
        return this.medicationName;
    }
}
