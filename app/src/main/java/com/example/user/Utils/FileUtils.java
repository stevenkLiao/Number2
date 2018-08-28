package com.example.user.Utils;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


import java.io.File;
import java.net.URISyntaxException;

public class FileUtils {

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Do nothing
            }
            finally {
                if( cursor != null ) cursor.close();
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String typefaceChecker(String path){
        if( path == null || path.isEmpty() ) return "";

        File file = new File(path);

        if( !file.exists() || !file.isFile() ) {
            return "";
        }

        String filename = file.getName();
        return filename;
    }
}

