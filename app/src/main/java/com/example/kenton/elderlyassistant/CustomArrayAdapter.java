package com.example.kenton.elderlyassistant;

import android.content.Context;
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
    private final ArrayList<Integer> booleanValues;

    public CustomArrayAdapter(Context context, ArrayList<String> values, ArrayList<Integer> booleanValues) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.booleanValues = booleanValues;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_medications_2, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_medications_textview_2);

        textView.setText(values.get(position));
        // change the colour depending on whether the user dismissed a notificaiton
        if (booleanValues.get(position)==1) {
            textView.setBackgroundColor(Color.GREEN);
        } else {
            textView.setBackgroundColor(Color.RED);
        }

        return rowView;
    }

}
