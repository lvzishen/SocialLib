package com.lvsocialsdk.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Image util.
 * <p/>
 * Created by lvzishen on 16/12/2.
 */
public class ImageUtil {
    private static final String TAG = "ImageUtil";

    /**
     * Convert Bitmap to byte[]
     *
     * @param bitmap      the source bitmap
     * @param needRecycle need recycle
     * @return byte[]
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    /**
     * Scale bitmap with width and height.
     *
     * @param bitmap the source bitmap
     * @param w      the width
     * @param h      the height
     * @return the bitmap
     */
    public static Bitmap zoom(Bitmap bitmap, int w, int h) {
        if (null == bitmap) {
            return null;
        }

        try {
            float scaleWidth = w * 1.0f / bitmap.getWidth();
            float scaleHeight = h * 1.0f / bitmap.getHeight();

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);

            Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(result);

            canvas.drawBitmap(bitmap, matrix, null);

            return result;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * Get bitmap from file with the path.
     *
     * @param path the file path
     * @return the bitmap
     */
    public static Bitmap getBitmapFromFile(String path) {
        Bitmap bitmap = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(path);
            }
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }
}
