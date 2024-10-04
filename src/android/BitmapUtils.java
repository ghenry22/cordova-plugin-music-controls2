package com.homerours.musiccontrols;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
//import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class BitmapUtils {

    @Nullable
    public static Bitmap getBitmapCover(Context context, String coverURL) {
        try {
            if (coverURL.matches("^(https?|ftp)://.*$")) {
                // Remote image
                return getBitmapFromURL(coverURL);
            }

            else if (coverURL.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$")) {
                // Base image

                return getBitmapFromBase64(coverURL);
            } else {
                // Local image
                return getBitmapFromLocal(context, coverURL);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Nullable
    private static Bitmap getBitmapFromBase64(String encodedImage) {
        try {

            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap myBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return myBitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Nullable
    private static Bitmap getBitmapFromLocal(Context context, String localURL) {
        try {
            Uri uri = Uri.parse(localURL);
            File file = new File(uri.getPath());
            FileInputStream fileStream = new FileInputStream(file);
            BufferedInputStream buf = new BufferedInputStream(fileStream);
            Bitmap myBitmap = BitmapFactory.decodeStream(buf);
            buf.close();
            return myBitmap;
        } catch (Exception ex) {
            try {
                InputStream fileStream = context.getAssets().open("www/" + localURL);
                BufferedInputStream buf = new BufferedInputStream(fileStream);
                Bitmap myBitmap = BitmapFactory.decodeStream(buf);
                buf.close();
                return myBitmap;
            } catch (Exception ex2) {
                ex.printStackTrace();
                ex2.printStackTrace();
                return null;
            }
        }
    }

    @Nullable
    private static Bitmap getBitmapFromURL(String strURL) {
        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            URL url = new URL(strURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ignored) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}