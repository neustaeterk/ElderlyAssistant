package com.example.kenton.elderlyassistant;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class AddMedicationActivity extends AppCompatActivity {

    String photoName;
    String photoDir;
    long ID;
    boolean photoTaken;

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
        photoTaken = false;

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

                if (medName.equals(""))
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Please enter a medication name";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else
                {
                    TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
                    String hour = "" + timePicker.getCurrentHour(); // getHour() for API 23
                    String minute = "" + timePicker.getCurrentMinute(); //getMinute() for API 23
                    String time = hour + ":" + minute;

                    //just for testing
                    String daysOfWeek = "DMTWJFS";

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
                        ID = db.addReminder(medicationReminders);
                        medicationReminders.setId(ID);
                        medicationReminders.setDismissed(0);
                    }

                    int id = (int) medicationReminders.getId();
                    //Bitmap medPhoto = scaleImage(medicationReminders.getPhotoDirectory());
                    //sendNotification(bitmap, medName);
                    //scheduleNotification(getNotification(medName, medPhoto), Integer.parseInt(hour), Integer.parseInt(minute));
                    //scheduleNotification(getNotification(medName, medicationReminders.getPhotoDirectory()), Integer.parseInt(hour), Integer.parseInt(minute));
                    scheduleNotification(getNotification(medName, medicationReminders.getPhotoDirectory(), id),
                            Integer.parseInt(hour), Integer.parseInt(minute), id);

                    //Display confirmation message
                    Context context = getApplicationContext();
                    CharSequence text = "The reminder was added successfully";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    finish();
                }
            }
        });

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

    private void scheduleNotification(Notification notification, int hour, int minute, long Id) {
        Log.d("ID ", "In scheduleNotification: " + Id);
        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, (int) Id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) Id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //PendingIntent.
        long futureInMillis = SystemClock.elapsedRealtime() + 5000;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent );
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        //        AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.cancel(pendingIntent);
    }

    private Notification getNotification(String medName, String photoDir, int id) {

        Bitmap medPhoto = scaleImage(photoDir);
        //Intent dismissIntent = new Intent(this, )
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setAutoCancel(true)
                        .setContentTitle("Scheduled Medication")
                        .setContentText("Time to take " + medName);

        if (medPhoto != null)
        {
            NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
            bigPicStyle.bigPicture(medPhoto);
            bigPicStyle.setBigContentTitle("Time to take " + medName);
            mBuilder.setStyle(bigPicStyle);
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ScheduleActivity.class);
        resultIntent.putExtra("medId", id);

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
        mBuilder.addAction(R.drawable.ic_info_black_24dp, "Dismiss Alarm", resultPendingIntent);
        mBuilder.setContentIntent(resultPendingIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mBuilder.setSound(alarmSound);
        mBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);

        return mBuilder.build();
    }
}
