package com.example.kenton.elderlyassistant;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
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
import android.widget.Toast;

import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DatabaseHelper db = new DatabaseHelper(this);

        if(getIntent().hasExtra("medId")){
            int medId = getIntent().getIntExtra("medId", 0);
            Log.d("ID", "" + medId);
            MedicationReminders medicationReminders = db.getMedicationReminder(medId);
            Log.d("Reminder ", medicationReminders.toString());
            medicationReminders.setDismissed(1);
            db.updateReminder(medicationReminders);
            Log.d("Reminder ", medicationReminders.toString());
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(medId);
            Context context = getApplicationContext();
            CharSequence text = "Notification Selected";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

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

        Button allMedButton = (Button) findViewById(R.id.medicationListButton);
        allMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, MedicationsList.class);
                startActivity(intent);
            }
        });

        /*Button sendNotificationButton = (Button) findViewById(R.id.sendNotificationButton) ;
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
        });*/

        /*Button cancelAlarmButton = (Button) findViewById(R.id.cancelAlarmButton) ;
        cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                MedicationReminders medicationReminders = db.getMedicationReminder(1);

                if (medicationReminders != null)
                {
                    long Id = medicationReminders.getId();
                    String medName = medicationReminders.getMedicationName();
                    Context context = getApplicationContext();
                    Notification notification = getNotification(medName, medicationReminders.getPhotoDirectory());
                    Intent notificationIntent = new Intent(context, NotificationPublisher.class);
                    notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, Id);
                    notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) Id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                }
            }
        });*/

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

    private Notification getNotification(String medName, String photoDir) {
        int mId = 1;
        Bitmap medPhoto = scaleImage(photoDir);
        //Intent dismissIntent = new Intent(this, )
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setAutoCancel(true)
                        .setContentTitle("Scheduled Medication")
                        .setContentText("Time to take " + medName);
        //.addAction(R.drawable.ic_info_black_24dp, "Dismiss Alarm", );

        if (medPhoto != null)
        {
            NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
            bigPicStyle.bigPicture(medPhoto);
            bigPicStyle.setBigContentTitle("Time to take " + medName);
            mBuilder.setStyle(bigPicStyle);
        }

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
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mBuilder.setSound(alarmSound);
        mBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);

        return mBuilder.build();
    }

    private Bitmap scaleImage(String mCurrentPhotoPath)
    {
        Bitmap bitmap;
        ImageView mImageView = (ImageView) findViewById(R.id.imageView);
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();
        //MedicationReminders medicationReminders = db.getMedicationReminder(1);
        //String mCurrentPhotoPath = medicationReminders.getPhotoDirectory();
        //String medName = medicationReminders.getMedicationName();
        Log.d("Path", "" + mCurrentPhotoPath);
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
        //int scaleFactor = photoH/256;
        int scaleFactor = 8;
        int inSampleSize = 1;

        /*
        if (photoH > targetH || photoW > targetW) {

            final int halfHeight = photoH / 2;
            final int halfWidth = photoW / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > targetH
                    && (halfWidth / inSampleSize) > targetW) {
                inSampleSize *= 2;
            }
        }
        */

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        //bmOptions.inSampleSize = inSampleSize;
        bmOptions.inPurgeable = true;

        Bitmap bitmap_decoded = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        if (bitmap_decoded != null)
        {
            bitmap = Bitmap.createScaledBitmap(bitmap_decoded, 256, 256, false);
        }
        else
        {
            bitmap = null;
        }
        mImageView.setImageBitmap(bitmap);

        return bitmap;
    }
}
