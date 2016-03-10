package com.example.kenton.elderlyassistant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
                    String log = "Id: " + medReminder.getId() + " ,Time: " +
                                 medReminder.getTime() + " ,Day: " + medReminder.getDaysOfWeek() +
                                 " ,Name: " + medReminder.getMedicationName() +
                                 " ,Photo Name: " + medReminder.getPhotoName() +
                                 " ,Photo Directory: " + medReminder.getPhotoDirectory();
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

        Button sendNotificationButton = (Button) findViewById(R.id.sendNotificationButton) ;
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                ImageView mImageView = (ImageView) findViewById(R.id.imageView);
                // Get the dimensions of the View
                //int targetW = mImageView.getWidth();
                //int targetH = mImageView.getHeight();
                int targetW = 656;
                int targetH = 256;
                MedicationReminders medicationReminders = db.getMedicationReminder(1);
                String mCurrentPhotoPath = medicationReminders.getPhotoDirectory();
                String medName = medicationReminders.getMedicationName();
                Log.d("Path", mCurrentPhotoPath);
                Log.d("TargetDims ", "" + targetW + ", " + targetH);

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                Log.d("dims ", "" + photoW + ", " + photoH);

                // Determine how much to scale down the image
                //int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
                int scaleFactor = photoH/targetH;

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                Log.d("photo: ", "Scaled Dims: Height: " + bmOptions.outHeight + ", Width: " + bmOptions.outWidth);
                mImageView.setImageBitmap(bitmap);

                sendNotification(bitmap, medName);
            }
        });
    }

    private void sendNotification(Bitmap photo, String medName)
    {
        int mId = 1;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setAutoCancel(true)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
        bigPicStyle.bigPicture(photo);
        bigPicStyle.setBigContentTitle("Time to take " + medName);
        mBuilder.setStyle(bigPicStyle);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ScheduleActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ScheduleActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }
}
