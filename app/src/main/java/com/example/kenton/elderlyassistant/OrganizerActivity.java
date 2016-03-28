package com.example.kenton.elderlyassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class OrganizerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final PhotoManager photoManager = new PhotoManager(this);

        Button takePhotoButton = (Button) findViewById(R.id.takePhotoOrganizerButton) ;
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String directory = getString(R.string.photos_directory_appointment_summaries);
                photoManager.takePicture(directory);
            }
        });
    }

}
