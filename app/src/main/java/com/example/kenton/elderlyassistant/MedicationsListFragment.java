package com.example.kenton.elderlyassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicationsListFragment extends Fragment {

    DatabaseHelper mDb;
    //List<MedicationReminders> mMedList;
    ArrayAdapter<String> mMedListAdapter;
    ArrayList<MedicationReminders> reminders;
    CustomArrayAdapter mMedListAdapter2;

    public MedicationsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDb = new DatabaseHelper(getActivity());

        //mMedList = new ArrayList<MedicationReminders>();

        //mMedList = mDb.getAllReminders();

        reminders = new ArrayList<MedicationReminders>();
        reminders.addAll(mDb.getAllReminders());

        final ArrayList<String> medicationsNames = new ArrayList<String>();
        final ArrayList<Integer> medicationDismiss = new ArrayList<Integer>();
        //final ArrayList<MedicationReminders> reminders = new ArrayList<MedicationReminders>();

        /*for (int i = 0; i < mMedList.size();i++){
            medicationsNames.add(mMedList.get(i).getMedicationName());
            //Log.d("XXXXXXX", medicationsNames.get(i));
            medicationDismiss.add(mMedList.get(i).getDismissed());
            reminders.add(mMedList.get(i));
        }*/

        View rootView = inflater.inflate(R.layout.fragment_medications_list, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.listview_medications);

        mMedListAdapter = new ArrayAdapter<String>
                (getActivity(), R.layout.list_medications, R.id.list_medications_textview, medicationsNames);

        //listView.setAdapter(mMedListAdapter);

        //final CustomArrayAdapter mMedListAdapter2 = new CustomArrayAdapter(getActivity(), medicationsNames, reminders);
        final CustomArrayAdapter mMedListAdapter2 = new CustomArrayAdapter(getActivity(), reminders);
        listView.setAdapter(mMedListAdapter2);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MedicationReminders reminder = mMedListAdapter2.getItemAtPosition(position);
                //MedicationReminders reminder = (MedicationReminders) o;
                AlertDialog dialog = createDialog(view, position, mDb, reminder);
                dialog.show();
                reminders.clear();
                reminders.addAll(mDb.getAllReminders());
                /*
                medicationsNames.clear();
                for (int i = 0; i < reminders.size();i++) {
                    medicationsNames.add(reminders.get(i).getMedicationName());
                }*/
                mMedListAdapter2.notifyDataSetChanged();
                //mMedListAdapter2 = new CustomArrayAdapter(getActivity(), medicationsNames, reminders);
            }
        });



        //mMedListAdapter2.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //reminders.clear();
        //reminders.addAll(mDb.getAllReminders());
        //mMedListAdapter2.notifyDataSetChanged();

    }

    /**
     * This function is not used
     * @param pos
     * @param listView
     * @return
     */
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /*
     *  createDialog - creates the popup dialog which implements the "edit" and "delete" functions
     */
    private AlertDialog createDialog(View view, int pos, DatabaseHelper mDb, MedicationReminders mReminder)
    {
        final int position = pos;
        final View v = view;
        final DatabaseHelper db = mDb;
        final MedicationReminders reminder = mReminder;

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Choose an option")
                .setTitle(reminder.getMedicationName());
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
                        db.deleteReminder(reminder);
                    }
                });
                builderDelete.show();
            }
        });
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked edit
                Context context = getContext();
                Intent intent = new Intent(context, EditReminderActivity.class);
                int medId = (int) reminder.getId();
                intent.putExtra("medId", medId);
                context.startActivity(intent);
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
