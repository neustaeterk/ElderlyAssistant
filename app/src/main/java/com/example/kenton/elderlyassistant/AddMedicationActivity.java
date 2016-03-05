package com.example.kenton.elderlyassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.sql.Time;

public class AddMedicationActivity extends AppCompatActivity {

    String photoName;
    String photoDir;
    long ID;
    boolean photoTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DatabaseHelper db = new DatabaseHelper(this);
        final PhotoManager photoManager = new PhotoManager(this);
        final MedicationReminders medicationReminders = new MedicationReminders();

        Button takePhotoButton = (Button) findViewById(R.id.takePhotoButton) ;
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.d("Starting Camera: ", "Initializing camera...");
                photoManager.takePicture();
                photoName = photoManager.getPhotoName();
                photoDir = photoManager.getPhotoDirectory();
                //start a new entry to the database with the photo name and directory
                medicationReminders.setPhotoName(photoName);
                medicationReminders.setPhotoDirectory(photoDir);

                ID = db.addReminder(medicationReminders);

                //change the id of the medicationReminder so that it can be updated
                medicationReminders.setId(ID);
                //update boolean so that the add button updates the entry already added
                photoTaken = true;
            }
        });

        Button addMedicationButton = (Button) findViewById(R.id.addMedicationButton) ;
        addMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.d("Insert: ", "Inserting...");

                EditText editText = (EditText) findViewById(R.id.medicationName);
                String medName = editText.getText().toString();

                TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
                String hour = "" + timePicker.getCurrentHour(); // getHour() for API 23
                String minute = "" + timePicker.getCurrentMinute(); //getMinute() for API 23
                String time = hour + ":" + minute;

                //just for testing
                String daysOfWeek = "M";

                medicationReminders.setTime(time);
                medicationReminders.setDaysOfWeek(daysOfWeek);
                medicationReminders.setMedicationName(medName);

                //if the photo was taken we update, otherwise add to database
                if (photoTaken)
                {
                    db.updateReminder(medicationReminders);
                }
                else
                {
                    db.addReminder(medicationReminders);
                }

                finish();
            }
        });

    }

}
