package com.example.kenton.elderlyassistant;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Kenton on 2016-04-04.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Bitmap[] thumbnails;

    public ImageAdapter(Context c, Bitmap[] thumbnails) {
        mContext = c;
        this.thumbnails = thumbnails;
    }

    public int getCount() {
        //return mThumbIds.length;
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
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        //imageView.setImageResource(mThumbIds[position]);
        imageView.setImageBitmap(thumbnails[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp,
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp,
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp,
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp,
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp,
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp,
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp,
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp,
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp,
            //R.drawable.ic_info_black_24dp, R.drawable.ic_notifications_black_24dp
    };
}