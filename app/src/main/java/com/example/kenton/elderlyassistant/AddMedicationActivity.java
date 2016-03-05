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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DatabaseHelper db = new DatabaseHelper(this);
        final PhotoManager photoManager = new PhotoManager(this);

        Button takePhotoButton = (Button) findViewById(R.id.takePhotoButton) ;
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.d("Starting Camera: ", "Initializing camera...");
                photoManager.takePicture();
                photoName = photoManager.getPhotoName();
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

                db.addReminder(new MedicationReminders(time, daysOfWeek, medName));
                finish();
            }
        });

    }

}
