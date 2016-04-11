package com.example.kenton.elderlyassistant;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kenton on 2016-04-04.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Bitmap[] thumbnails;
    private String[] photoDates;

    public ImageAdapter(Context c, Bitmap[] thumbnails, String[] dates) {
        mContext = c;
        this.thumbnails = thumbnails;
        this.photoDates = dates;
    }

    public int getCount() {
        return thumbnails.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View rowView = inflater.inflate(R.layout.medical_records_layout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.dateTextView);
        textView.setText(photoDates[position]);
        Log.i("photos", photoDates[position]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.organizerImageView);
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            //imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            //imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(thumbnails[position]);
        return rowView;
    }
}