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
import android.widget.TextView;

import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DatabaseHelper db = new DatabaseHelper(this);

        Button addReminderButton = (Button) findViewById(R.id.addReminderButton) ;
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                /*Log.d("Insert: ", "Inserting...");
                db.addReminder(new MedicationReminders("10:00", "Pill 1"));
                db.addReminder((new MedicationReminders("13:00", "Pill 2")));*/
                Intent intent = new Intent(ScheduleActivity.this, AddMedicationActivity.class) ;
                startActivity(intent);
            }
        });

        Button showRemindersButton = (Button) findViewById(R.id.showRemindersButton) ;
        showRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.d("Reading: ", "Reading all reminders..");
                List<MedicationReminders> reminders = db.getAllReminders();
                for (MedicationReminders medReminder : reminders) {
                    String log = "Id: " + medReminder.getId() + " ,Time: " + medReminder.getTime() + " ,Name: " + medReminder.getMedicationName();
                    // Writing Contacts to log
                    Log.d("Name: ", log);
                }
            }
        });

        Button deleteRemindersButton = (Button) findViewById(R.id.deleteRemindersButton) ;
        deleteRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                db.deleteAllReminders();
            }
        });
    }

}
