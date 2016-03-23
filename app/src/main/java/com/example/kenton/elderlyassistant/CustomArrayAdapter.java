package com.example.kenton.elderlyassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kenton on 2016-03-18.
 */
public class CustomArrayAdapter extends ArrayAdapter<String>{
    private final Context context;
    private final ArrayList<String> values;
    //private final ArrayList<Integer> booleanValues;
    private final ArrayList<MedicationReminders> reminders;
    private DatabaseHelper db;

    public CustomArrayAdapter(Context context, ArrayList<String> values, ArrayList<MedicationReminders> reminders) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.reminders = reminders;
        db = new DatabaseHelper(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_medications_2, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_medications_textview_2);

        textView.setText(values.get(position));
        // change the colour depending on whether the user dismissed a notificaiton
        int dismissed = reminders.get(position).getDismissed();

        if (dismissed == 1)
        {
            textView.setBackgroundColor(Color.GREEN);
        }
        else
        {
            textView.setBackgroundColor(Color.RED);
        }

        /*rowView.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                String medName = values.get(position);
                AlertDialog dialog = createDialog(v, medName, position);
                dialog.show();
            }
        });
        this.notifyDataSetInvalidated();
        this.notifyDataSetChanged();
        */
        return rowView;
    }

    public String getItem(int position){

        return values.get(position);
    }

    public MedicationReminders getItemAtPosition(int position)
    {
        return reminders.get(position);
    }


    /*
     *  createDialog - creates the popup dialog which implements the "edit" and "delete" functions
     */
    private AlertDialog createDialog(View view, String medName, int pos)
    {
        final int position = pos;
        final View v = view;
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Choose an option")
                .setTitle(medName);
        // Add the buttons
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked delete button
                AlertDialog.Builder builderDelete = new AlertDialog.Builder(v.getContext());
                builderDelete.setTitle("Are you sure you want to delete this reminder?");
                builderDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked cancel
                    }
                });
                builderDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked yes
                        db.deleteReminder(reminders.get(position));
                        //refresh list somehow

                    }
                });
                builderDelete.show();
            }
        });
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked edit
                Intent intent = new Intent(context, EditReminderActivity.class);
                int medId = (int) reminders.get(position).getId();
                intent.putExtra("medId", medId);
                context.startActivity(intent);
                //refresh view
                reminders.clear();
                reminders.addAll(db.getAllReminders());
                values.clear();

                for (int i = 0; i < reminders.size();i++){
                    values.add(reminders.get(i).getMedicationName());
                }


            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                // no code required, the dialog is cancelled automatically
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        return dialog;
    }

}
