package com.example.kenton.elderlyassistant;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kenton on 2016-02-23.
 */
public class MedicationReminders {

    long id;
    String time;
    String medicationName;
    String daysOfWeek;
    String photoName;
    String photoDirectory;
    int dismissed;

    public  MedicationReminders()
    {

    }

    public MedicationReminders(long id, String time, String daysOfWeek, String medicationName,
                               String photoName, String photoDirectory, int dismissed)
    {
        this.id = id;
        this.time = time;
        this.daysOfWeek = daysOfWeek;
        this.medicationName = medicationName;
        this.photoName = photoName;
        this.photoDirectory = photoDirectory;
        this.dismissed = dismissed;
    }

    public MedicationReminders(long id, String time, String daysOfWeek, String medicationName)
    {
        this.id = id;
        this.time = time;
        this.daysOfWeek = daysOfWeek;
        this.medicationName = medicationName;
        this.dismissed = 0;
    }

    public MedicationReminders(String time, String daysOfWeek, String medicationName)
    {
        this.time = time;
        this.daysOfWeek = daysOfWeek;
        this.medicationName = medicationName;
        this.dismissed = 0;
    }

    public MedicationReminders(String photoName, String photoDirectory)
    {
        this.photoName = photoName;
        this.photoDirectory = photoDirectory;
        this.dismissed = 0;
    }

    public void setId(long id)
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

    public void setPhotoName(String photoName)
    {
        this.photoName = photoName;
    }

    public void setPhotoDirectory(String photoDirectory)
    {
        this.photoDirectory = photoDirectory;
    }

    public void setDismissed(int dismissed)
    {
        this.dismissed = dismissed;
    }

    public long getId()
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

    public String getPhotoName()
    {
        return this.photoName;
    }

    public String getPhotoDirectory()
    {
        return this.photoDirectory;
    }

    public int getDismissed()
    {
        return this.dismissed;
    }

    public String toString()
    {
        return "Id: " + id + " ,Time: " +
                time + " ,Day: " + daysOfWeek +
                " ,Name: " + medicationName +
                " ,Photo Name: " + photoName +
                " ,Photo Directory: " + photoDirectory +
                " ,Dismissed: " + dismissed;
    }
}
