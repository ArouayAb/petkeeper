package ma.ensam.petkeeper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ma.ensam.petkeeper.R;

public class BitmapUtility {
    public static Bitmap extractFromUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= 29) {
            try(ParcelFileDescriptor pdf = context.getContentResolver().openFileDescriptor(
                    uri, "r")
            ) {
                bitmap = BitmapFactory.decodeFileDescriptor(pdf.getFileDescriptor());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapFactory.decodeFile(
                    uri.getPath()
            );
        }

        return bitmap;
    }

    public static Bitmap extractFromPath(String url) {
        if (url == null) {
            return Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        }
        FileInputStream fis = null;
        try {
            File imageFile = new File(url);
            fis = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(fis);
    }
}
