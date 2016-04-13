package com.example.kenton.elderlyassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PreferencesActivity extends AppCompatActivity {

    static final int REQUEST_SELECT_PHONE_NUMBER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button findContactButton = (Button) findViewById(R.id.findContactButton) ;
        findContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                selectContact();
            }
        });

        final LayoutInflater inflater = this.getLayoutInflater();
        Button setAddressButton = (Button) findViewById(R.id.setAddressButton) ;
        setAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreferencesActivity.this);
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
        }
    }

    public void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
        }
    }

}
