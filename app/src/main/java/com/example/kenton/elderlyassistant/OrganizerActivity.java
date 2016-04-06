package com.example.kenton.elderlyassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OrganizerActivity extends AppCompatActivity {

    final int TAKE_PICTURE_CODE = 1;
    final int GET_PICTURE_CODE = 2;
    PhotoManager photoManager;
    String directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoManager = new PhotoManager(this);

        Button takePhotoButton = (Button) findViewById(R.id.takePhotoOrganizerButton) ;
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                AlertDialog alertDialog = createDialog(TAKE_PICTURE_CODE);
                alertDialog.show();
            }
        });

        Button viewMedicalRecordsButton = (Button) findViewById(R.id.viewMedicalRecordsButton) ;
        viewMedicalRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                AlertDialog alertDialog = createDialog(GET_PICTURE_CODE);
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoManager.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Context context = getApplicationContext();
            CharSequence text = "Picture Saved";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    private AlertDialog createDialog(int code)
    {
        final int CODE = code;
        AlertDialog.Builder builder = new AlertDialog.Builder(OrganizerActivity.this);
        builder.setMessage("Choose a category");

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.organizer_category_selector, null);
        builder.setView(dialogView);

        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.categoriesRadioGroup);
                int checkedButton = radioGroup.getCheckedRadioButtonId();
                if (checkedButton == R.id.appointmentSummariesRadioButton) {
                    directory = getString(R.string.photos_directory_appointment_summaries);
                }
                else if (checkedButton == R.id.testResultsRadioButton) {
                    directory = getString(R.string.photos_directory_test_results);
                }
                else if (checkedButton == R.id.miscRadioButton) {
                    directory = getString(R.string.photos_directory_miscellaneous);
                }
                else {
                    directory = "Error: checked button";
                }

                if (CODE == TAKE_PICTURE_CODE) {
                    photoManager.takePicture(directory);
                }
                else if (CODE == GET_PICTURE_CODE) {
                    Intent intent = new Intent(OrganizerActivity.this, MedicalRecordsViewerActivity.class) ;
                    intent.putExtra("directory", directory);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // cancelled
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        return dialog;
    }

}
