package ma.ensam.petkeeper.config.app;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AppConfig {
    @SuppressLint("ConstantLocale")
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd MMM yyyy", Locale.getDefault()
    );
}