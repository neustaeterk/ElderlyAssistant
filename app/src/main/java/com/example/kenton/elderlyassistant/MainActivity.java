package com.example.kenton.elderlyassistant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.security.Security;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hey changes
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Button sendMessageButton = (Button) findViewById(R.id.sendMessageButton) ;
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                //Intent intent = new Intent(MainActivity.this, CrazyFactsActivity.class) ;
                //startActivity(intent);
                sendTextMessage();
            }
        });

        Button findContactButton = (Button) findViewById(R.id.findContactButton) ;
        findContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {

            }
        });

        Button getGPSButton = (Button) findViewById(R.id.getGPSButton) ;
        getGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                double [] coordinates = getGPSCoordinates();
                TextView textView = (TextView) findViewById(R.id.textView);
                String coordinatesString = "" + coordinates[0] + ", " + coordinates[1];
                textView.setText(coordinatesString);
            }
        });

        Button sendNotificationButton = (Button) findViewById(R.id.sendNotificationButton) ;
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                sendNotification();
            }
        });

        Button medReminderButton = (Button) findViewById(R.id.medicationReminderButton) ;
        medReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class) ;
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class) ;
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendTextMessage()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String contactNumber = sharedPreferences.getString("contact_number", "preference not found");
        //SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage(contactNumber, null, "testing", null, null);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(contactNumber);
    }

    private double[] getGPSCoordinates()
    {
        double [] coordinates = new double[2]; // [latitude, longitude]
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //String locationProvider = LocationManager.NETWORK_PROVIDER;
        String locationProvider = LocationManager.GPS_PROVIDER;

        try {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            coordinates[0] = lastKnownLocation.getLatitude();
            coordinates[1] = lastKnownLocation.getLongitude();
        }
        catch (SecurityException ex) {
            Log.d("Elderly Assistant: ", "Error creating location service: " + ex.getMessage());
        }

        // Define a listener that responds to location updates
        /*LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };*/

        // Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        return coordinates;
    }

    private void sendNotification()
    {
        int mId = 1;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setAutoCancel(true)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
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
