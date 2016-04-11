package com.example.kenton.elderlyassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MedicalRecordsViewerActivity extends AppCompatActivity {

    private Bitmap[] photos;
    private ImageAdapter imageAdapter;
    private String directory;
    private File[] files;
    private String[] dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_records_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("directory")){
            directory = getIntent().getStringExtra("directory");
        }
        else {
            directory = getString(R.string.photos_directory_appointment_summaries);
        }

        final PhotoManager photoManager = new PhotoManager(this);
        //get compressed photos for the listView
        photos = photoManager.getPictures(directory);
        files = photoManager.getFiles(directory);
        dates = extractDates();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(this, photos, dates);
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                BitmapFactory bitmapFactory = new BitmapFactory();
                Bitmap uncompressedPhoto = bitmapFactory.decodeFile(files[position].getAbsolutePath());
                AlertDialog dialog = createDialog(uncompressedPhoto, files[position]);
                dialog.show();
            }
        });
    }

    private AlertDialog createDialog(Bitmap photo, File inFile)
    {
        final File file = inFile;
        AlertDialog.Builder builder = new AlertDialog.Builder(MedicalRecordsViewerActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.medical_records_photo_viewer, null);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.zoomedPhotoView);
        imageView.setImageBitmap(photo);
        builder.setView(dialogView);

        final Context context = this;

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked delete button
                AlertDialog.Builder builderDelete = new AlertDialog.Builder(MedicalRecordsViewerActivity.this);
                builderDelete.setTitle("Are you sure you want to delete this picture?");
                builderDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked cancel
                    }
                });
                builderDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked yes
                        boolean deleted = file.delete();
                        if (deleted) {
                            Toast.makeText(context, "The picture has been deleted",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "The picture could not be deleted",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Intent refresh = new Intent(context, MedicalRecordsViewerActivity.class);
                        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(refresh);
                    }
                });
                builderDelete.show();
            }
        });

        builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // cancelled
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private String[] extractDates() {
        String[] photoDates = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            // the date part of the filename based on the naming format when the picture was saved
            StringBuilder date = new StringBuilder(filename.substring(5, 13));
            date.insert(4, '-');
            date.insert(7, '-');

            photoDates[i] = date.toString();
        }

        return photoDates;
    }

}
