package ma.ensam.petkeeper.utils;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    //Date to Long conversion
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    //Uri to String conversion

}
