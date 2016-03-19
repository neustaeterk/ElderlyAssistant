package com.example.kenton.elderlyassistant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
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

    static final int REQUEST_SELECT_PHONE_NUMBER = 1;

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
                sendTextMessage("");
            }
        });

        Button findContactButton = (Button) findViewById(R.id.findContactButton) ;
        findContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                selectContact();
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

        Button goHomeButton = (Button) findViewById(R.id.goHomeButton) ;
        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {

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

    private void sendTextMessage(String message)
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

    public void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                // Do something with the phone number
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(number);

                // We need an Editor object to make preference changes.
                // All objects are from android.context.Context
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("contact_number", number);

                // Commit the edits!
                editor.commit();
            }
        }
    }

}
