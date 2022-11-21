package ma.ensam.petkeeper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

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
}
