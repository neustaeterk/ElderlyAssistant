package com.example.kenton.elderlyassistant;

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

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicationsListFragment extends Fragment {

    DatabaseHelper mDb;
    List<MedicationReminders> mMedList;
    ArrayAdapter<String> mMedListAdapter;

    public MedicationsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDb = new DatabaseHelper(getActivity());

        mMedList = new ArrayList<MedicationReminders>();

        mMedList = mDb.getAllReminders();

        ArrayList<String> medicationsNames = new ArrayList<String>();
        ArrayList<Integer> medicationDismiss = new ArrayList<Integer>();
        ArrayList<MedicationReminders> reminders = new ArrayList<MedicationReminders>();

        for (int i = 0; i < mMedList.size();i++){
            medicationsNames.add(mMedList.get(i).getMedicationName());
            //Log.d("XXXXXXX", medicationsNames.get(i));
            medicationDismiss.add(mMedList.get(i).getDismissed());
            reminders.add(mMedList.get(i));
        }

        View rootView = inflater.inflate(R.layout.fragment_medications_list, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_medications);

        mMedListAdapter = new ArrayAdapter<String>
                (getActivity(), R.layout.list_medications, R.id.list_medications_textview, medicationsNames);

        //listView.setAdapter(mMedListAdapter);

        CustomArrayAdapter mMedListAdapter2 = new CustomArrayAdapter(getActivity(), medicationsNames, reminders);
        listView.setAdapter(mMedListAdapter2);



        //View view = getViewByPosition(0, listView);
        //view.setBackgroundColor(Color.parseColor("red"));

        /*
        int count = mMedListAdapter.getCount();
        Log.d("List ", "Number of items: " + count);
        int firstPos = listView.getFirstVisiblePosition();
        Log.d("ListView ", "First visible position: " + firstPos);
        int listCount = listView.getChildCount();
        Log.d("ListView ", "Number of items: " + listCount);
        int lastPos = listView.getLastVisiblePosition();
        Log.d("ListView ", "Last visible position: " + lastPos);
        View view = mMedListAdapter.getView(firstPos, null, listView);
        view.setBackgroundColor(Color.GREEN);
        */

        //View view2 = listView.getChildAt(firstPos);
        //View view3 = listView.getAdapter().getView(0, view2, listView);
        //view3.setBackgroundColor(Color.GREEN);

        //TextView tv = (TextView)view2.findViewById(R.id.list_medications_textview);
        //tv.setText("some new text");

        //mMedListAdapter.notifyDataSetChanged();

        //int pos = 0;
        //View view = mMedListAdapter.getView(pos, (TextView)rootView.findViewById(R.id.list_medications_textview), listView);
        //view.setBackgroundColor(Color.parseColor("red"));

        return rootView;
    }

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
}
