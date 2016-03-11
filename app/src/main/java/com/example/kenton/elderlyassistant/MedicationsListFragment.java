package com.example.kenton.elderlyassistant;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        for (int i = 0; i < mMedList.size();i++){
            medicationsNames.add(mMedList.get(i).getMedicationName());
            //Log.d("XXXXXXX", medicationsNames.get(i));
        }


        mMedListAdapter = new ArrayAdapter<String>
                (getActivity(), R.layout.list_medications, R.id.list_medications_textview, medicationsNames);

        View rootView = inflater.inflate(R.layout.fragment_medications_list, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_medications);
        listView.setAdapter(mMedListAdapter);

        return rootView;
    }
}
