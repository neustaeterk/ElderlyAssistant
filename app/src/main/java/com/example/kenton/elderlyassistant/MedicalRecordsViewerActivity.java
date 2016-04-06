package com.example.kenton.elderlyassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

public class MedicalRecordsViewerActivity extends AppCompatActivity {

    private int count;
    private Bitmap[] thumbnails;
    private Bitmap[] photos;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private ImageAdapter imageAdapter;
    private String directory;

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

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;

        Cursor imagecursor = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                MediaStore.Images.Media.DATA + " like ? ",
                new String[] {"/ElderlyAssistant/"+getString(R.string.photos_directory_appointment_summaries)},
                null);

        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
        this.count = imagecursor.getCount();
        this.thumbnails = new Bitmap[this.count];
        this.arrPath = new String[this.count];
        this.thumbnailsselection = new boolean[this.count];

        for (int i = 0; i < this.count; i++) {

            imagecursor.moveToPosition(i);

            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);

            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);

            arrPath[i]= imagecursor.getString(dataColumnIndex);

        }

        PhotoManager photoManager = new PhotoManager(this);
        photos = photoManager.getPictures(directory);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        //imageAdapter = new ImageAdapter(this, thumbnails);
        imageAdapter = new ImageAdapter(this, photos);
        gridview.setAdapter(imageAdapter);
        imagecursor.close();

        final Context context = this;

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(context, "" + position,
                //        Toast.LENGTH_SHORT).show();
                AlertDialog dialog = createDialog(photos[position]);
                dialog.show();
            }
        });
    }

    private AlertDialog createDialog(Bitmap photo)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MedicalRecordsViewerActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.medical_records_photo_viewer, null);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.zoomedPhotoView);
        imageView.setImageBitmap(photo);
        builder.setView(dialogView);

        // Add the buttons
        builder.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

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

}
