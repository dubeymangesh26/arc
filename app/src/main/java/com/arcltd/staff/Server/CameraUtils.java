package com.arcltd.staff.Server;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraUtils {    //Get Uri Of captured Image
    public static Uri getOutputMediaFileUri(Context context) {
        File mediaStorageDir = new File(
                context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "Camera");
        //If File is not present create directory
        if (!mediaStorageDir.exists()) {
            if (mediaStorageDir.mkdir())
                Log.e("Create Directory", "Main Directory Created : " + mediaStorageDir);
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());//Get Current timestamp
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");//create image path with system mill and image format
        /*if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + "com.kawawa.motors.provider", mediaFile);
        } else {*/
        return Uri.fromFile(mediaFile);
//        }

    }

    public static File getOutputMediaFile(Context context) {
        File mediaStorageDir = new File(
                context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "Camera");
        //If File is not present create directory
        if (!mediaStorageDir.exists()) {
            if (mediaStorageDir.mkdir())
                Log.e("Create Directory", "Main Directory Created : " + mediaStorageDir);
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());//Get Current timestamp
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");//create image path with system mill and image format
        /*if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + "com.kawawa.motors.provider", mediaFile);
        } else {*/
        return new File(getRealPathFromURI(Uri.fromFile(mediaFile), context));
//        }

    }

    private static String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getApplicationContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /*  Convert Captured image path into Bitmap to display over ImageView  */
    public static Bitmap convertImagePathToBitmap(String imagePath, boolean scaleBitmap) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);//Decode image path

        //If you want to scale bitmap/reduce captured image size then send true
        if (scaleBitmap)
            return Bitmap.createScaledBitmap(bitmap, 500, 500, true);
        else
            //if you don't want to scale bitmap then send false
            return bitmap;
    }


}
