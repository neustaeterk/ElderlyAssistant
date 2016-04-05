package com.example.kenton.elderlyassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kenton on 2016-03-04.
 */
public class PhotoManager {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    String mCurrentPhotoPath;
    Activity activity;
    String imageFileName;
    String imageFileDirectory;

    PhotoManager (Activity activity)
    {
        this.activity = activity;
    }

    public String getPhotoName()
    {
        return imageFileName;
    }

    public String getPhotoDirectory()
    {
        return imageFileDirectory;
    }

    private File createImageFile(String directory) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_" + directory;
        File photosDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File storageDir = new File(photosDir + "/ElderlyAssistant/" + directory);

        // keep the storage directory to be able to be retrieved later
        //imageFileDirectory = storageDir.getPath();

        //make the desired directy if it doesn't exist
        if (!storageDir.exists())
        {
            storageDir.mkdirs();
        }

        //make the photo in the storageDir
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        imageFileDirectory = image.getAbsolutePath();
        return image;
    }

    public void takePicture(String directory)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(directory);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("Error: ", "Could not create Image File");
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                //make the photo available to see in the user's gallery
                //galleryAddPic();
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    public Bitmap[] getPictures(String directory)
    {
        Bitmap[] photos;
        File photosDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File storageDir = new File(photosDir + "/ElderlyAssistant/" + directory);
        FileFilter fileFilter = null;
        File[] files = storageDir.listFiles(fileFilter);
        photos = new Bitmap[files.length];
        BitmapFactory bitmapFactory = new BitmapFactory();
        for (int i=0; i < files.length; i++) {
            Log.i("debug", files[i].toString());
            //photos[i] = bitmapFactory.decodeFile(files[i].getAbsolutePath());
            photos[i] = scaleImage(files[i].getAbsolutePath());
        }
        return photos;
    }

    public Bitmap scaleImage(String mCurrentPhotoPath)
    {
        Bitmap bitmap;
        // Get the dimensions of the View
        //int targetW = mImageView.getWidth();
        //int targetH = mImageView.getHeight();

        Log.d("Path", "" + mCurrentPhotoPath);
        //Log.d("TargetDims ", "" + targetW + ", " + targetH);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        Log.d("dims ", "" + photoW + ", " + photoH);

        // Determine how much to scale down the image
        //int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        //int scaleFactor = photoH/256;
        int scaleFactor = 8;
        int inSampleSize = 1;

        /*
        if (photoH > targetH || photoW > targetW) {

            final int halfHeight = photoH / 2;
            final int halfWidth = photoW / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > targetH
                    && (halfWidth / inSampleSize) > targetW) {
                inSampleSize *= 2;
            }
        }
        */

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        //bmOptions.inSampleSize = inSampleSize;
        bmOptions.inPurgeable = true;

        Bitmap bitmap_decoded = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        if (bitmap_decoded != null)
        {
            bitmap = Bitmap.createScaledBitmap(bitmap_decoded, 256, 256, false);
        }
        else
        {
            bitmap = null;
        }
        //mImageView.setImageBitmap(bitmap);

        return bitmap;
    }
}
