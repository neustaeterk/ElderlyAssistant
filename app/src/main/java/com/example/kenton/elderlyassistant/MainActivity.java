package com.example.kenton.elderlyassistant;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_SELECT_PHONE_NUMBER = 1;
    static final int REQUEST_GPS = 2;
    private static final int FORMAT_DEGREES = 0;
    private static final int RESET_DISMISSED_ID = -2;
    private TextView coordinatesText, addressText;
    private LocationManager locationManager;
    private String locationProviderGPS;
    private String locationProviderNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //scheduleResetMedicationDismissed();

        Button getGPSButton = (Button) findViewById(R.id.getGPSButton) ;
        getGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String[] coordinates = getGPSCoordinates();
                //coordinatesText = (TextView) findViewById(R.id.textView);
                //addressText = (TextView) findViewById(R.id.textView2);
                //String coordinatesString = "" + coordinates[0] + ", " + coordinates[1];
                //coordinatesText.setText(coordinatesString);

                //String message = "Emergency received from location:\n https://maps.google.com/maps?q=" + coordinates[0] + "," + coordinates[1];


                String[] testcoordinates = {"45.4951488", "-73.5763037"};
                /*if (!coordinates[0].equals("no location available")){
                    String address = findAddress(testcoordinates);
                    addressText.setText(address);
                    Log.d("Address", address);
                    if (!address.equals("Address not found.")) {
                        message = message + "\n" + address;
                    }
                    sendTextMessage(message);
                }*/
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

        Button medicalRecordOrganizerButton = (Button) findViewById(R.id.medicalRecordOrganizerButton) ;
        medicalRecordOrganizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(MainActivity.this, OrganizerActivity.class) ;
                startActivity(intent);
            }
        });

        Button goHomeButton = (Button) findViewById(R.id.goHomeButton) ;
        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String address = sharedPreferences.getString("home_address", "preference not found");
                if (address.equals("preference not found") || address.equals(""))
                {
                    setAddress();
                }
                else {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(address));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            }
        });

        Button helpButton = (Button) findViewById(R.id.helpButton) ;
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(MainActivity.this, HelpMainActivity.class) ;
                startActivity(intent);
            }
        });

        Button preferencesButton = (Button) findViewById(R.id.preferencesButton) ;
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(MainActivity.this, PreferencesActivity.class) ;
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
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
                //TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText(number);

                // We need an Editor object to make preference changes.
                // All objects are from android.context.Context
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("contact_number", number);

                // Commit the edits!
                editor.commit();
            }

            if (requestCode == REQUEST_GPS){
                String[] coordinates = getGPSCoordinates();
            }
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        // Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(locationProvider, 0, 0, locListener);
    }

    // Code below about LocationListener refer to http://developer.android.com/guide/topics/location/strategies.html
    // Define a listener that responds to location updates
    LocationListener locListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            //displayLocation(location);
            Log.d("location", location.toString());
            String lat = Location.convert(location.getLatitude(), FORMAT_DEGREES);
            String longitude = Location.convert(location.getLongitude(), FORMAT_DEGREES);
            String[] coordinates = {lat, longitude};
            String address = findAddress(coordinates);
            String message = "";
            if (!address.equals("Address not found."))
            {
                message = address;
                //addressText.setText(address);
            }

            try
            {
                locationManager.removeUpdates(locListenerNetwork);
                message = message + "\nEmergency received from location: https://maps.google.com/maps?q=" + lat + "," + longitude;
                sendTextMessage(message);
            }
            catch (SecurityException se)
            {

            }

            Context context = getApplicationContext();
            CharSequence text = "Successfully sent your location coordinates.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }

        // "Your LocationListener must implement several callback methods that the Location Manager
        // calls when the user location changes or when the status of the service changes."
        // Left empty intentionally here
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onProviderDisabled(String provider) {
        }
    };


    // Code below about LocationListener refer to http://developer.android.com/guide/topics/location/strategies.html
    // Define a listener that responds to location updates
    LocationListener locListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.

            //displayLocation(location);
            Log.d("location", location.toString());
            String lat = Location.convert(location.getLatitude(), FORMAT_DEGREES);
            String longitude = Location.convert(location.getLongitude(), FORMAT_DEGREES);
            String[] coordinates = {lat, longitude};
            String address = findAddress(coordinates);
            String message = "";
            if (!address.equals("Address not found."))
            {
                message = address;
                //addressText.setText(address);
            }

            try
            {
                locationManager.removeUpdates(locListenerGPS);
                message = message + "\nNetwork\nEmergency received from location: https://maps.google.com/maps?q=" + lat + "," + longitude;
                sendTextMessage(message);
            }
            catch (SecurityException se)
            {

            }

            Context context = getApplicationContext();
            CharSequence text = "Successfully sent your location coordinates.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }

        // "Your LocationListener must implement several callback methods that the Location Manager
        // calls when the user location changes or when the status of the service changes."
        // Left empty intentionally here
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onProviderDisabled(String provider) {
        }
    };

    /**
     *
     * @param loc - Location to be displayed
     */
    public void displayLocation(Location loc) {
        if (loc != null) {
            String lat = Location.convert(loc.getLatitude(), FORMAT_DEGREES);
            String longitude = Location.convert(loc.getLongitude(), FORMAT_DEGREES);
            //coordinatesText.setText(lat + ", " + longitude);

        } else {
            String lat = "no location available";
            String longitude = "no location available";
            //coordinatesText.setText(lat + ", " + longitude);
        }
    }

    private String findAddress(String[] coordinates){
        Geocoder geocoder = new Geocoder(this);
        double latitude = Double.parseDouble(coordinates[0]);
        double longitude = Double.parseDouble(coordinates[1]);
        List<Address> addressList = new ArrayList<>();;
        String address;

        if(geocoder.isPresent()){
            try {
                addressList = geocoder.getFromLocation(latitude, longitude, 1);
            }
            catch(IOException geo){

            }
        }

        if(!addressList.isEmpty()){
            Address addressObject =addressList.get(0);
            address = addressObject.getAddressLine(0) + ", " + addressObject.getAddressLine(1) + ", " + addressObject.getAddressLine(2);
        }
        else{
            address = "Address not found.";
        }

        return address;
    }

    private String[] getGPSCoordinates()
    {
        String [] coordinates = new String[2]; // [latitude, longitude]
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Your location is disabled. Would you like to enable it?");
            final Context context = this.getApplicationContext();
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();

        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String defaultNumber = getString(R.string.pref_default_contact_number);
        String contactNumber = sharedPreferences.getString("contact_number", defaultNumber);

        if(contactNumber.equals(defaultNumber)){
            selectContact(true);
            return coordinates;
        }

        locationProviderNetwork = LocationManager.NETWORK_PROVIDER;
        locationProviderGPS = LocationManager.GPS_PROVIDER;


        try {
            //Location lastKnownLocation = locationManager.getLastKnownLocation(locationProviderGPS);

            if(gps_enabled)
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListenerGPS);
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locListenerGPS,null);
            if(network_enabled)
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListenerNetwork);
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locListenerNetwork,null);

            //locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locListener);
            //locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

            /*if (lastKnownLocation != null) {
                coordinates[0] = (Location.convert(lastKnownLocation.getLatitude(), FORMAT_DEGREES));
                coordinates[1] = (Location.convert(lastKnownLocation.getLongitude(), FORMAT_DEGREES));

            } else {
                coordinates[0] =("no location available");
                coordinates[1] =("no location available");
            }*/


        }
        catch (SecurityException ex) {
            Log.d("Elderly Assistant: ", "Error creating location service: " + ex.getMessage());
        }

        return coordinates;
    }

    public void selectContact(boolean getGPS) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (getGPS){
                startActivityForResult(intent, REQUEST_GPS);
            }
            else {
                startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
            }
        }
    }

    private void sendTextMessage(String message)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String contactNumber = sharedPreferences.getString("contact_number", "preference not found");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(contactNumber, null, message, null, null);
        //TextView textView = (TextView) findViewById(R.id.textView);
        //textView.setText(contactNumber);
    }

    private void scheduleResetMedicationDismissed()
    {
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 27);

        Intent intent = new Intent(this, ResetMedicationsDismissed.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, RESET_DISMISSED_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void setAddress()
    {
        final LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Home Address");
        alertDialog.setMessage("Please enter your address: ");

        final View dialogView = inflater.inflate(R.layout.dialog_edit_text, null);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currentAddress = sharedPreferences.getString("home_address", "preference not found");

        final EditText input = (EditText) dialogView.findViewById(R.id.input);
        input.setText(currentAddress);

        alertDialog.setView(dialogView);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String address = input.getText().toString();
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("home_address", address);
                        // Commit the edits!
                        editor.commit();
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(address));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog disappears and cancels automatically
                    }
                });

        alertDialog.show();
    }
}
