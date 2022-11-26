package ma.ensam.petkeeper.config.app;

import android.Manifest;
import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AppConfig {
    public static final boolean DEBUG_MODE = false;

    public static final String[] galleryPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @SuppressLint("ConstantLocale")
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd MMM yyyy", Locale.getDefault()
    );
}